package seedu.address.model.attendance;

import java.util.Objects;

import seedu.address.model.person.Person;

/**
 * Represents the attendance of a person to an event in the event planner.
 * Guarantees: person is immutable and not null
 */
public class Attendance {

    private Person attendee;
    private boolean hasAttendedEvent;

    /**
     * Person must be not be null
     * @param attendee
     */
    public Attendance(Person attendee) {
        Objects.requireNonNull(attendee);
        this.attendee = attendee;
        this.hasAttendedEvent = false;
    }

    /**
     * Person must be not be null
     * @param attendee
     * @param hasAttended
     */
    public Attendance(Person attendee, boolean hasAttended) {
        Objects.requireNonNull(attendee);
        this.attendee = attendee;
        this.hasAttendedEvent = hasAttended;
    }

    public Person getPerson() {
        Objects.requireNonNull(attendee);
        return attendee;
    }

    public boolean hasAttended() {
        return hasAttendedEvent;
    }

    /**
     * Edits this attendance by transferring the name and tags of the dummyAttendance over
     */
    public void setAttendance(Attendance dummyAttendance) {
        this.attendee = dummyAttendance.getPerson();
        this.hasAttendedEvent = dummyAttendance.hasAttended();
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
        return otherAttendance.getPerson().equals(this.getPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendee, hasAttendedEvent);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(attendee.getName())
                .append(" Attendance: ")
                .append(Boolean.toString(hasAttendedEvent));
        return builder.toString();
    }
}
