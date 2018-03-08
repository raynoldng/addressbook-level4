package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EventPlanner;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEventPlanner;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.EpicEventBuilder;

public class AddEventCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullEvent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddEventCommand(null);
    }

    @Test
    public void execute_eventAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingEventAdded modelStub = new ModelStubAcceptingEventAdded();
        EpicEvent validEvent = new EpicEventBuilder().build();

        CommandResult commandResult = getAddEventCommandForEpicEvent(validEvent, modelStub).execute();

        assertEquals(String.format(AddEventCommand.MESSAGE_SUCCESS, validEvent), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validEvent), modelStub.eventsAdded);
    }

    @Test
    public void execute_duplicateEvent_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateEventException();
        EpicEvent validEvent = new EpicEventBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddEventCommand.MESSAGE_DUPLICATE_EVENT);

        getAddEventCommandForEpicEvent(validEvent, modelStub).execute();
    }

    @Test
    public void equals() {
        EpicEvent eventA = new EpicEventBuilder().withName("Event A").build();
        EpicEvent eventB = new EpicEventBuilder().withName("Event B").build();
        AddEventCommand addEventACommand = new AddEventCommand(eventA);
        AddEventCommand addEventBCommand = new AddEventCommand(eventB);

        // same object -> returns true
        assertTrue(addEventACommand.equals(addEventACommand));

        // same values -> returns true
        AddEventCommand addEventACommandCopy = new AddEventCommand(eventA);
        assertTrue(addEventACommand.equals(addEventACommandCopy));

        // different types -> returns false
        assertFalse(addEventACommand.equals(1));

        // null -> returns false
        assertFalse(addEventACommand.equals(null));

        // different person -> returns false
        assertFalse(addEventACommand.equals(addEventBCommand));
    }

    /**
     * Generates a new AddEventCommand with the details of the given event.
     */
    private AddEventCommand getAddEventCommandForEpicEvent(EpicEvent event, Model model) {
        AddEventCommand command = new AddEventCommand(event);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void addEvent(EpicEvent event) throws DuplicateEventException {
            fail("This method should not be called.");
        }

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
        public void deletePerson(Person target) throws PersonNotFoundException {
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
        public void updateFilteredEventList(Predicate<EpicEvent> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateEventException when trying to add a event.
     */
    private class ModelStubThrowingDuplicateEventException extends ModelStub {
        @Override
        public void addEvent(EpicEvent person) throws DuplicateEventException {
            throw new DuplicateEventException();
        }

        @Override
        public ReadOnlyEventPlanner getEventPlanner() {
            return new EventPlanner();
        }
    }

    /**
     * A Model stub that always accept the event being added.
     */
    private class ModelStubAcceptingEventAdded extends ModelStub {
        final ArrayList<EpicEvent> eventsAdded = new ArrayList<>();

        @Override
        public void addEvent(EpicEvent event) throws DuplicateEventException {
            requireNonNull(event);
            eventsAdded.add(event);
        }

        @Override
        public ReadOnlyEventPlanner getEventPlanner() {
            return new EventPlanner();
        }
    }
}
