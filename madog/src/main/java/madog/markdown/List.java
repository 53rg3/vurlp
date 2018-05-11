package madog.markdown;


import java.util.ArrayList;
import java.util.stream.Stream;

public class List {

    private final java.util.List<String> rowList = new ArrayList<>();
    private boolean isNumberedList = false;
    private int currentNumber = 0;

    public void entry(final String title, final String body) {
        this.rowList.add(""+
                this.nextNumber()+" **"+title+"**<br>\n" +
                body+"\n");
    }

    public void entry(final String body) {
        this.rowList.add(this.nextNumber()+" " + body+"\n");
    }

    public String getAsMarkdown() {
        final StringBuilder stringBuilder = new StringBuilder();
        this.rowList.forEach(stringBuilder::append);

        this.rowList.clear();
        this.isNumberedList = false;
        this.currentNumber = 1;

        return stringBuilder.toString();
    }

    private String nextNumber() {
        if(this.isNumberedList) {
            return ++currentNumber + ".";
        } else {
            return "*";
        }
    }

    public void isNumberedList(final boolean shouldBeNumbered) {
        this.isNumberedList = shouldBeNumbered;
    }
}
