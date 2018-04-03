package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;

//@@author william6364
/**
 * A list of events that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see EpicEvent#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueEpicEventList {

    private final ObservableList<EpicEvent> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(EpicEvent toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an event to the list.
     *
     * @throws DuplicateEventException if the event to add is a duplicate of an existing event in the list.
     */
    public void add(EpicEvent toAdd) throws DuplicateEventException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(toAdd);
    }

    //@@author jiangyue12392
    /**
     * Removes the equivalent event from the list.
     *
     * @throws EventNotFoundException if no such event could be found in the list.
     */
    public boolean remove(EpicEvent eventToRemove) throws EventNotFoundException {
        requireNonNull(eventToRemove);
        final boolean eventFoundAndDeleted = internalList.remove(eventToRemove);
        if (!eventFoundAndDeleted) {
            throw new EventNotFoundException();
        }
        return eventFoundAndDeleted;
    }

    //@@author bayweiheng
    /**
     * Registers the person to the event. Updates the person's numberOfEventsRegisteredFor
     * upon success.
     *
     * @throws DuplicateAttendanceException if the person is already registered
     * @throws EventNotFoundException if no such event could be found in the list
     */
    public void registerPersonForEvent(Person person, EpicEvent eventToRegisterFor)
            throws DuplicateAttendanceException, EventNotFoundException {
        requireAllNonNull(person, eventToRegisterFor);

        int index = internalList.indexOf(eventToRegisterFor);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        eventToRegisterFor.registerPerson(person);
        person.incrementNumberOfEventsRegisteredFor();
    }

    /**
     * Deregisters the person to the event. Updates the person's numberOfEventsRegisteredFor
     * upon success.
     *
     * @throws PersonNotFoundInEventException if person could not be found in event
     * @throws EventNotFoundException if no such event could be found in the list
     */
    public void deregisterPersonFromEvent(Person person, EpicEvent eventToRegisterFor)
            throws PersonNotFoundInEventException, EventNotFoundException {
        requireAllNonNull(person, eventToRegisterFor);

        int index = internalList.indexOf(eventToRegisterFor);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        eventToRegisterFor.deregisterPerson(person);
        person.decrementNumberOfEventsRegisteredFor();
    }

    //@@author william6364
    /**
     * Toggles the attendance of the person in the event.
     *
     * @throws PersonNotFoundInEventException if person could not be found in event
     * @throws EventNotFoundException if no such event could be found in the list
     */
    public void toggleAttendance(Person person, EpicEvent eventToToggleAttendance)
            throws PersonNotFoundInEventException, EventNotFoundException {
        requireAllNonNull(person, eventToToggleAttendance);

        int index = internalList.indexOf(eventToToggleAttendance);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        eventToToggleAttendance.toggleAttendance(person);
    }

    //@@author jiangyue12392
    /**
     * Replaces the event {@code targetEvent} in the list with {@code editedEvent}.
     *
     * @throws DuplicateEventException if the replacement is equivalent to another existing event in the list.
     * @throws EventNotFoundException if {@code targetEvent} could not be found in the list.
     */
    public void setEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedEvent);

        int index = internalList.indexOf(targetEvent);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        if (!targetEvent.equals(editedEvent) && internalList.contains(editedEvent)) {
            throw new DuplicateEventException();
        }

        internalList.get(index).setEvent(editedEvent);

        // Forces UI to refresh
        internalList.set(index, internalList.get(index));
    }

    public void setEvents(UniqueEpicEventList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setEvents(List<EpicEvent> events) throws DuplicateEventException {
        requireAllNonNull(events);
        final UniqueEpicEventList replacement = new UniqueEpicEventList();
        for (final EpicEvent event : events) {
            replacement.add(event);
        }
        setEvents(replacement);
    }

    //@@author william6364
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEpicEventList // instanceof handles nulls
                && this.internalList.equals(((UniqueEpicEventList) other).internalList));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<EpicEvent> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }
    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
