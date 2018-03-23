package seedu.address.logic.commands;

import static org.junit.Assert.fail;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEventPlanner;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {

    @Override
    public void resetData(ReadOnlyEventPlanner newData) {
        fail("This method should not be called.");
    }

    @Override
    public ReadOnlyEventPlanner getEventPlanner() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void addEvent(EpicEvent event) throws DuplicateEventException {
        fail("This method should not be called.");
    }

    @Override
    public void updateEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException {
        fail("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) throws DuplicatePersonException {
        fail("This method should not be called.");
    }

    @Override
    public void deleteEvent(EpicEvent targetEvent) throws EventNotFoundException {
        fail("This method should not be called.");
    }

    @Override
    public void registerPersonForEvent(Person person, EpicEvent event) throws EventNotFoundException,
            DuplicateAttendanceException {
        fail("This method should not be called.");
    }

    @Override
    public void deregisterPersonFromEvent(Person person, EpicEvent event) throws EventNotFoundException,
            PersonNotFoundInEventException {
        fail("This method should not be called.");
    }

    @Override
    public void deletePerson(Person targetPerson) throws PersonNotFoundException {
        fail("This method should not be called.");
    }

    @Override
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException {
        fail("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        fail("This method should not be called.");
    }

    @Override
    public ObservableList<EpicEvent> getFilteredEventList() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public ObservableList<EpicEvent> getEventList() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void updateFilteredEventList(Predicate<EpicEvent> predicate) {
        fail("This method should not be called.");
    }
}
