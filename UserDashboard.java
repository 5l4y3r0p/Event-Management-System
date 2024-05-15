import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class UserDashboard extends JFrame {
    private JTable eventsTable, registeredEventsTable, notificationsTable;
    private DefaultTableModel eventsModel, registeredEventsModel, notificationsModel;
    private JButton logoutButton;
    private String username;

    public UserDashboard(String username) {
        super("User Dashboard - Event Management System");
        this.username = username;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setupUI();
    }

    private void setupUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        setupEventsTab(tabbedPane);
        setupRegisteredEventsTab(tabbedPane);
        setupNotificationsTab(tabbedPane);

        add(tabbedPane, BorderLayout.CENTER);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        });
        add(logoutButton, BorderLayout.SOUTH);

        refreshData();
    }

    private void setupEventsTab(JTabbedPane tabbedPane) {
        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsModel = new DefaultTableModel(new Object[]{"ID", "Name", "Remaining Slots", "Date", "Location"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventsTable = new JTable(eventsModel);
        eventsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = eventsTable.getSelectedRow();
                    if (row != -1) {
                        int eventId = (Integer) eventsModel.getValueAt(row, 0);
                        new RegistrationForm(UserDashboard.this, eventId, username, UserDashboard.this).setVisible(true);
                    }
                }
            }
        });
        eventsPanel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        tabbedPane.addTab("All Events", eventsPanel);
    }

    private void setupRegisteredEventsTab(JTabbedPane tabbedPane) {
        JPanel registeredEventsPanel = new JPanel(new BorderLayout());
        registeredEventsModel = new DefaultTableModel(new Object[]{"ID", "Name", "Date", "Location"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        registeredEventsTable = new JTable(registeredEventsModel);
        registeredEventsPanel.add(new JScrollPane(registeredEventsTable), BorderLayout.CENTER);
        tabbedPane.addTab("My Events", registeredEventsPanel);
    }

    private void setupNotificationsTab(JTabbedPane tabbedPane) {
        JPanel notificationsPanel = new JPanel(new BorderLayout());
        notificationsModel = new DefaultTableModel(new Object[]{"Event ID", "Event Name", "Message"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        notificationsTable = new JTable(notificationsModel);
        notificationsPanel.add(new JScrollPane(notificationsTable), BorderLayout.CENTER);
        tabbedPane.addTab("Notifications", notificationsPanel);
    }

    public void refreshData() {
        try {
            loadEvents();
            loadRegisteredEvents();
            loadNotifications();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to refresh data: " + e.getMessage(), "Refresh Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEvents() throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, name, slots, registration_deadline, location FROM events")) {
            ResultSet rs = stmt.executeQuery();
            eventsModel.setRowCount(0);
            while (rs.next()) {
                eventsModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("slots"),  // Shows remaining slots
                        rs.getDate("registration_deadline"),
                        rs.getString("location")
                });
            }
        }
    }

    private void loadRegisteredEvents() throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT r.event_id, e.name, e.registration_deadline, e.location " +
                             "FROM registrations r JOIN events e ON r.event_id = e.id " +
                             "WHERE r.user_id = (SELECT id FROM users WHERE username = ?)")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            registeredEventsModel.setRowCount(0);
            while (rs.next()) {
                registeredEventsModel.addRow(new Object[]{
                        rs.getInt("event_id"),
                        rs.getString("name"),
                        rs.getDate("registration_deadline"),
                        rs.getString("location")
                });
            }
        }
    }

    private void loadNotifications() throws SQLException {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT n.event_id, e.name, n.message " +
                             "FROM notifications n JOIN registrations r ON n.event_id = r.event_id " +
                             "JOIN events e ON e.id = n.event_id " +
                             "WHERE r.user_id = (SELECT id FROM users WHERE username = ?)")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            notificationsModel.setRowCount(0);
            while (rs.next()) {
                notificationsModel.addRow(new Object[]{
                        rs.getInt("event_id"),
                        rs.getString("name"),
                        rs.getString("message")
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard("SampleUser").setVisible(true));
    }
}
