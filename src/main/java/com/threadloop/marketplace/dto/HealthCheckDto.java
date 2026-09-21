package com.threadloop.marketplace.dto;

public class HealthCheckDto {
    private String status;
    private String database;
    private String engine;
    private Long totalUsers;
    private String timestamp;

    public HealthCheckDto() {}

    public HealthCheckDto(String status, String database, String engine, Long totalUsers, String timestamp) {
        this.status = status;
        this.database = database;
        this.engine = engine;
        this.totalUsers = totalUsers;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
