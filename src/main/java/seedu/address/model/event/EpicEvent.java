package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.Name;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.UniqueAttendanceList;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

//@@author william6364
/**
 * Represents a Event in the event planner.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class EpicEvent {

    private Name name;

    private UniqueTagList tags;
    private final UniqueAttendanceList attendanceList;

    /**
     * Every field must be present and not null.
     */
    public EpicEvent(Name name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.attendanceList = new UniqueAttendanceList();
    }

    //@@author jiangyue12392
    public EpicEvent(Name name, UniqueAttendanceList attendanceList, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.attendanceList = attendanceList;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    //@@author bayweiheng
    public EpicEvent(EpicEvent toBeCopied) {
        requireNonNull(toBeCopied);
        this.name = new Name(toBeCopied.getName().toString());
        this.tags = new UniqueTagList(toBeCopied.getTags());
        this.attendanceList = new UniqueAttendanceList();
    }

    //@@author william6364
    public Name getName() {
        return name;
    }

    //@@author raynoldng
    public static EpicEvent getDummyEpicEvent() {
        return new EpicEvent(new Name("dummyEvent"), new HashSet<Tag>());
    }

    //@@author bayweiheng
    /**
     * Edits this event by transferring the name and tags of the dummyEvent over
     */
    public void setEvent(EpicEvent dummyEvent) {
        this.name = dummyEvent.getName();
        this.tags = new UniqueTagList(dummyEvent.getTags());
        for (Attendance attendance : this.attendanceList.asObservableList()) {
            attendance.setAttendance(new Attendance(attendance.getPerson(), this, attendance.hasAttended()));
        }
    }

    /** registers person for this event */
    public void registerPerson(Person person) throws DuplicateAttendanceException {
        attendanceList.add(person, this);
    }

    /** deregisters person from this event */
    public void deregisterPerson(Person person) throws PersonNotFoundInEventException {
        try {
            attendanceList.remove(person, this);
        } catch (PersonNotFoundInEventException e) {
            throw new PersonNotFoundInEventException();
        }
    }

    //@@author jiangyue12392
    /** replace the person in the attendance list with the given person and the event in the attendance list*/
    public void replace(Person person) {
        attendanceList.replace(person, this);
    }

    //@@author william6364

    /** toggles the attendance of a person in this event */
    public void toggleAttendance(Person person) throws PersonNotFoundInEventException {
        try {
            attendanceList.toggleAttendance(person, this);
        } catch (PersonNotFoundInEventException e) {
            throw new PersonNotFoundInEventException();
        }
    }

    //@@author bayweiheng
    /**
     * Decrements all this event's attendanceList' numberOfEventsRegisteredFor.
     * Called only when this event is being deleted
     */
    public void handleDeleteEvent() {
        attendanceList.handleDeleteEvent();
    }


    /**
     * Increments all this event's attendanceList' numberOfEventsRegisteredFor.
     * Called only when this event is being added.
     * Required to properly maintain numberOfPersonsRegisteredFor for these persons
     * when an undo of a delete operation is called
     */
    public void handleAddEvent() {
        attendanceList.handleAddEvent();
    }

    /**
     * Sets the attendance list using another event's. Used for undoing deleteEvent
     */
    public void setAttendanceList(List<Attendance> dummyRegisteredPersons) {
        try {
            attendanceList.setAttendanceList(dummyRegisteredPersons);
        } catch (DuplicateAttendanceException e) {
            throw new AssertionError("this should not happen, dummyRegisteredPersons"
                    + "is a valid Attendance List from another event");
        }
    }

    //@@author raynoldng
    public ObservableList<Attendance> getAttendanceList() {
        return attendanceList.asObservableList();
    }

    public UniqueAttendanceList getUniqueAttendanceList() {
        return attendanceList;
    }

    //@@author bayweiheng
    /** returns true if person is in this event, regardless of whether
     * his attendance has been marked
     */
    public boolean hasPerson(Person person) {
        return attendanceList.contains(person, this);
    }

    //@@author william6364

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EpicEvent)) {
            return false;
        }

        EpicEvent otherEpicEvent = (EpicEvent) other;
        return otherEpicEvent.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
