package util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticsTest {

    @Test
    void calculateMinMaxDecimal() {
        Statistics statistics = new Statistics();
        statistics.maxInt = BigDecimal.valueOf(5);
        statistics.minInt = BigDecimal.valueOf(1);
        BigDecimal newMax = BigDecimal.valueOf(21);
        BigDecimal newMin = BigDecimal.valueOf(-70);
        statistics.calculateMinMaxInteger(newMax);
        statistics.calculateMinMaxInteger(newMin);
        assertThat(statistics.maxInt).isEqualTo(newMax);
        assertThat(statistics.minInt).isEqualTo(newMin);
    }

}