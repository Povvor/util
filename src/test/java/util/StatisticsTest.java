package util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class StatisticsTest {

    @Test
    void processStringForStatisticsWhenInput4Empty() {
        Statistics statistics = new Statistics();
        for (int i = 0; i < 4; i++) {
            statistics.processStringForStatistics("", true, Type.STRING);
        }
        assertThat(statistics.getEmptyStringCount()).isEqualTo(4);
    }

    @Test
    void processStringForStatisticsWhenInputString() {
        Statistics statistics = new Statistics();
        String input = "A";
        statistics.processStringForStatistics(input, true, Type.STRING);
        assertThat(statistics.getShortestString()).isNotNull();
        assertThat(statistics.getLongestString()).isNotNull();
    }

    @Test
    void whenInput2StringsWhenInputInteger() {
        Statistics statistics = new Statistics();
        String input = "1";
        statistics.processStringForStatistics(input, true, Type.INTEGER);
        assertThat(statistics.getSumInt()).isNotNull();
    }

    @Test
    void whenInput2StringsWhenInputFloat() {
        Statistics statistics = new Statistics();
        String input = "1.2";
        statistics.processStringForStatistics(input, true, Type.FLOAT);
        assertThat(statistics.getSumDecimal()).isNotNull();
    }

    @Test
    void updateIntegerMinMaxAndSumWhenOldValuesIsNull(){
        Statistics statistics = new Statistics();
        statistics.setSumInt(BigDecimal.ZERO);
        BigDecimal newMax = BigDecimal.valueOf(100);
        BigDecimal newMin = BigDecimal.valueOf(0);
        statistics.updateIntegerMinMaxAndSum(newMax);
        statistics.updateIntegerMinMaxAndSum(newMin);
        assertThat(statistics.getMaxInt()).isEqualTo(newMax);
        assertThat(statistics.getMinInt()).isEqualTo(newMin);
    }

    @Test
    void updateIntegerMinMaxAndSumWhenBothChanged() {
        Statistics statistics = new Statistics();
        statistics.setSumInt(BigDecimal.ZERO);
        statistics.setMaxInt(BigDecimal.valueOf(5));
        statistics.setMinInt(BigDecimal.valueOf(1));
        BigDecimal newMax = BigDecimal.valueOf(21);
        BigDecimal newMin = BigDecimal.valueOf(-70);
        statistics.updateIntegerMinMaxAndSum(newMax);
        statistics.updateIntegerMinMaxAndSum(newMin);
        assertThat(statistics.getMaxInt()).isEqualTo(newMax);
        assertThat(statistics.getMinInt()).isEqualTo(newMin);
    }

    @Test
    void updateIntegerMinMaxAndSumWhenNothingChanged() {
        Statistics statistics = new Statistics();
        BigDecimal oldMax = BigDecimal.valueOf(3000);
        BigDecimal oldMin = BigDecimal.valueOf(-1440);
        statistics.setSumInt(BigDecimal.ZERO);
        statistics.setMaxInt(oldMax);
        statistics.setMinInt(oldMin);
        BigDecimal maxInput = BigDecimal.valueOf(2121);
        BigDecimal minInput = BigDecimal.valueOf(-700);
        statistics.updateIntegerMinMaxAndSum(maxInput);
        statistics.updateIntegerMinMaxAndSum(minInput);
        assertThat(statistics.getMaxInt()).isEqualTo(oldMax);
        assertThat(statistics.getMinInt()).isEqualTo(oldMin);
    }

    @Test
    void updateIntegerMinMaxAndSumSumTest() {
        Statistics statistics = new Statistics();
        BigDecimal input1 = BigDecimal.valueOf(1000);
        BigDecimal input2 = BigDecimal.valueOf(2000);
        BigDecimal input3 = BigDecimal.valueOf(-3500);
        statistics.updateIntegerMinMaxAndSum(input1);
        statistics.updateIntegerMinMaxAndSum(input2);
        statistics.updateIntegerMinMaxAndSum(input3);
        BigDecimal expected = input1.add(input2.add(input3));
        assertThat(statistics.getSumInt()).isEqualTo(expected);
        }

    @Test
    void updateFloatMinMaxAndSumWhenOldValuesIsNull(){
        Statistics statistics = new Statistics();
        statistics.setSumInt(BigDecimal.ZERO);
        BigDecimal newMax = BigDecimal.valueOf(100);
        BigDecimal newMin = BigDecimal.valueOf(0);
        statistics.updateFloatMinMaxAndSum(newMax);
        statistics.updateFloatMinMaxAndSum(newMin);
        assertThat(statistics.getMaxDecimal()).isEqualTo(newMax);
        assertThat(statistics.getMinDecimal()).isEqualTo(newMin);
    }

    @Test
    void updateFloatMinMaxAndSumWhenBothChanged() {
        Statistics statistics = new Statistics();
        statistics.setSumDecimal(BigDecimal.ZERO);
        statistics.setMaxDecimal(BigDecimal.valueOf(5));
        statistics.setMinDecimal(BigDecimal.valueOf(1));
        BigDecimal newMax = BigDecimal.valueOf(21);
        BigDecimal newMin = BigDecimal.valueOf(-70);
        statistics.updateFloatMinMaxAndSum(newMax);
        statistics.updateFloatMinMaxAndSum(newMin);
        assertThat(statistics.getMaxDecimal()).isEqualTo(newMax);
        assertThat(statistics.getMinDecimal()).isEqualTo(newMin);
    }

    @Test
    void updateFloatMinMaxAndSumWhenNothingChanged() {
        Statistics statistics = new Statistics();
        statistics.setSumDecimal(BigDecimal.ZERO);
        BigDecimal oldMax = BigDecimal.valueOf(3000);
        BigDecimal oldMin = BigDecimal.valueOf(-1440);
        statistics.setMaxDecimal(oldMax);
        statistics.setMinDecimal(oldMin);
        BigDecimal maxInput = BigDecimal.valueOf(2121);
        BigDecimal minInput = BigDecimal.valueOf(-700);
        statistics.updateFloatMinMaxAndSum(maxInput);
        statistics.updateFloatMinMaxAndSum(minInput);
        assertThat(statistics.getMaxDecimal()).isEqualTo(oldMax);
        assertThat(statistics.getMinDecimal()).isEqualTo(oldMin);
    }

    @Test
    void updateFloatMinMaxAndSumSumTest() {
        Statistics statistics = new Statistics();
        BigDecimal input1 = BigDecimal.valueOf(123.54);
        BigDecimal input2 = BigDecimal.valueOf(24.54);
        BigDecimal input3 = BigDecimal.valueOf(-12.3333);
        statistics.updateFloatMinMaxAndSum(input1);
        statistics.updateFloatMinMaxAndSum(input2);
        statistics.updateFloatMinMaxAndSum(input3);
        BigDecimal expected = input1.add(input2.add(input3));
        assertThat(statistics.getSumDecimal()).isEqualTo(expected);
    }

    @Test
    void updateStringMinMaxAndSumWhenOldValuesIsNull() {
        Statistics statistics = new Statistics();
        String inputShort = "Hello World";
        String inputLong = "Java the best!!!";
        statistics.updateStringMinMaxAndSum(inputShort);
        statistics.updateStringMinMaxAndSum(inputLong);
        assertThat(statistics.getShortestString()).isEqualTo(inputShort.length());
        assertThat(statistics.getLongestString()).isEqualTo(inputLong.length());
    }

    @Test
    void updateStringMinMaxAndSumWhenBothChanged() {
        Statistics statistics = new Statistics();
        statistics.setShortestString(5);
        statistics.setLongestString(8);
        String inputShort = "Hi!";
        String inputLong = "Java the best!!!";
        statistics.updateStringMinMaxAndSum(inputShort);
        statistics.updateStringMinMaxAndSum(inputLong);
        assertThat(statistics.getShortestString()).isEqualTo(inputShort.length());
        assertThat(statistics.getLongestString()).isEqualTo(inputLong.length());
    }

    @Test
    void updateStringMinMaxAndSumWhenNothingChanged() {
        Statistics statistics = new Statistics();
        statistics.setShortestString(1);
        statistics.setLongestString(20);
        String inputShort = "Hello World";
        String inputLong = "Java the best!!!";
        statistics.updateStringMinMaxAndSum(inputShort);
        statistics.updateStringMinMaxAndSum(inputLong);
        assertThat(statistics.getShortestString()).isEqualTo(1);
        assertThat(statistics.getLongestString()).isEqualTo(20);
    }
}