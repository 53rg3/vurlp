package madog.tools.cleaners;

import java.io.File;
import java.io.IOException;

/**
 * Deletes output folder, markdown files, Madog tests and example resources
 */
public class ProjectPurger {

    public static void purge() {
        DeleteUtils.deleteFile("./_resources/documents/example.pdf");
        DeleteUtils.deleteFile("./_resources/images/example.jpg");
        DeleteUtils.deleteLastCommit();
        DeleteUtils.deleteFolder("./src/test/java/madog");
        DeleteUtils.deleteFolder("./src/main/java/output");
    }

}
