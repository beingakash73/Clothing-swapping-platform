package com.threadloop.marketplace.util;

public class FairnessCalculator {

    public static class FairnessResult {
        private final double score;
        private final double difference;

        public FairnessResult(double score, double difference) {
            this.score = score;
            this.difference = difference;
        }

        public double getScore() { return score; }
        public double getDifference() { return difference; }
    }

    public static FairnessResult calculate(double requestedValue, double offeredValue) {
        if (requestedValue == 0 && offeredValue == 0) {
            return new FairnessResult(100.0, 0.0);
        }
        double diff = offeredValue - requestedValue;
        double maxVal = Math.max(requestedValue, offeredValue);
        double minVal = Math.min(requestedValue, offeredValue);
        double ratio = maxVal > 0 ? (minVal / maxVal) : 1.0;
        double score = Math.round(ratio * 100.0);
        return new FairnessResult(score, diff);
    }
}
