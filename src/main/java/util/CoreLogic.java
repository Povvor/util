package util;

import lombok.Getter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class CoreLogic {
    private @Getter int stringCount, intCount, floatCount;
    private final @Getter Statistics statistics = new Statistics();
    private @Getter BufferedWriter stringWriter, floatWriter, integerWriter;
    private @Getter Path stringPath, floatPath, integerPath;
    private final Options options;

    public CoreLogic(Options options) {
        this.options = options;
    }

    public void run() {
        initPaths();
        if (options.files().isEmpty()) {
            PrintUtils.printNoFilesMsg();
            return;
        }
        initWriters();
        for (String fileName : options.files()) {
            Stream<String> strings = readFile(fileName);
            strings.forEach(this::processString);
        }
        statistics.printStatistics(options.isShortStat(), options.isFullStat(), stringCount, floatCount, intCount);
        closeWriters();
        deleteEmptyFiles();
    }

    public StringType defineStringType(String string) {
        try {
            new BigInteger(string);
            return StringType.INTEGER;
        } catch (NumberFormatException e) {
            try {
                new BigDecimal(string);
                return StringType.FLOAT;
            } catch (NumberFormatException e2) {
                return StringType.STRING;
            }
        }
    }

    public void processString(String inputString) {
        StringType type = defineStringType(inputString);
        statistics.processStringForStatistics(inputString, options.isFullStat(), type);
        switch (type) {
            case STRING:
                if (inputString.isEmpty()) {
                    return;
                }
                write(stringWriter, inputString);
                stringCount++;
                break;
            case FLOAT:
                write(floatWriter, inputString);
                floatCount++;
                break;
            case INTEGER:
                write(integerWriter, inputString);
                intCount++;
                break;
            default:
                System.out.println("Неизвестный тип данных");
        }
    }

    public void write(BufferedWriter writer, String string) {
        if (writer == null) {
            return;
        }
        try {
            writer.write(string);
            writer.newLine();
        } catch (IOException e) {
            PrintUtils.printFileOpenError(e, writer.toString());
        }
    }

    public void initWriters() {
        try {
            Files.createDirectories(Paths.get(options.path()));
            stringWriter = Files.newBufferedWriter(stringPath, StandardOpenOption.CREATE, options.toRewrite());
            integerWriter = Files.newBufferedWriter(integerPath, StandardOpenOption.CREATE, options.toRewrite());
            floatWriter = Files.newBufferedWriter(floatPath, StandardOpenOption.CREATE, options.toRewrite());
        } catch (IOException e) {
            System.out.println("Задан недопустимый путь!");
        }
    }

    public void closeWriters() {
        try {
            if (stringWriter != null) {
                stringWriter.close();
            }
            if (integerWriter != null) {
                integerWriter.close();
            }
            if (floatWriter != null) {
                floatWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии потока!");
        }

    }

    public Stream<String> readFile(String fileName) {
        try {
            return Files.lines(Paths.get(fileName));
        } catch (IOException e) {
            PrintUtils.printFileOpenError(e, fileName);
            return Stream.empty();
        }
    }

    public void initPaths() {
        stringPath = Paths.get(options.path() + options.prefix() + "strings.txt");
        floatPath = Paths.get(options.path() + options.prefix() + "floats.txt");
        integerPath = Paths.get(options.path() + options.prefix() + "integers.txt");
    }

    public void deleteEmptyFiles() {
        if (stringCount == 0) {
            try {
                Files.deleteIfExists(stringPath);
            } catch (IOException e) {
                PrintUtils.printFileDeleteErrorMsg("Strings");
            }
        }
        if (floatCount == 0) {
            try {
                Files.deleteIfExists(floatPath);
            } catch (IOException e) {
                PrintUtils.printFileDeleteErrorMsg("Floats");
            }
        }
        if (intCount == 0) {
            try {
                Files.deleteIfExists(integerPath);
            } catch (IOException e) {
                PrintUtils.printFileDeleteErrorMsg("Integers");
            }
        }
    }
}

