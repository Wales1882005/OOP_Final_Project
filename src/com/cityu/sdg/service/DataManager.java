package com.cityu.sdg.service;

import com.cityu.sdg.model.*;
import java.io.*;
import java.util.*;

public class DataManager {
    private List<CarbonLog> logList;
    private Map<String, User> userMap;
    private static final String FILE_PATH = "climate_logs.txt";

    public DataManager() {
        this.logList = new ArrayList<>();
        this.userMap = new HashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getUserID(), user);
    }

    public User getUser(String userID) {
        return userMap.get(userID);
    }

    public void addCarbonLog(CarbonLog log) {
        logList.add(log);
    }

    public List<CarbonLog> getLogList() {
        return logList;
    }

    public double calculateTotalEmissions() {
        double total = 0;
        for (CarbonLog log : logList) {
            if ("Offset".equalsIgnoreCase(log.getCategory())) {
                total -= log.getCo2Amount();
            } else {
                total += log.getCo2Amount();
            }
        }
        return total;
    }

    public void saveDataToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (CarbonLog log : logList) {
                writer.write(log.getLogID() + "," +
                             log.getActivityName() + "," +
                             log.getCo2Amount() + "," +
                             log.getCategory());
                writer.newLine();
            }
        }
    }

    public void loadDataFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        logList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String category = parts[3];
                    logList.add(new CarbonLog(id, name, amount, category));
                }
            }
        }
    }
}
