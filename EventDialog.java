import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;

public class EventDialog extends JDialog {
    private JTextField nameField;
    private JTextField slotsField;
    private JFormattedTextField registrationDeadlineField;
    private JTextField locationField;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean saved = false;
    private Event event;

    public EventDialog(Frame owner, String title, boolean modal) {
        this(owner, title, modal, null);
    }

    public EventDialog(Frame owner, String title, boolean modal, Event event) {
        super(owner, title, modal);
        this.event = event;
        setSize(400, 300);
        setLayout(new GridLayout(5, 2, 10, 10));

        nameField = new JTextField();
        slotsField = new JTextField();
        registrationDeadlineField = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd")));
        locationField = new JTextField();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Slots:"));
        add(slotsField);
        add(new JLabel("Registration Deadline:"));
        add(registrationDeadlineField);
        add(new JLabel("Location:"));
        add(locationField);
        add(saveButton);
        add(cancelButton);

        if (event != null) {
            nameField.setText(event.getName());
            slotsField.setText(String.valueOf(event.getSlots()));
            registrationDeadlineField.setText(new SimpleDateFormat("yyyy-MM-dd").format(event.getRegistrationDeadline()));
            locationField.setText(event.getLocation());
        }

        saveButton.addActionListener(this::saveEvent);
        cancelButton.addActionListener(e -> dispose());

        setLocationRelativeTo(owner);
    }

    private void saveEvent(ActionEvent e) {
        try {
            String name = nameField.getText();
            int slots = Integer.parseInt(slotsField.getText());
            Date registrationDeadline = new SimpleDateFormat("yyyy-MM-dd").parse(registrationDeadlineField.getText());
            String location = locationField.getText();
            if (event == null) {
                event = new Event(-1, name, slots, registrationDeadline, location);
            } else {
                event.setName(name);
                event.setSlots(slots);
                event.setRegistrationDeadline(registrationDeadline);
                event.setLocation(location);
            }
            saved = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please correct and try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Event getEvent() {
        return event;
    }
}
