package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.EventPlannerChangedEvent;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
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
    public synchronized void deletePerson(Person target) throws PersonNotFoundException {
        eventPlanner.removePerson(target);
        indicateEventPlannerChanged();
    }

    @Override
    public synchronized void addPerson(Person person) throws DuplicatePersonException {
        eventPlanner.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateEventPlannerChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        eventPlanner.updatePerson(target, editedPerson);
        indicateEventPlannerChanged();
    }

    @Override
    public synchronized void addEvent(EpicEvent event) throws DuplicateEventException {

    }

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
                && filteredPersons.equals(other.filteredPersons);
    }

}
