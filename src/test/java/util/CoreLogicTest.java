package util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

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
    void processStringTest() {
        CoreLogic coreLogic = new CoreLogic();
        String[] input = {"hello", "world", "!!!", "1", "11", "21.2"};
        for (String string : input) {
            coreLogic.processString(string);
        }
        assertThat(coreLogic.getStringOutput().size()).isEqualTo(3);
        assertThat(coreLogic.getIntegerOutput().size()).isEqualTo(2);
        assertThat(coreLogic.getFloatOutput().size()).isEqualTo(1);
    }

    @Test
    void writeTest() {
        CoreLogic coreLogic = new CoreLogic();
        String fileName = "writeTest.txt";
        String filePath = "src/test/java/resources/writeTest";
        Path path = Paths.get(filePath + '/' + fileName);
        coreLogic.setPath(filePath);
        List<String> input = Arrays.asList("hello", "world", "!!!");
        coreLogic.write(input, fileName);
        List<String> result;

        try {
            result = Files.readAllLines(path);
            assertThat(result.size()).isEqualTo(input.size());
            assertThat(result).isEqualTo(input);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeTestWhenReadOnly() {
        CoreLogic coreLogic = new CoreLogic();
        List<String> input = Arrays.asList("hello", "world", "!!!");
        coreLogic.write(input, "src/test/java/readOnly.txt");
        List<String> result;
        try {
            result = Files.readAllLines(Paths.get("src/test/java/resources/readOnly.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    void writeAllAndShowStatisticsWhenStringsNotEmpty() {
        CoreLogic coreLogic = new CoreLogic();
        Path path = Paths.get("src/test/java/resources/writeTest");
        coreLogic.setPath(path.toString());
        String string = "string";
        String integer = "42";
        String floatingPoint = "3.14";
        coreLogic.getStringOutput().add(string);
        coreLogic.getIntegerOutput().add(integer);
        coreLogic.getFloatOutput().add(floatingPoint);
        coreLogic.writeAllAndShowStatistics();
        Path strings = Paths.get(path + "/strings.txt");
        Path integers = Paths.get(path + "/integers.txt");
        Path floats = Paths.get(path + "/floats.txt");
        System.out.printf(path.toString());
        try {
            assertThat(Files.readAllLines(strings).get(0)).isEqualTo(string);
            assertThat(Files.readAllLines(integers).get(0)).isEqualTo(integer);
            assertThat(Files.readAllLines(floats).get(0)).isEqualTo(floatingPoint);
            Files.deleteIfExists(strings);
            Files.deleteIfExists(integers);
            Files.deleteIfExists(floats);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeAllAndShowStatisticsWhenStringsAreEmpty() {
        CoreLogic coreLogic = new CoreLogic();
        Path path = Paths.get("src/test/java/resources/writeTest");
        coreLogic.setPath(path.toString());
        coreLogic.writeAllAndShowStatistics();
        Path strings = Paths.get(path + "strings.txt");
        Path integers = Paths.get(path + "integers.txt");
        Path floats = Paths.get(path + "floats.txt");
        assertThatThrownBy(() -> Files.readAllLines(strings))
                .isInstanceOf(IOException.class);
        assertThatThrownBy(() -> Files.readAllLines(integers))
                .isInstanceOf(IOException.class);
        assertThatThrownBy(() -> Files.readAllLines(floats))
                .isInstanceOf(IOException.class);
    }
}