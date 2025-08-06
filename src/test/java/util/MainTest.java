package util;

import org.junit.jupiter.api.Test;

import java.nio.file.StandardOpenOption;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    @Test
    void parseArgsTest() {
        String prefix = "prefix";
        String path = "path";
        String filename = "file.txt";
        String[] args = {"-s", "-f", "-a", "-p", prefix, "-o", path, filename};
        Options result = Main.parseArgs(args);
        Main.parseArgs(args);
        assertThat(result.isShortStat()).isTrue();
        assertThat(result.isFullStat()).isTrue();
        assertThat(result.toRewrite()).isEqualTo(StandardOpenOption.APPEND);
        assertThat(result.prefix()).isEqualTo(prefix);
        assertThat(result.path()).isEqualTo(path + '/');
        assertThat(result.files().get(0)).isEqualTo(filename);
    }

    @Test
    void findValueAfterArgTest() {
        String[] input = {"-p", "prefix"};
        String result = Main.findValueAfterArg(input, 0);
        String expected = input[1];
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findValueAfterArgTestWhenError() {
        String[] input = {"-s", "prefix", "-p"};
        String result = Main.findValueAfterArg(input, 2);
        assertThat(result).isEmpty();

    }

}
