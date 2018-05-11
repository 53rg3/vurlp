package madog.core;

public abstract class Output {

    public static boolean shouldSetCurrentPath = true;

    public Output() {
        if (shouldSetCurrentPath) {
            Print.setCurrentPage(this.createOutputPathFromClassPath());
        }
    }

    public abstract void addMarkDownAsCode();

    public String createOutputPathFromClassPath() {
        final String classPath = this.getClass().getName();
        if (!classPath.startsWith("output")) {
            throw new IllegalArgumentException("All Output classes must reside in /output/ folder, found class outside. Check: " + classPath);
        }

        return this.transformClassPathToOutputPath(classPath);
    }

    private String transformClassPathToOutputPath(final String classPath) {
        String outputPath = replaceLast(classPath, "\\." + this.getClass().getSimpleName(), "/" + Config.MARKDOWN_OUTPUT_FILE_NAME);
        outputPath = Config.PACKAGE_OUTPUT_PATTERN.matcher(outputPath).replaceFirst("");
        outputPath = Config.CLASS_SORTING_PATTERN.matcher(outputPath).replaceAll("/");
        outputPath = Config.SUBPAGE_PATTERN.matcher(outputPath).replaceAll("/");
        if(outputPath.startsWith("/")) {
            outputPath = "." + outputPath;
        } else {
            outputPath = "./" + outputPath;
        }

        return outputPath;
    }

    private static String replaceLast(final String text, final String regex, final String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

}
