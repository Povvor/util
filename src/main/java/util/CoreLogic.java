package util;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CoreLogic {
    private @Getter final List<String> files = new ArrayList<>();
    private @Getter final List<String> integerOutput = new ArrayList<>();
    private @Getter final List<String> floatOutput = new ArrayList<>();
    private @Getter final List<String> stringOutput = new ArrayList<>();
    private @Getter int stringCount, intCount, floatCount;
    private @Getter boolean isShortStat = false;
    private @Getter boolean isFullStat = false;
    private @Getter StandardOpenOption toRewrite = StandardOpenOption.TRUNCATE_EXISTING;
    private @Getter String prefix = "";
    private @Getter @Setter String path = "";
    private final @Getter Statistics statistics = new Statistics();
    private final PrintUtils printUtils = new PrintUtils(statistics);
    private @Getter BufferedWriter stringWriter, floatWriter, integerWriter;

    public void processFiles() {
        for (String fileName : files) {
            Stream<String> strings = readFile(fileName);
            initWriters();
            strings.forEach(this::processString);
            }
        statistics.printStatistics(isShortStat, isFullStat, printUtils, stringCount, floatCount, intCount);
        closeWriters();
    }

    public Type defineStringType(String string) {
        try {
            BigDecimal number = new BigDecimal(string);
            return number.stripTrailingZeros().scale() <= 0 ? Type.INTEGER : Type.FLOAT;
        } catch (NumberFormatException e) {
            return Type.STRING;
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
        try {
            writer.write(string);
            writer.newLine();
        } catch (IOException e) {
            printUtils.printFileOpenError(e, writer.toString());
        }
    }

    public void initWriters() {
        try {
            stringWriter = Files.newBufferedWriter(Paths.get(path + '/' + prefix + "strings.txt"), StandardOpenOption.CREATE, toRewrite);
        } catch (IOException e) {
            printUtils.printFileOpenError(e, path + "/" + prefix + "strings.txt");
        }
        try {
            floatWriter = Files.newBufferedWriter(Paths.get(path + '/' + prefix + "floats.txt"), StandardOpenOption.CREATE, toRewrite);
        } catch (IOException e) {
            printUtils.printFileOpenError(e, path + "/" + prefix + "floats.txt");
        }
        try {
            integerWriter = Files.newBufferedWriter(Paths.get(path + '/' + prefix + "integers.txt"), StandardOpenOption.CREATE, toRewrite);
        } catch (IOException e) {
            printUtils.printFileOpenError(e, path + "/" + prefix + "integers.txt");
        }
    }

    public void closeWriters() {
        try {
            stringWriter.close();
            floatWriter.close();
            integerWriter.close();
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии потока!");
        }

    }

    public Stream<String> readFile(String fileName) {
        try {
            return Files.lines(Paths.get(fileName));
        } catch (IOException e) {
            printUtils.printFileOpenError(e, fileName);
            return Stream.empty(); // безопасный fallback
        }
    }
}
