import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RegistrantListDialog extends JDialog {
    private JTable registrantsTable;
    private int eventId;

    public RegistrantListDialog(Frame owner, int eventId) {
        super(owner, "Registrants", true);
        this.eventId = eventId;
        setSize(400, 300);
        setLayout(new BorderLayout());

        registrantsTable = new JTable(new DefaultTableModel(new Object[]{"User ID", "Username"}, 0));
        add(new JScrollPane(registrantsTable), BorderLayout.CENTER);

        loadRegistrants();
    }

    private void loadRegistrants() {
        DefaultTableModel model = (DefaultTableModel) registrantsTable.getModel();
        model.setRowCount(0);

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT users.id, users.username FROM registrations JOIN users ON users.id = registrations.user_id WHERE event_id = ?")) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("username")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
