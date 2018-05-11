package madog.core;

import madog.core.Page.PageType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class Printer {

    private String currentPage = "index";
    private final Map<String,Page> outputMap = new LinkedHashMap<>();
    private final List<String> lastCommitProtocol = new ArrayList<>();
    private boolean shouldDisplayCompleteTableOfContentOfAllPages = false;
    private boolean displayCompleteNotebookOnThisPage = false;

    public void printMarkdownFiles() {
        this.deleteLastCommit();
        this.writeFile(Config.INTERNAL_LAST_COMMIT_FILE_LOCATION, lastCommitProtocol);
        this.outputMap.forEach(this::printPage);
    }

    private void printPage(final String pathToFile, final Page page) {
        final String parentDir = new File(pathToFile).getParent();
        if(!new File(parentDir).exists()) {
            this.createDirectory(parentDir);
        }

        if(page.hasContent()) {
            this.writeFile(pathToFile, page.getPageAsList());
        }
    }

    private void createDirectory(final String dir) {
        try {
            Files.createDirectories(Paths.get(FileLocator.getFilePathWithMadogFolder(dir, Config.USE_AS_DOC_TOOL)));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(final String pathToFile, final Iterable<String> iterable) {
        try {
            Files.write(Paths.get(FileLocator.getFilePathWithMadogFolder(pathToFile, Config.USE_AS_DOC_TOOL)), iterable);
            System.out.println(FileLocator.getFilePathWithMadogFolder(pathToFile, Config.USE_AS_DOC_TOOL) + " created.");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLastCommit() {
        this.deleteReadMeFiles();
        this.deleteOutputFolders();
    }

    private void deleteReadMeFiles() {
        this.getLastCommitAsListFromFile()
                .stream()
                .map(File::new)
                .forEach(File::delete);
    }

    private void deleteOutputFolders() {
        this.getLastCommitAsListFromFile()
                .stream()
                .map(File::new)
                .map(File::getParent)
                .filter(parent -> !parent.equals("."))
                .sorted(Comparator.reverseOrder())
                .map(File::new)
                .forEach(File::delete);
    }

    private List<String> getLastCommitAsListFromFile() {
        try {
            return Files.readAllLines(new File(FileLocator.getFilePathWithMadogFolder(Config.INTERNAL_LAST_COMMIT_FILE_LOCATION, Config.USE_AS_DOC_TOOL)).toPath());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Can't read last_commit file in: "+Config.INTERNAL_LAST_COMMIT_FILE_LOCATION);
    }


    public void appendToRespectiveTableOfContents(final String markdown, final Depth depth) {
        this.getDedicatedPage().appendToTableOfContents(markdown, depth);
    }
    public void appendToRespectiveTextSection(final String markdown) {
        this.getDedicatedPage().appendToTextSection(markdown);
    }
    private Page getDedicatedPage() {
        if(this.outputMap.containsKey(this.currentPage)) {
            return this.outputMap.get(this.currentPage);
        } else {
            final Page page = this.createPage();
            this.outputMap.put(this.currentPage, page);
            this.lastCommitProtocol.add(this.prependMadogFolder(this.currentPage));
            return page;
        }
    }

    private String prependMadogFolder(String currentPage) {
        if(Config.USE_AS_DOC_TOOL) {
            currentPage = currentPage.replaceAll("^\\.", "."+Config.MADOG_FOLDER_NAME);
        }
        return currentPage;
    }

    public enum Depth {
        ONE,
        TWO,
        THREE
    }

    public Page createPage() {
        final Page page;
        if(this.shouldDisplayCompleteTableOfContentOfAllPages) {
            page = new Page(PageType.COMPLETE_TOC, this.outputMap);
            this.shouldDisplayCompleteTableOfContentOfAllPages = false;
        } else if(this.displayCompleteNotebookOnThisPage) {
            page = new Page(PageType.COMPLETE_NOTEBOOK, this.outputMap);
            this.displayCompleteNotebookOnThisPage = false;
        } else {
            page = new Page(PageType.STANDARD, null);
        }
        return page;
    }

    public void setCurrentPage(final String currentPage) {
        this.currentPage = currentPage;
    }

    public void displayCompleteTableOfContentOfAllPagesOnThisPage(final boolean shouldDisplayCompleteTableOfContentOfAllPages) {
        this.shouldDisplayCompleteTableOfContentOfAllPages = shouldDisplayCompleteTableOfContentOfAllPages;
    }

    public void displayCompleteNotebookOnThisPage(final boolean displayCompleteNotebookOnThisPage) {
        this.displayCompleteNotebookOnThisPage = displayCompleteNotebookOnThisPage;
    }

}
