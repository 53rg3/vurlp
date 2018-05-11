package madog.core;

import java.util.regex.Pattern;

public class Config {

    // User config parameter
    public static final String DEFAULT_HIGHLIGHTING = "JAVA";
    public static final String MADOG_FOLDER_NAME = "/madog"; // Should be "/madog" if used as doc tool or sub-project, otherwise ""
    public static final boolean USE_AS_DOC_TOOL = true; // Set to true if Madog is a module inside a project. This will prepend MADOG_FOLDER_NAME to outputs.
    public static final boolean ALLOW_DUPLICATE_FILE_NAMES = true; // This will enable possibility for duplicate file names. Makes linking in rare cases slightly harder.
    public static final String MARKDOWN_OUTPUT_FILE_NAME = "readme.md";


    // Internal config
    public static final Pattern CLASS_SORTING_PATTERN = Pattern.compile("\\.?[a-zA-Z]\\d?\\w?\\d?_");
    public static final Pattern PACKAGE_OUTPUT_PATTERN = Pattern.compile("^output(\\.|/)");
    public static final Pattern SUBPAGE_PATTERN = Pattern.compile("\\.(?!md$)");
    public static final String INTERNAL_LAST_COMMIT_FILE_LOCATION = "./_resources/.last_commit";

}
