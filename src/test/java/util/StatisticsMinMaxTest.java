package util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticsMinMaxTest {

    @Test
    void calculateMinMaxIntegerWhenOldValuesIsNull(){
        Statistics statistics = new Statistics();
        BigDecimal newMax = BigDecimal.valueOf(100);
        BigDecimal newMin = BigDecimal.valueOf(0);
        statistics.calculateMinMaxSumInteger(newMax.toString());
        statistics.calculateMinMaxSumInteger(newMin.toString());
        assertThat(statistics.getMaxInt()).isEqualTo(newMax);
        assertThat(statistics.getMinInt()).isEqualTo(newMin);
    }

    @Test
    void calculateMinMaxIntegerWhenBothChangedTest() {
        Statistics statistics = new Statistics();
        statistics.setMaxInt(BigDecimal.valueOf(5));
        statistics.setMinInt(BigDecimal.valueOf(1));
        BigDecimal newMax = BigDecimal.valueOf(21);
        BigDecimal newMin = BigDecimal.valueOf(-70);
        statistics.calculateMinMaxSumInteger(newMax.toString());
        statistics.calculateMinMaxSumInteger(newMin.toString());
        assertThat(statistics.getMaxInt()).isEqualTo(newMax);
        assertThat(statistics.getMinInt()).isEqualTo(newMin);
    }

    @Test
    void calculateMinMaxIntegerWhenNothingChangedTest() {
        Statistics statistics = new Statistics();
        BigDecimal oldMax = BigDecimal.valueOf(3000);
        BigDecimal oldMin = BigDecimal.valueOf(-1440);
        statistics.setMaxInt(oldMax);
        statistics.setMinInt(oldMin);
        BigDecimal maxInput = BigDecimal.valueOf(2121);
        BigDecimal minInput = BigDecimal.valueOf(-700);
        statistics.calculateMinMaxSumInteger(maxInput.toString());
        statistics.calculateMinMaxSumInteger(minInput.toString());
        assertThat(statistics.getMaxInt()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(statistics.getMinInt()).isEqualTo(BigDecimal.valueOf(-1440));
    }

    @Test
    void calculateMinMaxSumDecimalWhenOldValuesIsNullTest(){
        Statistics statistics = new Statistics();
        BigDecimal newMax = BigDecimal.valueOf(100);
        BigDecimal newMin = BigDecimal.valueOf(0);
        statistics.calculateMinMaxSumDecimal(newMax.toString());
        statistics.calculateMinMaxSumDecimal(newMin.toString() );
        assertThat(statistics.getMaxDecimal()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(statistics.getMinDecimal()).isEqualTo(BigDecimal.valueOf(0));
    }

    @Test
    void calculateMinMaxSumDecimalWhenBothChangedTest() {
        Statistics statistics = new Statistics();
        statistics.setMaxDecimal(BigDecimal.valueOf(5));
        statistics.setMinDecimal(BigDecimal.valueOf(1));
        BigDecimal newMax = BigDecimal.valueOf(21);
        BigDecimal newMin = BigDecimal.valueOf(-70);
        statistics.calculateMinMaxSumDecimal(newMax.toString());
        statistics.calculateMinMaxSumDecimal(newMin.toString() );
        assertThat(statistics.getMaxDecimal()).isEqualTo(newMax);
        assertThat(statistics.getMinDecimal()).isEqualTo(newMin);
    }

    @Test
    void calculateMinMaxSumDecimalWhenNothingChangedTest() {
        Statistics statistics = new Statistics();
        statistics.setMaxDecimal(BigDecimal.valueOf(3000));
        statistics.setMinDecimal(BigDecimal.valueOf(-1440));
        BigDecimal maxInput = BigDecimal.valueOf(2121);
        BigDecimal minInput = BigDecimal.valueOf(-700);
        statistics.calculateMinMaxSumDecimal(maxInput.toString());
        statistics.calculateMinMaxSumDecimal(minInput.toString());
        assertThat(statistics.getMaxDecimal()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(statistics.getMinDecimal()).isEqualTo(BigDecimal.valueOf(-1440));
    }

    @Test
    void calculateMinMaxStringLengthWhenOldValuesIsNullTest() {
        Statistics statistics = new Statistics();
        String inputShort = "Hello World";
        String inputLong = "Java the best!!!";
        statistics.calculateMinMaxStringLength(inputShort);
        statistics.calculateMinMaxStringLength(inputLong);
        assertThat(statistics.getShortestString()).isEqualTo(inputShort.length());
        assertThat(statistics.getLongestString()).isEqualTo(inputLong.length());
    }

    @Test
    void calculateMinMaxStringLengthWhenBothChangedTest() {
        Statistics statistics = new Statistics();
        statistics.setShortestString(5);
        statistics.setLongestString(8);
        String inputShort = "Hi!";
        String inputLong = "Java the best!!!";
        statistics.calculateMinMaxStringLength(inputShort);
        statistics.calculateMinMaxStringLength(inputLong);
        assertThat(statistics.getShortestString()).isEqualTo(inputShort.length());
        assertThat(statistics.getLongestString()).isEqualTo(inputLong.length());
    }

    @Test
    void calculateMinMaxStringLengthWhenNothingChangedTest() {
        Statistics statistics = new Statistics();
        statistics.setShortestString(1);
        statistics.setLongestString(20);
        String inputShort = "Hello World";
        String inputLong = "Java the best!!!";
        statistics.calculateMinMaxStringLength(inputShort);
        statistics.calculateMinMaxStringLength(inputLong);
        assertThat(statistics.getShortestString()).isEqualTo(1);
        assertThat(statistics.getLongestString()).isEqualTo(20);
    }




}