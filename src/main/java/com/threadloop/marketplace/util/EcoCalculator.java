package com.threadloop.marketplace.util;

public class EcoCalculator {

    public static class EcoMetrics {
        private final double co2SavedKg;
        private final double waterSavedLiters;

        public EcoMetrics(double co2SavedKg, double waterSavedLiters) {
            this.co2SavedKg = co2SavedKg;
            this.waterSavedLiters = waterSavedLiters;
        }

        public double getCo2SavedKg() { return co2SavedKg; }
        public double getWaterSavedLiters() { return waterSavedLiters; }
    }

    public static EcoMetrics calculateForPrice(Double originalPrice) {
        double price = (originalPrice != null && originalPrice > 0) ? originalPrice : 100.0;
        double co2 = Math.round((price * 0.04) * 10.0) / 10.0;
        double water = Math.round(price * 18.0);
        return new EcoMetrics(co2, water);
    }
}
