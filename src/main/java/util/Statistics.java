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
    private @Getter @Setter BigDecimal decimalAvg, intAvg;
    private @Getter int emptyStringCount;

    public void printStatistics(
            Boolean isShortStat,
            Boolean isFullStat,
            PrintUtils printUtils,
            List<String> strings,
            List<String> ints,
            List<String> floats) {
        if (isShortStat || isFullStat) {
            printUtils.printShortStatistics(strings, floats, ints);
        }
        if (isFullStat) {
            calculateAvg(floats.size(), ints.size());
            printUtils.getFullStatistics();
        }
    }

    private void calculateAvg (int floatsCount, int intsCount) {
        decimalAvg = sumDecimal.divide(BigDecimal.valueOf(floatsCount), RoundingMode.HALF_UP);
        intAvg = sumInt.divide(BigDecimal.valueOf(intsCount), RoundingMode.HALF_UP);
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
        }
    }

    public void updateFloatMinMaxAndSum(BigDecimal bigDecimal) {
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
