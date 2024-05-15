import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class EventTableModel extends DefaultTableModel {
    private static final String[] columnNames = {"ID", "Name", "Slots", "Registration Deadline", "Location"};

    public EventTableModel() {
        super(columnNames, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // Make table cells non-editable
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:  // ID
                return Integer.class;
            case 1:  // Name
                return String.class;
            case 2:  // Slots
                return Integer.class;
            case 3:  // Registration Deadline
                return String.class;  // You can use Date.class if you configure your cell renderer accordingly
            case 4:  // Location
                return String.class;
            default:
                return Object.class;
        }
    }

    // Method to add an event to the table
    public void addEvent(Event event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        addRow(new Object[]{
                event.getId(),
                event.getName(),
                event.getSlots(),
                sdf.format(event.getRegistrationDeadline()),
                event.getLocation()
        });
    }

    // Method to update an event in the table
    public void updateEvent(int row, Event event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        setValueAt(event.getId(), row, 0);
        setValueAt(event.getName(), row, 1);
        setValueAt(event.getSlots(), row, 2);
        setValueAt(sdf.format(event.getRegistrationDeadline()), row, 3);
        setValueAt(event.getLocation(), row, 4);
        fireTableRowsUpdated(row, row);
    }

    // Method to remove an event from the table
    public void removeEvent(int row) {
        removeRow(row);
    }

    // Method to get Event at specific row
    public Event getEventAt(int row) {
        if (row >= 0 && row < getRowCount()) {
            int id = (Integer) getValueAt(row, 0);
            String name = (String) getValueAt(row, 1);
            int slots = (Integer) getValueAt(row, 2);
            String dateStr = (String) getValueAt(row, 3);
            String location = (String) getValueAt(row, 4);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return new Event(id, name, slots, sdf.parse(dateStr), location);
            } catch (Exception e) {
                e.printStackTrace();  // Exception handling for date parsing
            }
        }
        return null;
    }
}
