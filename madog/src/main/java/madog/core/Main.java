package madog.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    public static void main(final String[] args) {
        try (Stream<Path> paths = Files.walk(Paths.get(FileLocator.getMadogFolder()))) {
            paths
                    .filter(path -> path.toString().endsWith(".java"))
                    .sorted()
                    .map(Main::transformFilePathToClassPath)
                    .map(Main::createOutputClass)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Main::createOutputFromClassPath)
                    .forEach(Output::addMarkDownAsCode);

            Print.accessPrinter().printMarkdownFiles();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static Optional<Class<Output>> createOutputClass(final String classPath) {
        try {
            final Class<?> clazz = Class.forName(classPath);
            if(clazz.getSuperclass().equals(Output.class)) {
                return Optional.of((Class<Output>) clazz);
            }
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
}

    private static Output createOutputFromClassPath(final Class<Output> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (final InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Couldn't create: " + clazz.getName());
    }

    private static String transformFilePathToClassPath(final Path path) {
        return path.toString()
                .replace(".java", "")
                .replace(FileLocator.getMadogFolder(), "")
                .replace("/", ".");
    }


}
