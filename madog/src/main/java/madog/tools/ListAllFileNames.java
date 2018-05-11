package madog.tools;

import madog.core.FileLocator;
import madog.core.FileLocator.FilePojo;
import madog.core.FileLocator.FileType;

import java.util.Map.Entry;

public class ListAllFileNames {

    private static final FileLocator fileLocator = new FileLocator();

    public static void main(final String[] args) {
        fileLocator.getFileMap().entrySet()
                .stream()
                .filter(ListAllFileNames::shouldShow)
                .sorted(Entry.comparingByKey())
                .forEach(entry -> System.out.println("" +
                        "Key: " + entry.getKey() + " " +
                        "- FileType: " + entry.getValue().getFileType() + " " +
                        "- Path: "+entry.getValue().getPath()));
    }

    private static boolean shouldShow(Entry<String,FilePojo> entry) {

        if(entry.getValue().getFileType().equals(FileType.NOT_SUITABLE_FOR_REFERENCE)) {
           return false;
        }

        if(entry.getKey().contains(".git")) {
            return false;
        }

        return true;
    }

}
