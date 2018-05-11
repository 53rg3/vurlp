package madog.core;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Char {
    public static String get(final int amount, final String character) {
        return IntStream.range(0, amount)
                .mapToObj(i -> character)
                .collect(Collectors.joining(""));
    }

    public static String whitespace() {
        return " ";
    }

    public static String wrapWithNewlines(final String text) {
        return "\n"+text+"\n";
    }
    public static String wrapWithCodeBlock(final String text, final String highlighting) {
        return "\n```"+highlighting+"\n"
                +text+
                "\n```\n";
    }
}
