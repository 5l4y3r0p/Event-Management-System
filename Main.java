import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set the look and feel to the system default to make the GUI integrate better with the operating system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Ensure the application starts on the Event Dispatch Thread (EDT) because Swing components are not thread-safe
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 300);
            loginFrame.setResizable(false);
            loginFrame.setLocationRelativeTo(null); // Center the login window on the screen
            loginFrame.setVisible(true);
        });
    }
}
