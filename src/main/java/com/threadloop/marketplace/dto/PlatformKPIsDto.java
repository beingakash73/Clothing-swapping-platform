package com.threadloop.marketplace.dto;

public class PlatformKPIsDto {
    private Integer totalUsers;
    private Integer activeListings;
    private Integer completedSwaps;
    private Double totalKgWasteDiverted;
    private Double totalLitersWaterSaved;
    private Double totalKgCo2Avoided;
    private Double swapSuccessRate;

    public PlatformKPIsDto() {}

    public PlatformKPIsDto(Integer totalUsers, Integer activeListings, Integer completedSwaps,
                           Double totalKgWasteDiverted, Double totalLitersWaterSaved,
                           Double totalKgCo2Avoided, Double swapSuccessRate) {
        this.totalUsers = totalUsers;
        this.activeListings = activeListings;
        this.completedSwaps = completedSwaps;
        this.totalKgWasteDiverted = totalKgWasteDiverted;
        this.totalLitersWaterSaved = totalLitersWaterSaved;
        this.totalKgCo2Avoided = totalKgCo2Avoided;
        this.swapSuccessRate = swapSuccessRate;
    }

    public Integer getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }

    public Integer getActiveListings() { return activeListings; }
    public void setActiveListings(Integer activeListings) { this.activeListings = activeListings; }

    public Integer getCompletedSwaps() { return completedSwaps; }
    public void setCompletedSwaps(Integer completedSwaps) { this.completedSwaps = completedSwaps; }

    public Double getTotalKgWasteDiverted() { return totalKgWasteDiverted; }
    public void setTotalKgWasteDiverted(Double totalKgWasteDiverted) { this.totalKgWasteDiverted = totalKgWasteDiverted; }

    public Double getTotalLitersWaterSaved() { return totalLitersWaterSaved; }
    public void setTotalLitersWaterSaved(Double totalLitersWaterSaved) { this.totalLitersWaterSaved = totalLitersWaterSaved; }

    public Double getTotalKgCo2Avoided() { return totalKgCo2Avoided; }
    public void setTotalKgCo2Avoided(Double totalKgCo2Avoided) { this.totalKgCo2Avoided = totalKgCo2Avoided; }

    public Double getSwapSuccessRate() { return swapSuccessRate; }
    public void setSwapSuccessRate(Double swapSuccessRate) { this.swapSuccessRate = swapSuccessRate; }
}
