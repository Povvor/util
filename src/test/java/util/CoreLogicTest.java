package util;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CoreLogicTest {

    @Test
    void defineStringTypeTest() {
        CoreLogic coreLogic = new CoreLogic();
        Type resultString = coreLogic.defineStringType("Hello World!");
        Type resultInteger = coreLogic.defineStringType("42");
        Type resultFloat = coreLogic.defineStringType("3.14");
        assertThat(resultString).isEqualTo(Type.STRING);
        assertThat(resultInteger).isEqualTo(Type.INTEGER);
        assertThat(resultFloat).isEqualTo(Type.FLOAT);

    }

    @Test
    void parseArgsTest() {
        CoreLogic coreLogic = new CoreLogic();
        String prefix = "prefix";
        String path = "path";
        String filename = "file.txt";
        String[] args = {"-s", "-f", "-a", "-p", prefix, "-o", path, filename};
        coreLogic.parseArgs(args);
        assertThat(coreLogic.isShortStat()).isTrue();
        assertThat(coreLogic.isFullStat()).isTrue();
        assertThat(coreLogic.getToRewrite()).isEqualTo(StandardOpenOption.APPEND);
        assertThat(coreLogic.getPrefix()).isEqualTo(prefix);
        assertThat(coreLogic.getPath()).isEqualTo(path);
        assertThat(coreLogic.getFiles().get(0)).isEqualTo(filename);
    }

    @Test
    void processStringTest() throws IOException {
        CoreLogic coreLogic = new CoreLogic();
        coreLogic.setPath("src/test/java/resources/writeTest");
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
        CoreLogic coreLogic = new CoreLogic();
        String fileName = "writeTest.txt";
        String filePath = "src/test/java/resources/writeTest";
        Path path = Paths.get(filePath + '/' + fileName);
        coreLogic.setPath(filePath);
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
        CoreLogic coreLogic = new CoreLogic();
        String path = "src/test/java/readOnly.txt";
        String[] strings = {"hello", "world", "!!!", "1", "11", "21.2" };
        Stream<String> stream = Stream.of(strings);
        BufferedWriter writer;
        writer = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        stream.forEach(string -> coreLogic.write(writer, string));
        List<String>  result = Files.readAllLines(Paths.get("src/test/java/resources/readOnly.txt"));
        writer.close();
        assertThat(result).size().isEqualTo(0);
    }

    @Test
    void findValueAfterArgTest() {
        CoreLogic coreLogic = new CoreLogic();
        String[] input = {"-p", "prefix"};
        String result = coreLogic.findValueAfterArg(input, 0);
        String expected = input[1];
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findValueAfterArgTestWhenError() {
        CoreLogic coreLogic = new CoreLogic();
        String[] input = {"-s", "prefix", "-p"};
        String result = coreLogic.findValueAfterArg(input, 2);
        assertThat(result).isEmpty();

    }

    @Test
    void closeWritersTest() throws IOException {
        CoreLogic coreLogic = new CoreLogic();
        String filePath = "src/test/java/resources/writeTest";
        String testString = "hello world";
        coreLogic.setPath(filePath);
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

    }

    @Test
    void readFileTest() {
        CoreLogic coreLogic = new CoreLogic();
        String fileName = "src/test/java/resources/test.txt";
        List<String> result = coreLogic.readFile(fileName).toList();
        assertThat(result).size().isEqualTo(1);
        assertThat(result.get(0)).isEqualTo("text");
    }

    @Test
    void readFileTestWhenWrongPath() {
        CoreLogic coreLogic = new CoreLogic();
        String fileName = "src/test/java/resources/fake.txt";
        List<String> result = coreLogic.readFile(fileName).toList();
        assertThat(result).isEmpty();
    }

    @Test
    void deleteEmptyFilesTest() throws IOException {
        CoreLogic coreLogic = new CoreLogic();
        coreLogic.setPath("src/test/java/resources/writeTest");
        coreLogic.initPaths();
        coreLogic.initWriters();
        try (Stream<Path> stream = Files.list((Paths.get(coreLogic.getPath())))) {
            assertThat(stream.findAny()).isNotEmpty();
        }
        coreLogic.deleteEmptyFiles();
        try (Stream<Path> stream = Files.list((Paths.get(coreLogic.getPath())))) {
            assertThat(stream.findAny()).isEmpty();
        }
    }
}