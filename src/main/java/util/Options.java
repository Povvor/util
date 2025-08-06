package util;

import java.nio.file.StandardOpenOption;

import java.util.List;

public record Options(
        boolean isShortStat,
        boolean isFullStat,
        List<String> files,
        StandardOpenOption toRewrite,
        String prefix,
        String path
) {
    public Options() {
        this(false, false, List.of(), StandardOpenOption.TRUNCATE_EXISTING, "", "");
    }

    public Options(String path) {
        this(false, false, List.of(), StandardOpenOption.TRUNCATE_EXISTING, "", path);
    }
}
