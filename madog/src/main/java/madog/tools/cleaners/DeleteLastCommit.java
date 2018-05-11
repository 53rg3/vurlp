package madog.tools.cleaners;

import madog.core.Printer;

import java.io.File;
import java.io.IOException;

/**
 * Delete example readme.md files from cloning the repo
 */
public class DeleteLastCommit {

    public static void main(final String[] args) {
        new Printer().deleteLastCommit();
    }

}
