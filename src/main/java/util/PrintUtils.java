package util;

public final class PrintUtils {

    private PrintUtils() { }

    public static void printFullStatistics(Statistics statistics) {
        if  (statistics.getMinInt() != null) {
            System.out.println("Полная статистика для целых чисел:");
            printStatisticsForEachType(StringType.INTEGER, statistics);
        } else {
            System.out.println("Целых чисел не обнаружено!");
        }

        if  (statistics.getMinFloat() != null) {
            System.out.println("Полная статистика для действительных чисел:");
            printStatisticsForEachType(StringType.FLOAT, statistics);
        }  else {
            System.out.println("Действительных чисел не обнаружено!");
        }
        if  (statistics.getShortestString() != null) {
            System.out.println("Полная статистика для строк:");
            printStatisticsForEachType(StringType.STRING, statistics);
        } else {
            System.out.println("Строк не обнаружено!");
        }
    }

    public static void printShortStatistics(int stringCount, int integerCount, int floatCount) {
        System.out.println("Краткая статистика:");
        System.out.println("Количество записанных целых чисел: " + integerCount);
        System.out.println("Количество записанных действительных чисел: " + floatCount);
        System.out.println("Количество записанных строк:  " + stringCount);
        System.out.println();
    }

    public static void printHelp() {
        System.out.println("Опции:");
        System.out.println("  -p        Добавляет префикс к созданным файлам.");
        System.out.println("  -o        Задать путь для результатов");
        System.out.println("  -s        Вывод краткой статистики");
        System.out.println("  -f        Вывод полной статистики");
        System.out.println("  -a        Добавление строк в конец файлов (по умолчанию перезапись)");
    }

    public static void printFileOpenError(Exception e, String fileName) {
        System.out.println("Возникла проблема при обработке файла: " + fileName);
        System.out.println(e.getClass().getSimpleName());
        System.out.println("Данный файл проигнорирован\n");
    }

    public static void printNoArgsMsg() {
        System.out.println("Не введено ни одного аргумента");
        System.out.println("Введите -h или --help что бы получить помощь.");
    }

    public static void printNoFilesMsg() {
        System.out.println("Не обнаружено файлов доступных для обработки");
    }

    public static void printFileDeleteErrorMsg(String type) {
        System.out.println("Ошибка при попытке очистки файла: " + type);
    }

    private static void printStatisticsForEachType(StringType type, Statistics statistics) {
        switch (type) {
            case INTEGER:
                System.out.println("Минимальное число: " + statistics.getMinInt());
                System.out.println("Максимальное число: " + statistics.getMaxInt());
                System.out.println("Сумма: " + statistics.getSumInt());
                System.out.println("Среднее значение: " + statistics.getIntAvg());
                System.out.println();
                break;
            case FLOAT:
                System.out.println("Минимальное число: " + statistics.getMinFloat());
                System.out.println("Максимальное число: " + statistics.getMaxFloat());
                System.out.println("Сумма: " + statistics.getSumFloat());
                System.out.println("Среднее значение: " + statistics.getFloatAvg());
                System.out.println();
                break;
            case STRING:
                System.out.println("Самая короткая строка: " + statistics.getShortestString() + " символов.");
                System.out.println("Самая длинная строка: " + statistics.getLongestString() + " символов.");
                System.out.println("Обнаружено пустых строк: " + statistics.getEmptyStringCount());
                System.out.println();
                break;
            default:
                System.out.println("Неизвестный тип данных");
        }
    }
}
