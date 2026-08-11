package com.cityu.sdg.gui;

import com.cityu.sdg.model.CarbonLog;
import com.cityu.sdg.service.DataManager;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainDashboardGUI extends JFrame {
    private DataManager dataManager;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JTextField txtActivity, txtCo2;
    private JComboBox<String> cbCategory;
    private JLabel lblTotalEmissions;

    public MainDashboardGUI() {
        dataManager = new DataManager();

        setTitle("SDG 13: Carbon Footprint & Climate Action Tracker");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("SDG 13 — Climate Action: Carbon Emission & Offset Tracker");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Log Emission or Offset Activity"));

        formPanel.add(new JLabel("Activity Name / Description:"));
        txtActivity = new JTextField();
        formPanel.add(txtActivity);

        formPanel.add(new JLabel("CO₂ Amount (Metric Tonnes - tCO2e):"));
        txtCo2 = new JTextField();
        formPanel.add(txtCo2);

        formPanel.add(new JLabel("Category:"));
        String[] categories = {"Energy", "Transportation", "Manufacturing", "Offset"};
        cbCategory = new JComboBox<>(categories);
        formPanel.add(cbCategory);

        JButton btnAdd = new JButton("Add Log Entry");
        JButton btnSave = new JButton("Save Data to File");
        formPanel.add(btnAdd);
        formPanel.add(btnSave);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Log ID", "Activity Name", "CO₂ Amount (tCO2e)", "Category", "High Impact?"};
        tableModel = new DefaultTableModel(columns, 0);
        logTable = new JTable(tableModel);
        centerPanel.add(new JScrollPane(logTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        lblTotalEmissions = new JLabel("Net Carbon Footprint: 0.00 tCO2e");
        lblTotalEmissions.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(lblTotalEmissions);

        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addLogEntry());
        btnSave.addActionListener(e -> saveToFile());

        loadFromFile();
    }

    private void addLogEntry() {
        try {
            String activity = txtActivity.getText().trim();
            if (activity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an activity name.", "Input Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double co2 = Double.parseDouble(txtCo2.getText().trim());
            String category = (String) cbCategory.getSelectedItem();
            String id = "LOG-" + (dataManager.getLogList().size() + 1);

            CarbonLog log = new CarbonLog(id, activity, co2, category);
            dataManager.addCarbonLog(log);

            tableModel.addRow(new Object[]{
                id,
                activity,
                co2,
                category,
                log.isHighEmission() ? "YES" : "NO"
            });

            updateTotalDisplay();

            txtActivity.setText("");
            txtCo2.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for CO₂ amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile() {
        try {
            dataManager.saveDataToFile();
            JOptionPane.showMessageDialog(this, "All carbon logs saved to climate_logs.txt successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromFile() {
        try {
            dataManager.loadDataFromFile();
            for (CarbonLog log : dataManager.getLogList()) {
                tableModel.addRow(new Object[]{
                    log.getLogID(),
                    log.getActivityName(),
                    log.getCo2Amount(),
                    log.getCategory(),
                    log.isHighEmission() ? "YES" : "NO"
                });
            }
            updateTotalDisplay();
        } catch (IOException ex) {
            System.out.println("No existing log file found. Starting fresh.");
        }
    }

    private void updateTotalDisplay() {
        double total = dataManager.calculateTotalEmissions();
        lblTotalEmissions.setText(String.format("Net Carbon Footprint: %.2f tCO2e", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboardGUI().setVisible(true));
    }
}
