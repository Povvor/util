package util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CoreLogicTest {

    @Test
    void defineStringTypeTest()  {
        CoreLogic coreLogic = new CoreLogic();
        Type resultString = coreLogic.defineStringType("Hello World!");
        Type resultInteger = coreLogic.defineStringType("42");
        Type resultFloat = coreLogic.defineStringType("3.14");
        assertThat(resultString).isEqualTo(Type.STRING);
        assertThat(resultInteger).isEqualTo(Type.INTEGER);
        assertThat(resultFloat).isEqualTo(Type.FLOAT);
    }

    @Test
    void initInputStringsWhenWrongInput() {
        CoreLogic coreLogic = new CoreLogic();
        String image = "src/test/java/resources/testImg.png";
        String wrongPath = "src/test/java/test.txt";
        String suitableFile = "src/test/java/resources/test.txt";
        coreLogic.getFiles().add(image);
        coreLogic.getFiles().add(wrongPath);
        coreLogic.getFiles().add(suitableFile);
        List<String> result = coreLogic.initInputStrings();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo("text");
    }

    @Test
    void processStringTest()  {
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
        Path path = Paths.get("src/test/java/resources/writeTest.txt");
        List<String> input = Arrays.asList("hello", "world", "!!!");
        coreLogic.write(input, path.toString());
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
}