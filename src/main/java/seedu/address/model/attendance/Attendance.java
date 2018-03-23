package seedu.address.model.attendance;

import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * Represents the attendance of a person to an event in the event planner.
 * Guarantees: person is immutable and not null
 */
public class Attendance {

    private final Person attendee;
    private boolean attended;

    /**
     * Person must be not be null
     * @param attendee
     */
    public Attendance(Person attendee) {
        Objects.requireNonNull(attendee);
        this.attendee = attendee;
        attended = false;
    }

    public Person getAttendee() {
        return attendee;
    }

    public boolean hasAttended() {
        return attended;
    }

    /**
     * Sets the attendance for this attendee
     */
    public void setAttendance(boolean attended) {
        this.attended = attended;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Attendance)) {
            return false;
        }

        Attendance otherAttendance = (Attendance) other;
        return otherAttendance.getAttendee().equals(this.getAttendee());
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendee, attended);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(attendee.getName())
                .append(" Attendance: ");
                Boolean.toString(attended);
        return builder.toString();
    }
}
