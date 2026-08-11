package com.cityu.sdg.model;

public abstract class User {
    private String userID;
    private String name;
    private String email;

    public User(String userID, String name, String email) {
        setUserID(userID);
        setName(name);
        setEmail(email);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        if (userID == null || userID.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty.");
        }
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getAccountSummary();
}
