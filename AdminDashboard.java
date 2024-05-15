import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class AdminDashboard extends JFrame {
    private JTable eventsTable;
    private JButton addButton, editButton, deleteButton, viewRegistrantsButton, sendNotificationButton, logoutButton;
    private DefaultTableModel model;

    public AdminDashboard() {
        super("Admin Dashboard - Event Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Slots", "Registration Deadline", "Location"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // prevent cell editing
                return false;
            }
        };
        eventsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Event");
        editButton = new JButton("Edit Event");
        deleteButton = new JButton("Delete Event");
        viewRegistrantsButton = new JButton("View Registrants");
        sendNotificationButton = new JButton("Send Notification");
        logoutButton = new JButton("Logout");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewRegistrantsButton);
        buttonPanel.add(sendNotificationButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::addEvent);
        editButton.addActionListener(this::editEvent);
        deleteButton.addActionListener(this::deleteEvent);
        viewRegistrantsButton.addActionListener(this::viewRegistrants);
        sendNotificationButton.addActionListener(this::sendNotification);
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        loadEvents();
    }

    private void addEvent(ActionEvent e) {
        EventDialog dialog = new EventDialog(this, "Add Event", true);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            Event event = dialog.getEvent();
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO events (name, slots, registration_deadline, location) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, event.getName());
                stmt.setInt(2, event.getSlots());
                stmt.setDate(3, new java.sql.Date(event.getRegistrationDeadline().getTime()));
                stmt.setString(4, event.getLocation());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    loadEvents(); // Reload data to include the new event
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void editEvent(ActionEvent e) {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) model.getValueAt(selectedRow, 0);
            Event event = getEvent(eventId);
            if (event != null) {
                EventDialog dialog = new EventDialog(this, "Edit Event", true, event);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    updateEvent(event);
                }
            }
        }
    }

    private void deleteEvent(ActionEvent e) {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteEvent(eventId);
            }
        }
    }

    private void viewRegistrants(ActionEvent e) {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) model.getValueAt(selectedRow, 0);
            new RegistrantListDialog(this, eventId).setVisible(true);
        }
    }

    private void sendNotification(ActionEvent e) {
        int selectedRow = eventsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) model.getValueAt(selectedRow, 0);
            new NotificationDialog(this, eventId).setVisible(true);
        }
    }

    private void loadEvents() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM events");
             ResultSet rs = stmt.executeQuery()) {
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("slots"),
                        new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("registration_deadline")),
                        rs.getString("location")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateEvent(Event event) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE events SET name=?, slots=?, registration_deadline=?, location=? WHERE id=?")) {
            stmt.setString(1, event.getName());
            stmt.setInt(2, event.getSlots());
            stmt.setDate(3, new java.sql.Date(event.getRegistrationDeadline().getTime()));
            stmt.setString(4, event.getLocation());
            stmt.setInt(5, event.getId());
            stmt.executeUpdate();
            loadEvents(); // Reload data to reflect changes
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteEvent(int eventId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM events WHERE id=?")) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            loadEvents(); // Reload data to remove the deleted event
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Event getEvent(int eventId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM events WHERE id=?")) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("slots"),
                        rs.getDate("registration_deadline"),
                        rs.getString("location")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
