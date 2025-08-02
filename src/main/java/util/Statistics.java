package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Statistics {
    private BigDecimal minInt;
    private BigDecimal maxInt;
    private BigDecimal sumInt;
    private BigDecimal minDecimal;
    private BigDecimal maxDecimal;
    private BigDecimal sumDecimal;
    private Integer shortestString, longestString;
    private int emptyStringCount;

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
                calculateMinMaxSumDecimal(string);
                break;
            case INTEGER:
                calculateMinMaxSumInteger(string);
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

    public void calculateMinMaxSumDecimal(String string) {
        BigDecimal bigDecimal = new BigDecimal(string);
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

    public void calculateMinMaxSumInteger(String string) {
        BigDecimal bigInteger = new BigDecimal(string);
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

    public BigDecimal getMinInt() {
        return minInt;
    }

    public void setMinInt(BigDecimal minInt) {
        this.minInt = minInt;
    }

    public BigDecimal getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(BigDecimal maxInt) {
        this.maxInt = maxInt;
    }

    public BigDecimal getMinDecimal() {
        return minDecimal;
    }

    public void setMinDecimal(BigDecimal minDecimal) {
        this.minDecimal = minDecimal;
    }

    public BigDecimal getMaxDecimal() {
        return maxDecimal;
    }

    public void setMaxDecimal(BigDecimal maxDecimal) {
        this.maxDecimal = maxDecimal;
    }

    public Integer getShortestString() {
        return shortestString;
    }

    public void setShortestString(Integer shortestString) {
        this.shortestString = shortestString;
    }

    public Integer getLongestString() {
        return longestString;
    }

    public void setLongestString(Integer longestString) {
        this.longestString = longestString;
    }

    public BigDecimal getSumInt() {
        return sumInt;
    }

    public void setSumInt(BigDecimal sumInt) {
        this.sumInt = sumInt;
    }

    public BigDecimal getSumDecimal() {
        return sumDecimal;
    }

    public void setSumDecimal(BigDecimal sumDecimal) {
        this.sumDecimal = sumDecimal;
    }

    public int getEmptyStringCount() {
        return emptyStringCount;
    }

    public void setEmptyStringCount(int emptyStringCount) {
        this.emptyStringCount = emptyStringCount;
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
