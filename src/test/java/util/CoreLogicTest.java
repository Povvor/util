package util;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CoreLogicTest {

    @Test
    void defineStringTypeTest() {
        CoreLogic coreLogic = new CoreLogic(newOption());
        Type resultString = coreLogic.defineStringType("Hello World!");
        Type resultInteger = coreLogic.defineStringType("42");
        Type resultFloat = coreLogic.defineStringType("3.14");
        assertThat(resultString).isEqualTo(Type.STRING);
        assertThat(resultInteger).isEqualTo(Type.INTEGER);
        assertThat(resultFloat).isEqualTo(Type.FLOAT);

    }

    @Test
    void processStringTest() throws IOException {
        CoreLogic coreLogic = new CoreLogic(new Options());
        String[] input = {"hello", "world", "!!!", "1", "11", "21.2"};
        coreLogic.initPaths();
        coreLogic.initWriters();
        for (String string : input) {
            coreLogic.processString(string);
        }
        Files.deleteIfExists(Paths.get("src/test/java/resources/writeTest/floats.txt"));
        Files.deleteIfExists(Paths.get("src/test/java/resources/writeTest/integers.txt"));
        Files.deleteIfExists(Paths.get("src/test/java/resources/writeTest/strings.txt"));
        assertThat(coreLogic.getStringCount()).isEqualTo(3);
        assertThat(coreLogic.getIntCount()).isEqualTo(2);
        assertThat(coreLogic.getFloatCount()).isEqualTo(1);
    }

    @Test
    void writeTest() throws IOException {
        String fileName = "writeTest.txt";
        String filePath = "src/test/java/resources/writeTest";
        Path path = Paths.get(filePath + '/' + fileName);
        Options options = new Options(path.toString());
        CoreLogic coreLogic = new CoreLogic(options);
        String[] strings = {"hello", "world", "!!!", "1", "11", "21.2" };
        Stream<String> stream = Stream.of(strings);
        BufferedWriter writer;
        writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        stream.forEach(string -> coreLogic.write(writer, string));
        writer.close();
        List<String> result = Files.readAllLines(path);
        assertThat(result.size()).isEqualTo(strings.length);
        assertThat(result).isEqualTo(List.of(strings));
        Files.deleteIfExists(path);
    }

    @Test
    void writeTestWhenReadOnly() throws IOException {
        CoreLogic coreLogic = new CoreLogic(new Options());
        Path path = Paths.get("src/test/java/resources/readOnly.txt");
        String[] strings = {"hello", "world", "!!!", "1", "11", "21.2" };
        Stream<String> stream = Stream.of(strings);
        BufferedWriter writer;
        writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        stream.forEach(string -> coreLogic.write(writer, string));
        List<String>  result = Files.readAllLines(path);
        writer.close();
        assertThat(result).size().isEqualTo(0);
    }

    @Test
    void closeWritersTest() throws IOException {
        String filePath = "src/test/java/resources/writeTest";
        CoreLogic coreLogic = new CoreLogic(new Options(filePath));
        String testString = "hello world";
        coreLogic.initPaths();
        coreLogic.initWriters();
        coreLogic.write(coreLogic.getFloatWriter(), testString);
        coreLogic.write(coreLogic.getIntegerWriter(), testString);
        coreLogic.write(coreLogic.getStringWriter(), testString);
        coreLogic.closeWriters();
        coreLogic.write(coreLogic.getFloatWriter(), testString);
        coreLogic.write(coreLogic.getIntegerWriter(), testString);
        coreLogic.write(coreLogic.getStringWriter(), testString);
        assertThat(Files.readAllLines(coreLogic.getFloatPath())).size().isEqualTo(1);
        assertThat(Files.readAllLines(coreLogic.getIntegerPath())).size().isEqualTo(1);
        assertThat(Files.readAllLines(coreLogic.getStringPath())).size().isEqualTo(1);
        Files.deleteIfExists(Paths.get("src/test/java/resources/writeTest/floats.txt"));
        Files.deleteIfExists(Paths.get("src/test/java/resources/writeTest/integers.txt"));
        Files.deleteIfExists(Paths.get("src/test/java/resources/writeTest/strings.txt"));
    }

    @Test
    void initWritersTestWhenPathNotExist() throws IOException {
        String filePath = "src/test/java/resources/writeTest/absentPath";
        Path path = Paths.get(filePath);
        CoreLogic coreLogic = new CoreLogic(new Options(filePath));
        String testString = "hello world";
        coreLogic.initPaths();
        coreLogic.initWriters();
        coreLogic.write(coreLogic.getFloatWriter(), testString);
        coreLogic.write(coreLogic.getIntegerWriter(), testString);
        coreLogic.write(coreLogic.getStringWriter(), testString);
        try (Stream<Path> stream = Files.walk(path)) {
            assertThat(stream.count()).isEqualTo(4);
        }
        Files.deleteIfExists(coreLogic.getIntegerPath());
        Files.deleteIfExists(coreLogic.getFloatPath());
        Files.deleteIfExists(coreLogic.getStringPath());
        Files.delete(path);
    }

    @Test
    void readFileTest() {
        CoreLogic coreLogic = new CoreLogic(new Options());
        String fileName = "src/test/java/resources/test.txt";
        List<String> result = coreLogic.readFile(fileName).toList();
        assertThat(result).size().isEqualTo(1);
        assertThat(result.get(0)).isEqualTo("text");
    }

    @Test
    void readFileTestWhenWrongPath() {
        CoreLogic coreLogic = new CoreLogic(new Options());
        String fileName = "src/test/java/resources/fake.txt";
        List<String> result = coreLogic.readFile(fileName).toList();
        assertThat(result).isEmpty();
    }

    @Test
    void deleteEmptyFilesTest() throws IOException {
        String filePath = "src/test/java/resources/writeTest";
        CoreLogic coreLogic = new CoreLogic(new Options(filePath));
        coreLogic.initPaths();
        System.out.println(coreLogic.getFloatPath());
        coreLogic.initWriters();
        try (Stream<Path> stream = Files.list((Paths.get(filePath)))) {
            assertThat(stream.findAny()).isNotEmpty();
        }
        coreLogic.deleteEmptyFiles();
        try (Stream<Path> stream = Files.list((Paths.get(filePath)))) {
            assertThat(stream.findAny()).isEmpty();
        }
    }

    private Options newOption() {
        return new Options(false, false, new ArrayList<>(), StandardOpenOption.TRUNCATE_EXISTING, "prefix", "path");

    }
}