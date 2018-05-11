package core;

import assets.TestPojo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class VurlpTest {

    private Vurlp<TestPojo> vurlp = new Vurlp<>(TestPojo.class);
    private Vurlp<TestPojo> vurlpWithoutEncoding = new Vurlp<>(TestPojo.class, false);


    @Test
    public void fromParamsViaMapValid() {
        Map<String,String> map1 = new HashMap<>();
        map1.put("floatObject", "1");
        map1.put("simpleString", "some+Stri%3Dng");

        Map<String,String> map2 = new HashMap<>();
        map2.put("floatObject", "1");
        map2.put("simpleString", "some+Stri%3Dng");

        VurlpOptional<TestPojo> params = this.vurlp.fromParams(map1);
        VurlpOptional<TestPojo> paramsWithoutDecoding = this.vurlpWithoutEncoding.fromParams(map2);
        TestPojo testPojo1 = params.get();
        TestPojo testPojo2 = paramsWithoutDecoding.get();


        assertThat(params.isValid(), is(true));
        assertThat(testPojo1.getFloatObject(), is(1.0F));
        assertThat(testPojo1.getSimpleString(), is("some Stri=ng"));

        assertThat(paramsWithoutDecoding.isValid(), is(true));
        assertThat(testPojo2.getFloatObject(), is(1.0F));
        assertThat(testPojo2.getSimpleString(), is("some+Stri%3Dng"));
    }

    @Test
    public void fromParamsViaMapInvalid() {
        Map<String,String> map = new HashMap<>();
        map.putIfAbsent("simpleString", "someString");
        VurlpOptional<TestPojo> params = this.vurlp.fromParams(map);

        assertThat(params.isValid(), is(false));
        assertThat(params.getViolations().size(), is(1));
        assertThat(params.getViolationsAsString(), containsString("'floatObject' must not be null"));
    }

    @Test
    public void fromParamsViaString() {
        Map<String,String> map1 = new HashMap<>();
        map1.put("floatObject", "1");
        map1.put("simpleString", "some+Stri%3Dng");
        VurlpOptional<TestPojo> params = this.vurlp.fromParams(map1);
        TestPojo testPojo1 = params.get();

        assertThat(params.isValid(), is(true));
        assertThat(testPojo1.getFloatObject(), is(1.0F));
        assertThat(testPojo1.getSimpleString(), is("some Stri=ng"));
    }

    @Test
    public void toParamsValid() {
        TestPojo validPojo = new TestPojo("someString", 1.0F);
        TestPojo validPojo2 = new TestPojo("some Stri=ng", 1.0F);

        VurlpOptional<String> paramsWithEncoding = this.vurlp.toParams(validPojo2, true);
        VurlpOptional<String> paramsWithPrefix = this.vurlp.toParams(validPojo, true);
        VurlpOptional<String> paramsWithoutPrefix = this.vurlp.toParams(validPojo, false);

        assertThat(paramsWithPrefix.isValid(), is(true));
        assertThat(paramsWithoutPrefix.isValid(), is(true));
        assertThat(paramsWithPrefix.get(), is("?simpleString=someString&floatObject=1.0"));
        assertThat(paramsWithoutPrefix.get(), is("simpleString=someString&floatObject=1.0"));
        assertThat(paramsWithEncoding.get(), is("?simpleString=some+Stri%3Dng&floatObject=1.0"));
    }

    @Test
    public void toParamsInvalid() {
        TestPojo invalidPojo = new TestPojo("anotherString");
        VurlpOptional params = this.vurlp.toParams(invalidPojo, true);

        assertThat(params.isValid(), is(false));
        assertThat(params.getViolations().size(), is(1));
        assertThat(params.getViolationsAsString(), containsString("'floatObject' must not be null"));
    }

}
