package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.ObservableEpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true. */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that always evaluate to true. */
    Predicate<EpicEvent> PREDICATE_SHOW_ALL_EVENTS = unused -> true;

    /** {@code Predicate} that always evaluate to true. */
    Predicate<Attendance> PREDICATE_SHOW_ALL_ATTENDEES = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyEventPlanner newData);

    /** Returns the EventPlanne r. */
    ReadOnlyEventPlanner getEventPlanner();

    /** Deletes the given person. */
    void deletePerson(Person target) throws PersonNotFoundException;

    /** Adds the given person. */
    void addPerson(Person person) throws DuplicatePersonException;

    /**
     * Replaces the given person {@code targetPerson} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code targetPerson} could not be found in the list.
     */
    void updatePerson(Person targetPerson, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException;

    /** Returns an unmodifiable view of the filtered person list. */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /** Adds the given event. */
    void addEvent(EpicEvent event) throws DuplicateEventException;

    /** Deletes the given event. */
    void deleteEvent(EpicEvent targetEvent) throws EventNotFoundException;

    /**
     * Registers the given person {@code person} for the given event {@code event}
     *
     * @throws PersonNotFoundException if the person could not be found in the list
     * @throws EventNotFoundException if the event could not be found in the list
     * @throws DuplicatePersonException if the person is already registered for the event
     */
    void registerPersonForEvent(Person person, EpicEvent event)
            throws EventNotFoundException, DuplicateAttendanceException;

    /**
     * Deregisters the given person {@code person} from the given event {@code event}
     *
     * @throws PersonNotFoundException if the person could not be found in the list
     * @throws EventNotFoundException if the event could not be found in the list
     * @throws PersonNotFoundInEventException if the person could not be found in the event
     */
    void deregisterPersonFromEvent(Person person, EpicEvent event)
            throws EventNotFoundException, PersonNotFoundInEventException;

    /**
     * Toggles the attendance of the given person {@code person} in the given event {@code event}
     *
     * @throws PersonNotFoundException if the person could not be found in the list
     * @throws EventNotFoundException if the event could not be found in the list
     * @throws PersonNotFoundInEventException if the person could not be found in the event
     */
    void toggleAttendance(Person person, EpicEvent event)
            throws EventNotFoundException, PersonNotFoundInEventException;

    /**
     * Replaces the given event {@code targetEvent} with {@code editedEvent}.
     *
     * @throws DuplicateEventException if updating the event's details causes the event to be equivalent to
     *      another existing event in the list.
     * @throws EventNotFoundException if {@code targetEvnt} could not be found in the list.
     */
    void updateEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException;

    /** Returns an unmodifiable view of the filtered event list. */
    ObservableList<EpicEvent> getFilteredEventList();

    /** Returns an unmodifiable view of the event list. */
    ObservableList<EpicEvent> getEventList();

    /**
     * Updates the filter of the filtered event list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<EpicEvent> predicate);

    // @@author raynoldng
    ObservableEpicEvent getSelectedEpicEvent();

    void setSelectedEpicEvent(int index);

    void setSelectedEpicEvent(EpicEvent epicEvent);

    void updateFilteredAttendanceList(Predicate<Attendance> predicate);

    void visuallySelectEpicEvent(EpicEvent toAdd);

    void clearSelectedEpicEvent();
    // @@author
}
