package output;

import madog.core.Output;
import madog.core.Print;
import madog.core.Ref;

public class c00_Index extends Output {

    @Override
    public void addMarkDownAsCode() {

        Print.h1("VURLP");
        Print.wrapped("Validation and object mapping for URL Parameters, using Bean Validation and GSON.");

        Print.h2("Install");
        Print.codeBlock("mvn clean install");

        Print.codeBlock("" +
                "<dependency>\n" +
                "    <groupId>io.github.53rg3</groupId>\n" +
                "    <artifactId>vurlp</artifactId>\n" +
                "    <version>0.1</version>\n" +
                "</dependency>" +
                "");


        Print.h2("Quickstart");
        Print.wrapped("With `vurlp` you can express URL parameters as POJO and validate them using standard Bean Validation " +
                "annotations. So instead of handling strings we get type-safe object representations.");

        Print.wrapped("You need to create a `vurlp` instance for every class which shall represent URL parameters. Additional " +
                "constructors can set your own GSON implementation and disable URL encoding/decoding. See " +
                "" + Ref.internalPath("/src/test/java/core/VurlpTest.java", "Tests") + " for usage examples. " +
                "See " +
                "" + Ref.internalPath("/src/test/java/assets/TestPojo.java", "TestPojo.java") + " for a simple POJO to " +
                "represent URL parameters.");

        Print.wrapped("Use `.toParams(String value)` to convert a POJO into an URL query string. " +
                "Use `.fromParams(Map<String,String> urlParamsAsMap)` or `.fromParams(String urlParamsAsString)` to map " +
                "URL parameters or a URL query string to your POJO. All these methods return an `VurlpOptional<YourType>`, " +
                "which works similar to a standard `Optional`. If the URL parameters don't comply to the defined validations then " +
                "the violations can be retrieved via `.getViolations()`." +
                "\n\n" +
                "Example:");

        Print.codeBlock("" +
                "Vurlp<TestPojo> vurlp = new Vurlp<>(TestPojo.class);\n" +
                "VurlpOptional<TestPojo> vurlpOptional = vurlp.fromParams(\"?simpleString=someString&floatObject=1.0\");\n" +
                "if(vurlpOptional.isValid()) {\n" +
                "    TestPojo testPojo = vurlpOptional.get();\n" +
                "} else {\n" +
                "    System.out.println(vurlpOptional.getViolationsAsString());\n" +
                "}" +
                "");

    }

}
