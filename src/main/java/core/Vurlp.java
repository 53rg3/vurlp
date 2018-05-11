package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public class Vurlp<T> {

    private final Class<T> clazz;
    private final Gson gson;
    private final boolean shouldUseUrlEncoding;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = this.factory.getValidator();
    private final Type mapStringStringType = new TypeToken<Map<String, String>>() {
    }.getType();
    private final Pattern queryPrefixPattern = Pattern.compile("^\\?");

    public Vurlp(final Class<T> clazz) {
        this.clazz = clazz;
        this.shouldUseUrlEncoding = true;
        this.gson = new Gson();
    }

    public Vurlp(final Class<T> clazz, final boolean shouldUseUrlEncoding) {
        this.clazz = clazz;
        this.shouldUseUrlEncoding = shouldUseUrlEncoding;
        this.gson = new Gson();
    }

    public Vurlp(final Class<T> clazz, final Gson gson, final boolean shouldUseUrlEncoding) {
        this.clazz = clazz;
        this.shouldUseUrlEncoding = shouldUseUrlEncoding;
        this.gson = gson;
    }

    public VurlpOptional<T> fromParams(final Map<String, String> urlParamsAsMap) {

        if (this.shouldUseUrlEncoding) {
            for (Entry<String, String> entry : urlParamsAsMap.entrySet()) {
                urlParamsAsMap.put(entry.getKey(), this.decode(entry.getValue()));
            }
        }
        final T mappedObject = this.gson.fromJson(this.gson.toJsonTree(urlParamsAsMap), this.clazz);

        final Set<ConstraintViolation<T>> violations = this.validator.validate(mappedObject);
        if (!violations.isEmpty()) {
            return VurlpOptional.invalid(violations);
        } else {
            return VurlpOptional.of(mappedObject);
        }
    }

    public VurlpOptional<T> fromParams(String urlParams) {
        urlParams = this.queryPrefixPattern.matcher(urlParams).replaceFirst("");
        Map<String, String> map = new HashMap<>();
        for (String param : urlParams.split("&")) {
            String[] split = param.split("=", 2);
            map.put(split[0], split[0] == null ? null : split[1]);
        }

        return this.fromParams(map);
    }

    public VurlpOptional<String> toParams(final T object, final boolean prependQuestionMark) {

        final Set<ConstraintViolation<T>> violations = this.validator.validate(object);
        if (!violations.isEmpty()) {
            return VurlpOptional.invalid(violations);
        }

        final Map<String, String> map = this.gson.fromJson(this.gson.toJsonTree(object), this.mapStringStringType);
        final StringBuilder stringBuilder = new StringBuilder();

        if (prependQuestionMark) {
            stringBuilder.append("?");
        }

        int parameterCount = 0;
        for (final Entry<String, String> entry : map.entrySet()) {
            parameterCount++;

            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            if (this.shouldUseUrlEncoding) {
                stringBuilder.append(this.encode(entry.getValue()));
            } else {
                stringBuilder.append(entry.getValue());
            }
            if (parameterCount < map.size()) {
                stringBuilder.append("&");
            }
        }

        return VurlpOptional.of(stringBuilder.toString());
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return URLEncoder.encode(value);
        }
    }

    private String decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return URLDecoder.decode(value);
        }
    }


}
