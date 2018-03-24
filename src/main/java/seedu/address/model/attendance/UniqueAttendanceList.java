package seedu.address.model.attendance;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;

/**
 * A list of attendance objects that enforces uniqueness between the persons inside the object and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Attendance#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueAttendanceList {

    private final ObservableList<Attendance> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        return contains(new Attendance(toCheck));
    }

    /**
     * Returns true if the list contains an equivalent attendee as the given argument.
     */
    public boolean contains(Attendance toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the attendance list.
     *
     * @throws DuplicateAttendanceException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Person toAdd) throws DuplicateAttendanceException {
        add(new Attendance(toAdd));
    }

    /**
     * Adds an attendance object to the list.
     *
     * @throws DuplicateAttendanceException if the attendee to add is a duplicate of an existing attendee in the list.
     */
    public void add(Attendance toAdd) throws DuplicateAttendanceException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAttendanceException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the attendee {@code target} in the list with {@code editedAttendance}.
     *
     * @throws DuplicateAttendanceException if the replacement is equivalent to another existing attendee in the list.
     * @throws PersonNotFoundInEventException if {@code target} could not be found in the list.
     */
    public void setAttendance(Attendance target, Attendance editedAttendance)
            throws DuplicateAttendanceException, PersonNotFoundInEventException {
        requireNonNull(editedAttendance);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundInEventException();
        }

        if (!target.equals(editedAttendance) && internalList.contains(editedAttendance)) {
            throw new DuplicateAttendanceException();
        }

        internalList.get(index).setAttendance(editedAttendance);
    }

    /**
     * Removes the equivalent attendee from the list.
     *
     * @throws PersonNotFoundInEventException if no such attendee could be found in the list.
     */
    public boolean remove(Person toRemove) throws PersonNotFoundInEventException {
        return remove(new Attendance(toRemove));
    }

    /**
     * Removes the equivalent attendee from the list.
     *
     * @throws PersonNotFoundInEventException if no such attendee could be found in the list.
     */
    public boolean remove(Attendance toRemove) throws PersonNotFoundInEventException {
        requireNonNull(toRemove);
        final boolean attendeeFoundAndDeleted = internalList.remove(toRemove);
        if (!attendeeFoundAndDeleted) {
            throw new PersonNotFoundInEventException();
        }
        return attendeeFoundAndDeleted;
    }

    public void setAttendanceList(UniqueAttendanceList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setAttendanceList(List<Attendance> attendanceList) throws DuplicateAttendanceException {
        requireAllNonNull(attendanceList);
        final UniqueAttendanceList replacement = new UniqueAttendanceList();
        for (final Attendance attendance : attendanceList) {
            replacement.add(attendance);
        }
        setAttendanceList(replacement);
    }

    /**
     * Decrements the numberOfEventsRegisteredFor of all Persons in this AttendanceList by 1.
     * Called only when the event this AttendanceList belongs to is being deleted
     */
    public void handleDeleteEvent() {
        for (Attendance attendance: internalList) {
            attendance.getAttendee().decrementNumberOfEventsRegisteredFor();
        }
    }

    /**
     * Decrements the numberOfEventsRegisteredFor of all Persons in this AttendanceList by 1.
     * Called only when the event this AttendanceList belongs to is being added.
    * Required to properly maintain numberOfPersonsRegisteredFor for these persons
     * when an undo of a delete operation is called
     */
    public void handleAddEvent() {
        for (Attendance attendance: internalList) {
            attendance.getAttendee().incrementNumberOfEventsRegisteredFor();
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Attendance> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueAttendanceList // instanceof handles nulls
                && this.internalList.equals(((UniqueAttendanceList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
