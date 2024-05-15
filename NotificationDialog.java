import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class NotificationDialog extends JDialog {
    private JTextArea messageArea;
    private JButton sendButton;
    private int eventId;

    public NotificationDialog(Frame owner, int eventId) {
        super(owner, "Send Notification", true);
        this.eventId = eventId;
        setSize(400, 300);
        setLayout(new BorderLayout());

        messageArea = new JTextArea();
        sendButton = new JButton("Send");

        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        add(sendButton, BorderLayout.SOUTH);

        sendButton.addActionListener(this::sendNotification);
    }

    private void sendNotification(ActionEvent e) {
        String message = messageArea.getText();
        if (!message.isEmpty()) {
            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO notifications (event_id, message) VALUES (?, ?)")) {
                stmt.setInt(1, eventId);
                stmt.setString(2, message);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Notification sent successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
