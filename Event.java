import java.util.Date;

public class Event {
    private int id;
    private String name;
    private int slots;
    private Date registrationDeadline;
    private String location;

    public Event(int id, String name, int slots, Date registrationDeadline, String location) {
        this.id = id;
        this.name = name;
        this.slots = slots;
        this.registrationDeadline = registrationDeadline;
        this.location = location;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSlots() {
        return slots;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public String getLocation() {
        return location;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
