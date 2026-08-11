package com.cityu.sdg.model;

public class CarbonLog {
    private String logID;
    private String activityName;
    private double co2Amount;
    private String category;

    public CarbonLog(String logID, String activityName, double co2Amount, String category) {
        setLogID(logID);
        setActivityName(activityName);
        setCo2Amount(co2Amount);
        setCategory(category);
    }

    public String getLogID() { return logID; }
    public void setLogID(String logID) { this.logID = logID; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public double getCo2Amount() { return co2Amount; }
    public void setCo2Amount(double co2Amount) {
        if (co2Amount < 0) {
            throw new IllegalArgumentException("CO2 amount cannot be negative.");
        }
        this.co2Amount = co2Amount;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isHighEmission() {
        return co2Amount >= 50.0;
    }
}
