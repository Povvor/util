package util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Statistics {
    private @Getter @Setter BigDecimal minInt;
    private @Getter @Setter BigDecimal maxInt;
    private @Getter @Setter BigDecimal sumInt;
    private @Getter @Setter BigDecimal minDecimal;
    private @Getter @Setter BigDecimal maxDecimal;
    private @Getter @Setter BigDecimal sumDecimal;
    private @Getter @Setter Integer shortestString, longestString;
    private @Getter int emptyStringCount;

    public void getFullStatistics(int floatsCount, int intsCount) {
        if  (minInt != null) {
            BigDecimal avgInt = sumInt.divide(BigDecimal.valueOf(intsCount), RoundingMode.HALF_UP);
            System.out.println("Полная статистика для целых чисел:");
            printStatisticsForEachType(minInt, maxInt, sumInt, avgInt);

        } else {
            System.out.println("Целых чисел не обнаружено!");
        }

        if  (minDecimal != null) {
            BigDecimal avgDecimal = sumDecimal.divide(BigDecimal.valueOf(floatsCount), RoundingMode.HALF_UP);
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
                calculateMinMaxSumDecimal(bigDecimal);
                break;
            case INTEGER:
                BigDecimal bigInteger = new BigDecimal(string);
                calculateMinMaxSumInteger(bigInteger);
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

    public void calculateMinMaxSumDecimal(BigDecimal bigDecimal) {
        if (minDecimal == null || maxDecimal == null || sumDecimal == null) {
            minDecimal = bigDecimal;
            maxDecimal = bigDecimal;
            sumDecimal = bigDecimal;
        } else  {
            if (minDecimal.compareTo(bigDecimal) > 0) {
                minDecimal = bigDecimal;
            }
            if (maxDecimal.compareTo(bigDecimal) < 0) {
                maxDecimal = bigDecimal;
            }
            sumDecimal = sumDecimal.add(bigDecimal);
        }
    }

    public void calculateMinMaxSumInteger(BigDecimal bigInteger) {
        if (minInt == null || maxInt == null || sumInt == null) {
            minInt = bigInteger;
            maxInt = bigInteger;
            sumInt = bigInteger;
        } else  {
            if (minInt.compareTo(bigInteger) > 0) {
                minInt = bigInteger;
            }
            if (maxInt.compareTo(bigInteger) < 0) {
                maxInt = bigInteger;
            }
            sumInt = sumInt.add(bigInteger);
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
