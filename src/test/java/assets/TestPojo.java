package assets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TestPojo {

    private String simpleString;

    @NotNull(message = "'floatObject' must not be null")
    @Min(value = 1, message = "'floatObject' must be >=1")
    private Float floatObject;

    public TestPojo() {

    }

    public TestPojo(final String simpleString) {
        this.simpleString = simpleString;
    }

    public TestPojo(final String simpleString, final Float floatObject) {
        this.simpleString = simpleString;
        this.floatObject = floatObject;
    }

    public String getSimpleString() {
        return this.simpleString;
    }

    public void setSimpleString(final String simpleString) {
        this.simpleString = simpleString;
    }

    public Float getFloatObject() {
        return this.floatObject;
    }

    public void setFloatObject(final Float floatObject) {
        this.floatObject = floatObject;
    }

    @Override
    public String toString() {
        return "TestPojo{" +
                "simpleString='" + this.simpleString + '\'' +
                ", floatObject=" + this.floatObject +
                '}';
    }
}
