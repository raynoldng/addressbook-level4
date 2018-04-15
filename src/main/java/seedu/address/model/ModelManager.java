package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.model.EventPlannerChangedEvent;
import seedu.address.commons.events.ui.ClearEventListSelectionEvent;
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
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
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final EventPlanner eventPlanner;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<EpicEvent> filteredEvents;
    private final ObservableEpicEvent selectedEpicEvent;

    /**
     * Initializes a ModelManager with the given eventPlanner and userPrefs.
     */
    public ModelManager(ReadOnlyEventPlanner eventPlanner, UserPrefs userPrefs) {
        super();
        requireAllNonNull(eventPlanner, userPrefs);

        logger.fine("Initializing with event planner: " + eventPlanner + " and user prefs " + userPrefs);

        this.eventPlanner = new EventPlanner(eventPlanner);
        filteredPersons = new FilteredList<>(this.eventPlanner.getPersonList());
        filteredEvents = new FilteredList<>(this.eventPlanner.getEventList());
        if (filteredEvents.size() > 0) {
            selectedEpicEvent = new ObservableEpicEvent(filteredEvents.get(0));
        } else {
            selectedEpicEvent = new ObservableEpicEvent(EpicEvent.getDummyEpicEvent());
        }
        selectedEpicEvent.getFilteredAttendees().setPredicate(PREDICATE_SHOW_ALL_ATTENDEES);
    }

    public ModelManager() {
        this(new EventPlanner(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyEventPlanner newData) {
        eventPlanner.resetData(newData);
        indicateEventPlannerChanged();
    }

    @Override
    public ReadOnlyEventPlanner getEventPlanner() {
        return eventPlanner;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateEventPlannerChanged() {
        raise(new EventPlannerChangedEvent(eventPlanner));
    }

    @Override
    public synchronized void deletePerson(Person targetPerson) throws PersonNotFoundException {
        eventPlanner.removePerson(targetPerson);
        indicateEventPlannerChanged();
    }

    @Override
    public synchronized void addPerson(Person person) throws DuplicatePersonException {
        eventPlanner.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateEventPlannerChanged();
    }

    @Override
    public void updatePerson(Person targetPerson, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(targetPerson, editedPerson);

        eventPlanner.updatePerson(targetPerson, editedPerson);
        indicateEventPlannerChanged();
    }

    //@@author william6364

    //=========== Event Level Operations =====================================================================

    @Override
    public synchronized void addEvent(EpicEvent event) throws DuplicateEventException {
        eventPlanner.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        indicateEventPlannerChanged();
    }

    //@@author jiangyue12392
    @Override
    public synchronized void deleteEvent(EpicEvent targetEvent) throws EventNotFoundException {
        eventPlanner.removeEvent(targetEvent);
        indicateEventPlannerChanged();
    }

    //@@author bayweiheng
    @Override
    public void updateEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireAllNonNull(targetEvent, editedEvent);

        eventPlanner.updateEvent(targetEvent, editedEvent);
        indicateEventPlannerChanged();
    }

    //=========== Event-Person Interactions ==================================================================

    @Override
    public void registerPersonForEvent(Person person, EpicEvent event)
            throws EventNotFoundException, DuplicateAttendanceException {
        requireAllNonNull(person, event);

        eventPlanner.registerPersonForEvent(person, event);
        indicateEventPlannerChanged();
    }

    @Override
    public void deregisterPersonFromEvent(Person person, EpicEvent event)
            throws EventNotFoundException, PersonNotFoundInEventException {
        requireAllNonNull(person, event);

        eventPlanner.deregisterPersonFromEvent(person, event);
        indicateEventPlannerChanged();
    }

    //@@author william6364

    @Override
    public void toggleAttendance(Person person, EpicEvent event)
        throws EventNotFoundException, PersonNotFoundInEventException {
        requireAllNonNull(person, event);

        eventPlanner.toggleAttendance(person, event);
        indicateEventPlannerChanged();
    }

    //@@author

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code eventPlanner}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //@@author william6364
    //=========== Filtered Event List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the filtered list of {@code EpicEvent} backed by the internal list of
     * {@code eventPlanner}
     */
    @Override
    public ObservableList<EpicEvent> getFilteredEventList() {
        return FXCollections.unmodifiableObservableList(filteredEvents);
    }

    @Override
    public void updateFilteredEventList(Predicate<EpicEvent> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    //@@author raynoldng
    @Override
    public void setSelectedEpicEvent(int index) {
        selectedEpicEvent.setEpicEvent(filteredEvents.get(index));
    }

    @Override
    public void setSelectedEpicEvent(EpicEvent epicEvent) {
        selectedEpicEvent.setEpicEvent(epicEvent);
    }

    @Override
    public void updateFilteredAttendanceList(Predicate<Attendance> predicate) {
        requireNonNull(predicate);
        selectedEpicEvent.getFilteredAttendees().setPredicate(predicate);
    }

    @Override
    public void visuallySelectEpicEvent(EpicEvent toSelect) {
        setSelectedEpicEvent(toSelect);
        int eventIndexInFilteredList = getFilteredEventList().indexOf(toSelect);
        if (eventIndexInFilteredList != -1) {
            EventsCenter.getInstance().post(new JumpToEventListRequestEvent(
                    Index.fromZeroBased(eventIndexInFilteredList)));
        } else {
            EventsCenter.getInstance().post(new ClearEventListSelectionEvent());
        }
    }

    @Override
    public void clearSelectedEpicEvent() {
        visuallySelectEpicEvent(EpicEvent.getDummyEpicEvent());
        EventsCenter.getInstance().post(new ClearEventListSelectionEvent());
    }

    @Override
    public ObservableEpicEvent getSelectedEpicEvent() {
        return selectedEpicEvent;
    }

    //@@author bayweiheng
    /**
     * Returns an unmodifiable view of the list of {@code EpicEvent} backed by the internal list of
     * {@code eventPlanner}
     */
    @Override
    public ObservableList<EpicEvent> getEventList() {
        return FXCollections.unmodifiableObservableList(eventPlanner.getEventList());
    }

    //@@author

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return eventPlanner.equals(other.eventPlanner)
                && filteredPersons.equals(other.filteredPersons)
                && filteredEvents.equals(other.filteredEvents);
    }

}
