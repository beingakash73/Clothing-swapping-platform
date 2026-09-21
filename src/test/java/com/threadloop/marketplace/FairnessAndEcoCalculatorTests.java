package com.threadloop.marketplace;

import com.threadloop.marketplace.util.EcoCalculator;
import com.threadloop.marketplace.util.FairnessCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FairnessAndEcoCalculatorTests {

    @Test
    void testFairnessCalculationEqualValues() {
        FairnessCalculator.FairnessResult result = FairnessCalculator.calculate(195.0, 195.0);
        assertEquals(100.0, result.getScore());
        assertEquals(0.0, result.getDifference());
    }

    @Test
    void testFairnessCalculationDifferentValues() {
        FairnessCalculator.FairnessResult result = FairnessCalculator.calculate(160.0, 95.0);
        assertEquals(59.0, result.getScore());
        assertEquals(-65.0, result.getDifference());
    }

    @Test
    void testFairnessCalculationZeroValues() {
        FairnessCalculator.FairnessResult result = FairnessCalculator.calculate(0.0, 0.0);
        assertEquals(100.0, result.getScore());
        assertEquals(0.0, result.getDifference());
    }

    @Test
    void testEcoCalculatorCalculations() {
        EcoCalculator.EcoMetrics metrics = EcoCalculator.calculateForPrice(229.0);
        assertEquals(9.2, metrics.getCo2SavedKg());
        assertEquals(4122.0, metrics.getWaterSavedLiters());
    }
}
