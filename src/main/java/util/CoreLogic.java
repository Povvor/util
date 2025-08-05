package util;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CoreLogic {
    private @Getter
    final List<String> files = new ArrayList<>();
    private @Getter
    final List<String> integerOutput = new ArrayList<>();
    private @Getter
    final List<String> floatOutput = new ArrayList<>();
    private @Getter
    final List<String> stringOutput = new ArrayList<>();
    private @Getter int stringCount, intCount, floatCount;
    private @Getter boolean isShortStat = false;
    private @Getter boolean isFullStat = false;
    private @Getter StandardOpenOption toRewrite = StandardOpenOption.TRUNCATE_EXISTING;
    private @Getter String prefix = "";
    private @Getter
    @Setter String path = "";
    private final @Getter Statistics statistics = new Statistics();
    private final PrintUtils printUtils = new PrintUtils(statistics);
    private @Getter BufferedWriter stringWriter, floatWriter, integerWriter;
    private @Getter Path stringPath, floatPath, integerPath;

    public void run(String[] args) {
        parseArgs(args);
        initPaths();
        if (args.length == 0) {
            printUtils.printNoArgsMsg();
            return;
        }
        if (files.isEmpty()) {
            printUtils.printNoFilesMsg();
            return;
        }
        initWriters();
        for (String fileName : files) {
            Stream<String> strings = readFile(fileName);
            strings.forEach(this::processString);
        }
        statistics.printStatistics(isShortStat, isFullStat, printUtils, stringCount, floatCount, intCount);
        closeWriters();
        deleteEmptyFiles();
    }

    public Type defineStringType(String string) {
        try {
            new BigInteger(string);
            return Type.INTEGER;
        } catch (NumberFormatException e) {
            try {
                new BigDecimal(string);
                return Type.FLOAT;
            } catch (NumberFormatException e2) {
                return Type.STRING;
            }
        }
    }

    public String findValueAfterArg(String[] args, int index) {
        try {
            return args[index + 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка при обработке аргумента после опции: " + args[index]);
        }
        return "";
    }

    public void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    isShortStat = true;
                    break;
                case "-f":
                    isFullStat = true;
                    break;
                case "-a":
                    toRewrite = StandardOpenOption.APPEND;
                    break;
                case "-p":
                    prefix = findValueAfterArg(args, i);
                    i++;
                    break;
                case "-o":
                    path = findValueAfterArg(args, i);
                    i++;
                    break;
                case "-h", "--help":
                    printUtils.printHelp();
                    break;
                default:
                    files.add(args[i]);
                    break;
            }
        }
    }

    public void processString(String inputString) {
        Type type = defineStringType(inputString);
        statistics.processStringForStatistics(inputString, isFullStat, type);
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
            printUtils.printFileOpenError(e, writer.toString());
        }
    }

    public void initWriters() {
        try {
            Files.createDirectories(Paths.get(path));
            stringWriter = Files.newBufferedWriter(stringPath, StandardOpenOption.CREATE, toRewrite);
            integerWriter = Files.newBufferedWriter(integerPath, StandardOpenOption.CREATE, toRewrite);
            floatWriter = Files.newBufferedWriter(floatPath, StandardOpenOption.CREATE, toRewrite);
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
            printUtils.printFileOpenError(e, fileName);
            return Stream.empty();
        }
    }

    public void initPaths() {
        stringPath = Paths.get(path + '/' + prefix + "strings.txt");
        floatPath = Paths.get(path + '/' + prefix + "floats.txt");
        integerPath = Paths.get(path + '/' + prefix + "integers.txt");
    }

    public void deleteEmptyFiles() {
        if (stringCount == 0) {
            try {
                Files.deleteIfExists(stringPath);
            } catch (IOException e) {
                printUtils.printFileDeleteErrorMsg("Strings");
            }
        }
        if (floatCount == 0) {
            try {
                Files.deleteIfExists(floatPath);
            } catch (IOException e) {
                printUtils.printFileDeleteErrorMsg("Floats");
            }
        }
        if (intCount == 0) {
            try {
                Files.deleteIfExists(integerPath);
            } catch (IOException e) {
                printUtils.printFileDeleteErrorMsg("Integers");
            }
        }
    }
}

