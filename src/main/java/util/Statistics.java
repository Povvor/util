package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Statistics {
    public static BigDecimal minInt, maxInt, sumInt = BigDecimal.valueOf(0), avgInt;
    public static BigDecimal minDecimal, maxDecimal, sumDecimal = BigDecimal.valueOf(0), avgDecimal;
    public static int shortestString, longestString;
    public static int intCount, decCount, stringCount, emptyStringCount;
    public static boolean isFirstIntProcessed = false;
    public static boolean isFirstDecimalProcessed = false;
    public static boolean isFirstStringProcessed = false;

    public static void processInteger(String string, boolean shouldProceed) {
        if (!shouldProceed) {
            return;
        }
        BigDecimal Integer = new BigDecimal(string);
        sumInt = sumInt.add(Integer);
        intCount++;
        if (!isFirstIntProcessed) {
            minInt = Integer;
            maxInt = Integer;
            isFirstIntProcessed = true;
        } else {
            if (minInt.compareTo(Integer) > 0) {
                minInt = Integer;
            }
            if (maxInt.compareTo(Integer) < 0) {
                maxInt = Integer;
            }
        }

    }

    public static void processDecimal(String string, boolean shouldProceed) {
        if (!shouldProceed) {
            return;
        }
        BigDecimal bigDecimal = new BigDecimal(string);
        sumDecimal = sumDecimal.add(bigDecimal);
        decCount++;
        if (!isFirstDecimalProcessed) {
            minDecimal = bigDecimal;
            maxDecimal = bigDecimal;
            isFirstDecimalProcessed = true;
        } else  {
            if (minDecimal.compareTo(bigDecimal) > 0) {
                minDecimal = bigDecimal;
            }
            if (maxDecimal.compareTo(bigDecimal) < 0) {
                maxDecimal = bigDecimal;
            }
        }
    }

    public static void processString(String string, boolean shouldProceed) {
        if (!shouldProceed) {
            return;
        }
        if (string.isEmpty()) {
            emptyStringCount++;
            return;
        }
        stringCount++;
        int length = string.length();
        if (!isFirstStringProcessed) {
            shortestString = length;
            longestString = length;
            isFirstStringProcessed = true;
        }  else {
            if (shortestString > length) {
                shortestString = length;
            }
            if (longestString < length) {
                longestString = length;
            }
        }
    }

    private static void writeStatistics(Number min,  Number max, Number sum, Number avg) {
        System.out.println("Минимальное число: " + min);
        System.out.println("Максимальное число: " + max);
        System.out.println("Сумма: " + sum);
        System.out.println("Среднее значение: " + avg);
        System.out.println();
    }

    private static void writeStatistics(int shortest, int longest) {
        System.out.println("Самая короткая строка: " + shortest + " символов.");
        System.out.println("Самая длинная строка: " + longest + " символов.");
        System.out.println("Обнаружено пустых строк: " + emptyStringCount);
        System.out.println();
    }

    public static void getShortStatistics(List<String> strings, List<String> floats, List<String> ints) {
        System.out.println("Краткая статистика:");
        System.out.println("Количество записанных целых чисел: " + ints.size());
        System.out.println("Количество записанных действительных чисел: " + floats.size());
        System.out.println("Количество записанных строк:  " + strings.size());
        System.out.println();
    }

    public static void getFullStatistics() {
        if  (isFirstIntProcessed) {
            avgInt = sumInt.divide(BigDecimal.valueOf(intCount), RoundingMode.HALF_UP);
            System.out.println("Полная статистика для целых чисел:");
            writeStatistics(minInt, maxInt, sumInt, avgInt);

        } else {
            System.out.println("Целых чисел не обнаружено!");
        }

        if  (isFirstDecimalProcessed) {
        avgDecimal = sumDecimal.divide(BigDecimal.valueOf(decCount), RoundingMode.HALF_UP);
            System.out.println("Полная статистика для действительных чисел:");
            writeStatistics(minDecimal, maxDecimal, sumDecimal, avgDecimal);
        }

        if  (isFirstStringProcessed) {
            System.out.println("Полная статистика для строк:");
            writeStatistics(shortestString, longestString);
        }
    }
}
