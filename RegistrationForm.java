import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class RegistrationForm extends JDialog {
    private JTextField usernameField, nameField, ageField, emailField, phoneField;
    private JButton registerButton;
    private int eventId;
    private String userAccountUsername;
    private UserDashboard dashboard;

    public RegistrationForm(JFrame owner, int eventId, String userAccountUsername, UserDashboard dashboard) {
        super(owner, "Event Registration", true);
        this.eventId = eventId;
        this.userAccountUsername = userAccountUsername;
        this.dashboard = dashboard;
        initializeUI();
    }

    private void initializeUI() {
        setSize(300, 300);
        setLayout(new GridLayout(6, 2));

        usernameField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        registerButton = new JButton("Register");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Phone:"));
        add(phoneField);
        add(registerButton);

        registerButton.addActionListener(this::registerAction);
        setLocationRelativeTo(null);
    }

    private void registerAction(ActionEvent e) {
        if (registerUser()) {
            dashboard.refreshData();
            JOptionPane.showMessageDialog(this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private boolean registerUser() {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // Check if the event is already fully booked or user is already registered
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT slots FROM events WHERE id = ? FOR UPDATE");
            checkStmt.setInt(1, eventId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int slots = rs.getInt("slots");
                if (slots <= 0) {
                    JOptionPane.showMessageDialog(this, "This event is fully booked.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            PreparedStatement checkRegStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM registrations WHERE user_id = (SELECT id FROM users WHERE username = ?) AND event_id = ?");
            checkRegStmt.setString(1, userAccountUsername);
            checkRegStmt.setInt(2, eventId);
            ResultSet rsReg = checkRegStmt.executeQuery();
            if (rsReg.next() && rsReg.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "You are already registered for this event.", "Already Registered", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Register the user
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO registrations (event_id, user_id, registration_username, name, age, email, phone) VALUES (?, (SELECT id FROM users WHERE username = ?), ?, ?, ?, ?, ?)");
            insertStmt.setInt(1, eventId);
            insertStmt.setString(2, userAccountUsername);
            insertStmt.setString(3, usernameField.getText());
            insertStmt.setString(4, nameField.getText());
            insertStmt.setString(5, ageField.getText());
            insertStmt.setString(6, emailField.getText());
            insertStmt.setString(7, phoneField.getText());
            insertStmt.executeUpdate();

            // Decrement the available slots
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE events SET slots = slots - 1 WHERE id = ?");
            updateStmt.setInt(1, eventId);
            updateStmt.executeUpdate();

            conn.commit();  // Commit transaction
            return true;
        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();  // Rollback transaction on error
            } catch (SQLException exRollback) {
                JOptionPane.showMessageDialog(this, "Failed to rollback transaction.", "Transaction Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);  // Reset auto-commit to true
                    conn.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Failed to close the connection.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
