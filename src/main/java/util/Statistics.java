package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Statistics {
    public BigDecimal minInt;
    public BigDecimal maxInt;
    public BigDecimal sumInt = BigDecimal.valueOf(0);
    public BigDecimal minDecimal;
    public BigDecimal maxDecimal;
    public BigDecimal sumDecimal = BigDecimal.valueOf(0);
    public Integer shortestString, longestString;
    public int intCount, decCount, emptyStringCount;

    public void getFullStatistics() {
        if  (minInt != null) {
            BigDecimal avgInt = sumInt.divide(BigDecimal.valueOf(intCount), RoundingMode.HALF_UP);
            System.out.println("Полная статистика для целых чисел:");
            printStatisticsForEachType(minInt, maxInt, sumInt, avgInt);

        } else {
            System.out.println("Целых чисел не обнаружено!");
        }

        if  (minDecimal != null) {
            BigDecimal avgDecimal = sumDecimal.divide(BigDecimal.valueOf(decCount), RoundingMode.HALF_UP);
            System.out.println("Полная статистика для действительных чисел:");
            printStatisticsForEachType(minDecimal, maxDecimal, sumDecimal, avgDecimal);
        }  else {
            System.out.println("Действительных чисел не обнаружено!");
        }
        if  (shortestString != null) {
            System.out.println("Полная статистика для строк:");
            printStatisticsForEachType(shortestString, longestString);
        } else {
            System.out.println("Строк не обнаружено!");
        }
    }

    public void processStringForStatistics(String string, boolean shouldProceed, Type type) {
        if (!shouldProceed) {
            return;
        }
        if (string.isEmpty()) {
            emptyStringCount++;
            return;
        }

        switch (type) {
            case FLOAT:
                BigDecimal bigDecimal = new BigDecimal(string);
                decCount++;
                sumDecimal = sumDecimal.add(bigDecimal);
                calculateMinMaxDecimal(bigDecimal);
                break;
            case INTEGER:
                BigDecimal bigInteger = new BigDecimal(string);
                intCount++;
                sumInt = sumInt.add(bigInteger);
                calculateMinMaxInteger(bigInteger);
                break;
            case  STRING:
                calculateMinMaxStringLength(string);
                break;
        }
    }

    public void printShortStatistics(List<String> strings, List<String> floats, List<String> ints) {
        System.out.println("Краткая статистика:");
        System.out.println("Количество записанных целых чисел: " + ints.size());
        System.out.println("Количество записанных действительных чисел: " + floats.size());
        System.out.println("Количество записанных строк:  " + strings.size());
        System.out.println();
    }

    public void calculateMinMaxDecimal(BigDecimal bigDecimal) {
        if (minDecimal == null || maxDecimal == null) {
            minDecimal = bigDecimal;
            maxDecimal = bigDecimal;
        } else  {
            if (minDecimal.compareTo(bigDecimal) > 0) {
                minDecimal = bigDecimal;
            }
            if (maxDecimal.compareTo(bigDecimal) < 0) {
                maxDecimal = bigDecimal;
            }
        }
    }

    public void calculateMinMaxInteger(BigDecimal bigDecimal) {
        if (minInt == null || maxInt == null) {
            minInt = bigDecimal;
            maxInt = bigDecimal;
        } else  {
            if (minInt.compareTo(bigDecimal) > 0) {
                minInt = bigDecimal;
            }
            if (maxInt.compareTo(bigDecimal) < 0) {
                maxInt = bigDecimal;
            }
        }
    }

    public void calculateMinMaxStringLength(String string) {
        int length = string.length();
        if (shortestString == null || longestString == null) {
            shortestString = length;
            longestString = length;
        } else {
            if (shortestString > length) {
                shortestString = length;
            }
            if (longestString < length) {
                longestString = length;
            }
        }
    }

    private void printStatisticsForEachType(Number min, Number max, Number sum, Number avg) {
        System.out.println("Минимальное число: " + min);
        System.out.println("Максимальное число: " + max);
        System.out.println("Сумма: " + sum);
        System.out.println("Среднее значение: " + avg);
        System.out.println();
    }

    private void printStatisticsForEachType(int shortest, int longest) {
        System.out.println("Самая короткая строка: " + shortest + " символов.");
        System.out.println("Самая длинная строка: " + longest + " символов.");
        System.out.println("Обнаружено пустых строк: " + emptyStringCount);
        System.out.println();
    }
}
