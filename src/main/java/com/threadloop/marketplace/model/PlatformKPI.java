package com.threadloop.marketplace.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "platform_kpis")
public class PlatformKPI {

    @Id
    private String id = "global";

    private Integer totalUsers = 14820;
    private Integer activeListings = 4390;
    private Integer completedSwaps = 8740;
    private Double totalKgWasteDiverted = 12450.0;
    private Double totalLitersWaterSaved = 23600000.0;
    private Double totalKgCo2Avoided = 48070.0;
    private Double swapSuccessRate = 94.8;

    public PlatformKPI() {}

    public PlatformKPI(String id, Integer totalUsers, Integer activeListings, Integer completedSwaps,
                       Double totalKgWasteDiverted, Double totalLitersWaterSaved,
                       Double totalKgCo2Avoided, Double swapSuccessRate) {
        this.id = id != null ? id : "global";
        this.totalUsers = totalUsers;
        this.activeListings = activeListings;
        this.completedSwaps = completedSwaps;
        this.totalKgWasteDiverted = totalKgWasteDiverted;
        this.totalLitersWaterSaved = totalLitersWaterSaved;
        this.totalKgCo2Avoided = totalKgCo2Avoided;
        this.swapSuccessRate = swapSuccessRate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
