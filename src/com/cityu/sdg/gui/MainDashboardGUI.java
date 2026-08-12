package com.cityu.sdg.gui;

import com.cityu.sdg.model.CarbonLog;
import com.cityu.sdg.service.DataManager;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainDashboardGUI extends JFrame {
    private final DataManager dataManager;
    private final JTable logTable;
    private final DefaultTableModel tableModel;
    private final JTextField txtActivity;
    private final JTextField txtCo2;
    private final JComboBox<String> cbCategory;
    private final JLabel lblTotalEmissions;
    private final JButton btnAdd;
    private final JButton btnUpdate;
    private final JButton btnDelete;
    private final JButton btnSave;
    private int selectedRowIndex = -1;

    public MainDashboardGUI() {
        dataManager = new DataManager();

        setTitle("SDG 13: Carbon Footprint & Climate Action Tracker (Audited)");
        setSize(920, 670);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        JLabel titleLabel = new JLabel("SDG 13 — Climate Action: Carbon Emission & Offset Tracker");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formContainer = new JPanel(new BorderLayout(5, 5));
        formContainer.setBorder(BorderFactory.createTitledBorder("Manage Activity (Validated & Audited)"));

        JPanel formFieldsPanel = new JPanel(new GridLayout(3, 2, 8, 8));

        formFieldsPanel.add(new JLabel("Activity Name / Cert ID (Required for Offset):"));
        txtActivity = new JTextField();
        formFieldsPanel.add(txtActivity);

        formFieldsPanel.add(new JLabel("CO₂ Amount (Metric Tonnes - tCO2e):"));
        txtCo2 = new JTextField();
        formFieldsPanel.add(txtCo2);

        formFieldsPanel.add(new JLabel("Category:"));
        String[] categories = {"Energy", "Transportation", "Manufacturing", "Offset"};
        cbCategory = new JComboBox<>(categories);
        formFieldsPanel.add(cbCategory);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("Add Log");
        btnUpdate = new JButton("Update Selected Log");
        btnDelete = new JButton("Delete Selected Log");
        btnSave = new JButton("Save Data to File");

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSave);

        formContainer.add(formFieldsPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.add(formContainer, BorderLayout.NORTH);

        String[] columns = {"Log ID", "Activity Description", "CO₂ Amount (tCO2e)", "Category", "High Impact?"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logTable = new JTable(tableModel);
        logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        centerPanel.add(new JScrollPane(logTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        lblTotalEmissions = new JLabel("Net Carbon Footprint: 0.00 tCO2e");
        lblTotalEmissions.setFont(new Font("SansSerif", Font.BOLD, 15));
        bottomPanel.add(lblTotalEmissions);

        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addLogEntry());
        btnUpdate.addActionListener(e -> updateSelectedLog());
        btnDelete.addActionListener(e -> deleteSelectedLog());
        btnSave.addActionListener(e -> saveToFile());

        logTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && logTable.getSelectedRow() != -1) {
                selectedRowIndex = logTable.getSelectedRow();
                populateFieldsFromSelectedRow(selectedRowIndex);
            }
        });

        loadFromFile();
    }

    private boolean validateOffsetRules(String category, String activity) {
        if ("Offset".equalsIgnoreCase(category)) {
            boolean hasAuditRef = activity.toLowerCase().contains("cert") ||
                                 activity.toLowerCase().contains("ref:") ||
                                 activity.toLowerCase().contains("id:") ||
                                 activity.toLowerCase().contains("credit");

            if (!hasAuditRef) {
                JOptionPane.showMessageDialog(
                    this,
                    "Validation Rule Triggered:\n" +
                    "To prevent anti-greenwashing/fraudulent offsets, the activity description\n" +
                    "MUST include a Certificate ID or Reference (e.g., 'Tree Planting (Cert: #10423)').",
                    "Offset Anti-Greenwashing Warning",
                    JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Notice: Submitting a Carbon Offset reduces net emissions.\n" +
                "Are you sure this offset is verified by an accredited agency?",
                "Audit Pre-Check",
                JOptionPane.YES_NO_OPTION
            );
            return choice == JOptionPane.YES_OPTION;
        }
        return true;
    }

    private void populateFieldsFromSelectedRow(int rowIndex) {
        String activity = (String) tableModel.getValueAt(rowIndex, 1);
        double co2 = (Double) tableModel.getValueAt(rowIndex, 2);
        String category = (String) tableModel.getValueAt(rowIndex, 3);

        txtActivity.setText(activity);
        txtCo2.setText(String.valueOf(co2));
        cbCategory.setSelectedItem(category);

        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
    }

    private void addLogEntry() {
        try {
            String activity = txtActivity.getText().trim();
            if (activity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an activity description.", "Input Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double co2 = Double.parseDouble(txtCo2.getText().trim());
            String category = (String) cbCategory.getSelectedItem();

            if (!validateOffsetRules(category, activity)) {
                return;
            }

            String id = dataManager.generateNextLogID();
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
            clearInputsAndSelection();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for CO₂ amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelectedLog() {
        if (selectedRowIndex == -1) return;

        try {
            String activity = txtActivity.getText().trim();
            if (activity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an activity description.", "Input Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double co2 = Double.parseDouble(txtCo2.getText().trim());
            String category = (String) cbCategory.getSelectedItem();

            if (!validateOffsetRules(category, activity)) {
                return;
            }

            dataManager.updateCarbonLog(selectedRowIndex, activity, co2, category);
            CarbonLog updatedLog = dataManager.getLogList().get(selectedRowIndex);

            tableModel.setValueAt(activity, selectedRowIndex, 1);
            tableModel.setValueAt(co2, selectedRowIndex, 2);
            tableModel.setValueAt(category, selectedRowIndex, 3);
            tableModel.setValueAt(updatedLog.isHighEmission() ? "YES" : "NO", selectedRowIndex, 4);

            updateTotalDisplay();
            clearInputsAndSelection();
            JOptionPane.showMessageDialog(this, "Log entry updated and logged to audit trail!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for CO₂ amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedLog() {
        if (selectedRowIndex == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this log entry? This action will be logged in the audit trail.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dataManager.removeCarbonLog(selectedRowIndex);
            tableModel.removeRow(selectedRowIndex);

            updateTotalDisplay();
            clearInputsAndSelection();
        }
    }

    private void clearInputsAndSelection() {
        txtActivity.setText("");
        txtCo2.setText("");
        cbCategory.setSelectedIndex(0);
        logTable.clearSelection();
        selectedRowIndex = -1;
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
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
            tableModel.setRowCount(0);
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
            System.out.println("No existing log file found. Starting fresh session.");
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
