package com.cityu.sdg.model;

public class CorporateUser extends User {
    private String industrySector;

    public CorporateUser(String userID, String name, String email, String industrySector) {
        super(userID, name, email);
        this.industrySector = industrySector;
    }

    public String getIndustrySector() {
        return industrySector;
    }

    public void setIndustrySector(String industrySector) {
        this.industrySector = industrySector;
    }

    @Override
    public String getAccountSummary() {
        return "[CORPORATE] ID: " + getUserID() + " | Company: " + getName() +
                " | Industry: " + industrySector + " | Contact: " + getEmail();
    }
}
