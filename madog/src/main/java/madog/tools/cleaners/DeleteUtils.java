package madog.tools.cleaners;

import madog.core.Printer;

import java.io.File;
import java.io.IOException;

public class DeleteUtils {

    public static void deleteFile(final String path) {
        final File file = new File(path);
        if(!file.delete()) {
            System.out.println("Couldn't delete "+path);
        }
    }

    public static void deleteFolder(final String path) {
        try {
            deleteRecursively(new File(path));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGitFolder() {
        final File folder = new File("./.git");
        if(!folder.delete()) {
            System.out.println("Couldn't delete .git folder");
        }
    }

    private static void deleteRecursively(final File file) throws IOException {

        if(!file.exists()) {
            return;
        }

        for (final File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                deleteRecursively(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException();
                }
            }
        }

        if (!file.delete()) {
            throw new IOException();
        }
    }

    public static void deleteLastCommit() {
        new Printer().deleteLastCommit();
    }
}
