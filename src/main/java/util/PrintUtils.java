package util;

public class PrintUtils {
    private final Statistics statistics;

    public PrintUtils(Statistics statistics) {
        this.statistics = statistics;
    }

    public void getFullStatistics() {
        if  (statistics.getMinInt() != null) {
            System.out.println("Полная статистика для целых чисел:");
            printStatisticsForEachType(Type.INTEGER);
        } else {
            System.out.println("Целых чисел не обнаружено!");
        }

        if  (statistics.getMinDecimal() != null) {
            System.out.println("Полная статистика для действительных чисел:");
            printStatisticsForEachType(Type.FLOAT);
        }  else {
            System.out.println("Действительных чисел не обнаружено!");
        }
        if  (statistics.getShortestString() != null) {
            System.out.println("Полная статистика для строк:");
            printStatisticsForEachType(Type.STRING);
        } else {
            System.out.println("Строк не обнаружено!");
        }
    }

    public void printShortStatistics(int stringCount, int integerCount, int floatCount) {
        System.out.println("Краткая статистика:");
        System.out.println("Количество записанных целых чисел: " + integerCount);
        System.out.println("Количество записанных действительных чисел: " + floatCount);
        System.out.println("Количество записанных строк:  " + stringCount);
        System.out.println();
    }

    public void printStatisticsForEachType(Type type) {
        switch (type) {
            case INTEGER:
                System.out.println("Минимальное число: " + statistics.getMinInt());
                System.out.println("Максимальное число: " + statistics.getMaxInt());
                System.out.println("Сумма: " + statistics.getSumInt());
                System.out.println("Среднее значение: " + statistics.getIntAvg());
                System.out.println();
                break;
            case FLOAT:
                System.out.println("Минимальное число: " + statistics.getMinDecimal());
                System.out.println("Максимальное число: " + statistics.getMaxDecimal());
                System.out.println("Сумма: " + statistics.getSumDecimal());
                System.out.println("Среднее значение: " + statistics.getDecimalAvg());
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

    public void printHelp() {
        System.out.println("Опции:");
        System.out.println("  -p        Добавляет префикс к созданным файлам.");
        System.out.println("  -o        Задать путь для результатов");
        System.out.println("  -s        Вывод краткой статистики");
        System.out.println("  -f        Вывод полной статистики");
        System.out.println("  -a        Добавление файлов в конец файлов (по умолчанию перезапись)");
    }

    public void printFileOpenError(Exception e, String fileName) {
        System.out.println("Возникла проблема при обработке файла: " + fileName);
        System.out.println(e.getClass().getSimpleName());
        System.out.println("Данный файл проигнорирован\n");
    }

}
