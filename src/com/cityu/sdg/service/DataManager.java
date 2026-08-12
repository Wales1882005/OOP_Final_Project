package com.cityu.sdg.service;

import com.cityu.sdg.model.CarbonLog;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final List<CarbonLog> logList;
    private static final String FILE_PATH = "climate_logs.txt";
    private static final String AUDIT_LOG_PATH = "audit_trail.log";

    public DataManager() {
        this.logList = new ArrayList<>();
    }

    public void addCarbonLog(CarbonLog log) {
        logList.add(log);
        logAuditEvent("CREATE", "Added log entry: " + log.toString());
    }

    public String generateNextLogID() {
        int maxId = 0;
        for (CarbonLog log : logList) {
            String id = log.getLogID();
            if (id != null && id.startsWith("LOG-")) {
                try {
                    int value = Integer.parseInt(id.substring(4));
                    if (value > maxId) {
                        maxId = value;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return "LOG-" + (maxId + 1);
    }

    public void updateCarbonLog(int index, String newActivity, double newCo2, String newCategory) {
        CarbonLog oldLog = logList.get(index);
        String oldState = oldLog.toString();

        oldLog.setActivityName(newActivity);
        oldLog.setCo2Amount(newCo2);
        oldLog.setCategory(newCategory);

        logAuditEvent("UPDATE", "Updated " + oldState + " -> NEW: " + oldLog.toString());
    }

    public void removeCarbonLog(int index) {
        CarbonLog removedLog = logList.get(index);
        logList.remove(index);
        logAuditEvent("DELETE", "Deleted log entry: " + removedLog.toString());
    }

    public List<CarbonLog> getLogList() {
        return logList;
    }

    public double calculateTotalEmissions() {
        double total = 0.0;
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
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (CarbonLog log : logList) {
                writer.println(log.toFileFormat());
            }
        }
        logAuditEvent("SAVE", "Persisted " + logList.size() + " records to " + FILE_PATH);
    }

    public void loadDataFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        logList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                CarbonLog log = CarbonLog.fromFileFormat(line);
                if (log != null) {
                    logList.add(log);
                }
            }
        }
        logAuditEvent("LOAD", "Loaded " + logList.size() + " records from " + FILE_PATH);
    }

    public void logAuditEvent(String actionType, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = String.format("[%s] AUDIT [%s]: %s", timestamp, actionType, details);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(AUDIT_LOG_PATH, true)))) {
            out.println(entry);
        } catch (IOException e) {
            System.err.println("Failed to write to audit log: " + e.getMessage());
        }
    }
}
