## Table of Contents
[1. VURLP](#vurlp)<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[1.1 Install](#install)<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[1.2 Quickstart](#quickstart)<br>
# VURLP

Validation and object mapping for URL Parameters, using Bean Validation and GSON.

## Install

```JAVA
mvn clean install
```


```JAVA
<dependency>
    <groupId>io.github.53rg3</groupId>
    <artifactId>vurlp</artifactId>
    <version>0.1</version>
</dependency>
```

## Quickstart

With `vurlp` you can express URL parameters as POJO and validate them using standard Bean Validation annotations. So instead of handling strings we get type-safe object representations.


You need to create a `vurlp` instance for every class which shall represent URL parameters. Additional constructors can set your own GSON implementation and disable URL encoding/decoding. See [Tests](/src/test/java/core/VurlpTest.java) for usage examples. See [TestPojo.java](/src/test/java/assets/TestPojo.java) for a simple POJO to represent URL parameters.


Use `.toParams(String value)` to convert a POJO into an URL query string. Use `.fromParams(Map<String,String> urlParamsAsMap)` or `.fromParams(String urlParamsAsString)` to map URL parameters or a URL query string to your POJO. All these methods return an `VurlpOptional<YourType>`, which works similar to a standard `Optional`. If the URL parameters don't comply to the defined validations then the violations can be retrieved via `.getViolations()`.

Example:


```JAVA
Vurlp<TestPojo> vurlp = new Vurlp<>(TestPojo.class);
VurlpOptional<TestPojo> vurlpOptional = vurlp.fromParams("?simpleString=someString&floatObject=1.0");
if(vurlpOptional.isValid()) {
    TestPojo testPojo = vurlpOptional.get();
} else {
    System.out.println(vurlpOptional.getViolationsAsString());
}
```

