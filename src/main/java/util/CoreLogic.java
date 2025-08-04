package util;

import lombok.Getter;
import lombok.Setter;

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
    private @Getter boolean isShortStat = false;
    private @Getter boolean isFullStat = false;
    private @Getter StandardOpenOption toRewrite = StandardOpenOption.TRUNCATE_EXISTING;
    private @Getter String prefix = "";
    private @Getter @Setter String path = "";
    private final Statistics statistics = new Statistics();
    private final PrintUtils printUtils = new PrintUtils(statistics);

    public void processFiles() {
        for (String fileName : files) {
            try (Stream<String> strings = Files.lines(Paths.get(fileName))) {
                strings.forEach(this::processString);
            } catch (IOException e) {
                printUtils.printFileOpenError(e, fileName);
            }
        }
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

    public void write(List<String> outputStrings, String fileName) {
        try {
            if (!outputStrings.isEmpty()) {
                Files.write(Paths.get(path + '/' + prefix + fileName), outputStrings, StandardOpenOption.CREATE, toRewrite);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + fileName);
        }
    }

    public void writeAllAndShowStatistics() {
        write(stringOutput, "strings.txt");
        write(floatOutput, "floats.txt");
        write(integerOutput, "integers.txt");
        statistics.printStatistics(isShortStat, isFullStat, printUtils, stringOutput, integerOutput, floatOutput);
    }

    public void processString(String inputString) {
        Type type = defineStringType(inputString);
        statistics.processStringForStatistics(inputString, isFullStat, type);
        switch (type) {
            case STRING:
                stringOutput.add(inputString);
                break;
            case INTEGER:
                integerOutput.add(inputString);
                break;
            case  FLOAT:
                floatOutput.add(inputString);
                break;
            default:
                System.out.println("Неизвестный тип данных");
        }
    }
}
