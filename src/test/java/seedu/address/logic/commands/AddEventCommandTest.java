package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEventPlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;

import java.util.TreeSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Name;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.EpicEvent;
import seedu.address.testutil.EpicEventBuilder;
import seedu.address.ui.testutil.EventsCollectorRule;

//@@author william6364

public class AddEventCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
        model.setSelectedEpicEvent(INDEX_FIRST_EVENT.getZeroBased());
    }

    /**
     * Executes a {@code AddEventCommand} with the given {@code index},
     * and checks that the event is correctly added
     */
    @Test
    public void execute_eventAcceptedByModel_addSuccessful() {
        EpicEvent event = new EpicEvent(new Name("New Event"), new TreeSet<>());
        AddEventCommand addEventCommand = prepareCommand(event);
        try {
            CommandResult commandResult = addEventCommand.execute();
            assertEquals(String.format(AddEventCommand.MESSAGE_SUCCESS, event), commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        assertTrue(model.getEventList().contains(event));
    }

    /**
     * Executes an {@code AddEventCommand} with a duplicate event and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    @Test
    public void execute_duplicateEvent_throwsCommandException() {
        AddEventCommand addEventCommand = prepareCommand(model.getEventList().get(0));
        try {
            addEventCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(AddEventCommand.MESSAGE_DUPLICATE_EVENT, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
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
     * Returns a {@code AddEventCommand} with parameters {@code event}.
     */
    private AddEventCommand prepareCommand(EpicEvent event) {
        AddEventCommand addEventCommand = new AddEventCommand(event);
        addEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addEventCommand;
    }
}
