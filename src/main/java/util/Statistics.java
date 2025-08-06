package util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Statistics {
    private @Getter @Setter BigDecimal minInt;
    private @Getter @Setter BigDecimal maxInt;
    private @Getter @Setter BigDecimal sumInt;
    private @Getter @Setter BigDecimal minFloat;
    private @Getter @Setter BigDecimal maxFloat;
    private @Getter @Setter BigDecimal sumFloat;
    private @Getter @Setter Integer shortestString, longestString;
    private @Getter @Setter BigDecimal floatAvg, intAvg;
    private @Getter int emptyStringCount;

    public void printStatistics(
            Boolean isShortStat,
            Boolean isFullStat,
            int stringCount,
            int floatCount,
            int intCount) {
        if (stringCount == 0 && floatCount == 0 && intCount == 0) {
            return;
        }
        if (isShortStat || isFullStat) {
            PrintUtils.printShortStatistics(stringCount, intCount, floatCount);
        }
        if (isFullStat) {
            calculateAvg(floatCount, intCount);
            PrintUtils.printFullStatistics(this);
        }
    }

    public void calculateAvg(int floatsCount, int intsCount) {
        if (sumFloat != null) {
            floatAvg = sumFloat.divide(BigDecimal.valueOf(floatsCount), 10,  RoundingMode.HALF_UP);
        }
        if (sumInt != null) {
            intAvg = sumInt.divide(BigDecimal.valueOf(intsCount), 1,  RoundingMode.HALF_UP);
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
                updateFloatMinMaxAndSum(bigDecimal);
                break;
            case INTEGER:
                BigDecimal bigInteger = new BigDecimal(string);
                updateIntegerMinMaxAndSum(bigInteger);
                break;
            case  STRING:
                updateStringMinMaxAndSum(string);
                break;
            default:
                System.out.println("Неизвестный тип данных");
        }
    }

    public void updateFloatMinMaxAndSum(BigDecimal bigDecimal) {
        if (minFloat == null || maxFloat == null || sumFloat == null) {
            minFloat = bigDecimal;
            maxFloat = bigDecimal;
            sumFloat = bigDecimal;
        } else  {
            if (minFloat.compareTo(bigDecimal) > 0) {
                minFloat = bigDecimal;
            }
            if (maxFloat.compareTo(bigDecimal) < 0) {
                maxFloat = bigDecimal;
            }
            sumFloat = sumFloat.add(bigDecimal);
        }
    }

    public void updateIntegerMinMaxAndSum(BigDecimal bigInteger) {
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

    public void updateStringMinMaxAndSum(String string) {
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
}
