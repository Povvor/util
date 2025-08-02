package util;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<String> FILES = new ArrayList<>();
    private static final List<String> INPUT_STRINGS = new ArrayList<>();
    private static boolean isShortStat = false;
    private static boolean isFullStat = false;
    private static StandardOpenOption toRewrite = StandardOpenOption.TRUNCATE_EXISTING;
    private static String prefix = "";
    private static String path = "";

    public static void main(String[] args) {
        parseArgs(args);
        initInputList();
        if (INPUT_STRINGS.isEmpty()) {
            return;
        }


        List<String> floats = new ArrayList<>();
        List<String> ints = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        Statistics statistics = new Statistics();
        for(String string : INPUT_STRINGS) {
            Type type = defineStringType(string);
            statistics.processStringForStatistics(string, isFullStat, type);
            switch (type) {
                case STRING:
                    strings.add(string);
                    break;
                case INTEGER:
                    ints.add(string);
                    break;
                case  FLOAT:
                    floats.add(string);
                    break;
            }
        }

        write(strings, "strings.txt");
        write(floats, "floats.txt");
        write(ints, "integers.txt");

        if (isShortStat || isFullStat) {
            statistics.printShortStatistics(strings, floats, ints);
        }
        if (isFullStat) {
            statistics.getFullStatistics(floats.size(), ints.size());
        }
    }

    private static Type defineStringType(String string) {
        try {
            BigDecimal number = new BigDecimal(string);
            return number.stripTrailingZeros().scale() <= 0 ? Type.INTEGER : Type.FLOAT;
        } catch (NumberFormatException e) {
            return Type.STRING;
        }
    }

    private static String findValueAfterArg(String[] args, String arg) {
        for (int i = 0; i < args.length; i++) {
            try {
                if (args[i].equals(arg)) {
                    return args[i + 1];
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Ошибка при обработке аргумента после опции:" + arg);
            }
        }
        return "";
    }

    private static void parseArgs(String[] args) {
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
                    prefix = findValueAfterArg(args, "-p");
                    i++;
                    break;
                case "-o":
                    path = findValueAfterArg(args, "-o");
                    i++;
                    break;
                case "-h", "--help":
                    printHelp();
                    break;
                default:
                    FILES.add(args[i]);
                    break;
            }
        }
    }

    private static void initInputList() {
        for (String file : FILES) {
            try {
                INPUT_STRINGS.addAll(Files.readAllLines(Paths.get(file)));
            } catch (IOException e) {
                System.out.println("Возникла проблема при обработке файла: " + file);
                System.out.println("Данный файл проигнорирован\n");
            }
        }
        if (INPUT_STRINGS.isEmpty()) {
            System.out.println("Нет входных файлов доступных для обработки!\n");
        }
    }

    private static void write(List<String> outputStrings, String fileName) {
        try {
            if (!outputStrings.isEmpty()) {
                Files.write(Paths.get(path + prefix + fileName), outputStrings, StandardOpenOption.CREATE, toRewrite);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + fileName);
        }
    }

    private static void printHelp() {
        System.out.println("Опции:");
        System.out.println("  -p        Добавляет префикс к созданным файлам.");
        System.out.println("  -o        Задать путь для результатов");
        System.out.println("  -s        Вывод краткой статистики");
        System.out.println("  -f        Вывод полной статистики");
        System.out.println("  -a        Добавление файлов в конец файлов (по умолчанию перезапись)");
    }
}