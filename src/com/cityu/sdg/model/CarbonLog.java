package com.cityu.sdg.model;

public class CarbonLog {
    private String logID;
    private String activityName;
    private double co2Amount;
    private String category;

    public CarbonLog(String logID, String activityName, double co2Amount, String category) {
        setCo2Amount(co2Amount);
        this.logID = logID;
        this.activityName = activityName;
        this.category = category;
    }

    public String getLogID() { return logID; }
    public String getActivityName() { return activityName; }
    public double getCo2Amount() { return co2Amount; }
    public String getCategory() { return category; }

    public void setActivityName(String activityName) { this.activityName = activityName; }

    public void setCo2Amount(double co2Amount) {
        if (co2Amount < 0) {
            throw new IllegalArgumentException("CO₂ amount cannot be negative.");
        }
        this.co2Amount = co2Amount;
    }

    public void setCategory(String category) { this.category = category; }

    public boolean isHighEmission() {
        return co2Amount >= 50.0 && !"Offset".equalsIgnoreCase(category);
    }

    public String toFileFormat() {
        return logID + "," + activityName + "," + co2Amount + "," + category;
    }

    public static CarbonLog fromFileFormat(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            return new CarbonLog(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("[%s | %s | %.2f tCO2e | %s]", logID, activityName, co2Amount, category);
    }
}
