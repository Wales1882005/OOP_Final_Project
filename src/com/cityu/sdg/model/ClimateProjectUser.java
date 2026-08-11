package com.cityu.sdg.model;

import com.cityu.sdg.service.Prioritizable;

public class ClimateProjectUser extends User implements Prioritizable {
    private String projectType;
    private int impactRating;

    public ClimateProjectUser(String userID, String name, String email, String projectType, int impactRating) {
        super(userID, name, email);
        this.projectType = projectType;
        setImpactRating(impactRating);
    }

    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }

    public int getImpactRating() { return impactRating; }
    public void setImpactRating(int impactRating) {
        if (impactRating < 1 || impactRating > 5) {
            throw new IllegalArgumentException("Impact rating must be between 1 and 5.");
        }
        this.impactRating = impactRating;
    }

    @Override
    public double calculatePriorityScore() {
        return impactRating * 20.0;
    }

    @Override
    public String getAccountSummary() {
        return "[CLIMATE PROJECT] ID: " + getUserID() + " | Project: " + getName() +
               " | Type: " + projectType + " | Priority Score: " + calculatePriorityScore();
    }
}
