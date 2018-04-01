package seedu.address.model.attendance;

import java.util.Objects;

import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;

/**
 * Represents the attendance of a person to an event in the event planner.
 * Guarantees: person is immutable and not null
 */
public class Attendance {

    private Person attendee;
    private EpicEvent event;
    private boolean hasAttendedEvent;

    /**
     * Person must be not be null
     * @param attendee
     * @param event
     */
    public Attendance(Person attendee, EpicEvent event) {
        Objects.requireNonNull(attendee);
        this.attendee = attendee;
        this.event = event;
        this.hasAttendedEvent = false;
    }

    /**
     * Person must be not be null
     * @param attendee
     * @param event
     * @param hasAttended
     */
    public Attendance(Person attendee, EpicEvent event, boolean hasAttended) {
        Objects.requireNonNull(attendee);
        this.attendee = attendee;
        this.event = event;
        this.hasAttendedEvent = hasAttended;
    }

    public Person getPerson() {
        Objects.requireNonNull(attendee);
        return attendee;
    }

    public EpicEvent getEvent() {
        Objects.requireNonNull(event);
        return event;
    }

    public boolean hasAttended() {
        return hasAttendedEvent;
    }

    public void toggleAttendance() {
        this.hasAttendedEvent = !this.hasAttendedEvent;
    }

    /**
     * Edits this attendance by transferring the name and tags of the dummyAttendance over
     */
    public void setAttendance(Attendance dummyAttendance) {
        this.attendee = dummyAttendance.getPerson();
        this.event = dummyAttendance.getEvent();
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
        return otherAttendance.getPerson().equals(this.getPerson())
                && otherAttendance.getEvent().equals(this.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendee, event, hasAttendedEvent);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Person: ")
                .append(attendee.getName())
                .append(" Event: ")
                .append(event.getName())
                .append(" Attendance: ")
                .append(Boolean.toString(hasAttendedEvent));
        return builder.toString();
    }
}
