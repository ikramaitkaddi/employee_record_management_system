package com.erms.employee_management;

import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmployeeManagementUI extends JFrame {

    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeManagementUI() {
        super("Employee Management");

        // Set up the main JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new MigLayout("fill", "[grow]", "[grow][]")); // Main layout: Table and Controls

        // Create the table and scroll pane
        String[] columnNames = {"ID", "Full Name", "Job Title", "Department", "Hire Date", "Employment Status", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        JPanel tablePanel = new JPanel(new GridBagLayout()); //GridBagLayout for the table
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        tablePanel.add(scrollPane, gbc);

        // Create the control panel
        JPanel controlPanel = new JPanel(new MigLayout("wrap 2", "[][grow]")); //Label and Field/Button
        JLabel userRoleLabel = new JLabel("User Role:");
        JTextField userRoleField = new JTextField(15);
        JLabel userDepartmentLabel = new JLabel("Department:");
        JTextField userDepartmentField = new JTextField(15);
        JButton fetchButton = new JButton("Fetch Employees");

        controlPanel.add(userRoleLabel);
        controlPanel.add(userRoleField, "growx");
        controlPanel.add(userDepartmentLabel);
        controlPanel.add(userDepartmentField, "growx");
        controlPanel.add(fetchButton, "span, align center");

        // Add action listener to the fetch button
        fetchButton.addActionListener(e -> {
            String userRole = userRoleField.getText().trim();
            String userDepartment = userDepartmentField.getText().trim();
            if (!userRole.isEmpty()) {
                fetchEmployees(userRole, userDepartment);
            } else {
                JOptionPane.showMessageDialog(this, "User Role is required!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to the frame
        add(tablePanel, "grow"); // Table takes all available space
        add(controlPanel, "dock south"); // Control panel at the bottom

        // Make JFrame visible
        setVisible(true);
    }

    private void fetchEmployees(String userRole, String userDepartment) {
        try {
            // Build the API URL
            String apiUrl = "http://localhost:8080/api/employees?userRole=" + userRole;
            if (!userDepartment.isEmpty()) {
                apiUrl += "&userDepartment=" + userDepartment;
            }

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Handle response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                updateTable(response.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to fetch employees. Response code: " + responseCode);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateTable(String jsonResponse) {
        try {

            JSONArray employees = new JSONArray(jsonResponse);

            tableModel.setRowCount(0);

            for (int i = 0; i < employees.length(); i++) {
                JSONObject employee = employees.getJSONObject(i);
                Long id = employee.getLong("id");
                String fullName = employee.getString("fullName");
                String jobTitle = employee.getString("jobTitle");
                String department = employee.getString("department");
                String hireDate = employee.getString("hireDate");
                String employmentStatus = employee.getString("employmentStatus");
                String address = employee.getString("address");

                // Extract nested ContactInfo object
                JSONObject contactInfo = employee.getJSONObject("contactInfo");
                String phone = contactInfo.getString("phone");
                String email = contactInfo.getString("email");


                tableModel.addRow(new Object[]{id, fullName, jobTitle, department, hireDate, employmentStatus, phone, email, address});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error parsing employee data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementUI::new);
    }
}
