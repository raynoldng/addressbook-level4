package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEventPlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ATTENDANCE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ATTENDANCE;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_ATTENDANCE;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.ui.testutil.EventsCollectorRule;

//@@author william6364
/**
 * Contains integration tests (interaction with the Model) for {@code ToggleAttendanceCommand}.
 */
public class ToggleAttendanceCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
        model.setSelectedEpicEvent(INDEX_FIRST_EVENT.getZeroBased());
    }

    @Test
    public void execute_validIndex_success() {
        Index lastAttendanceIndex = Index.fromOneBased(model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().size());
        assertExecutionSuccess(INDEX_FIRST_ATTENDANCE);
        assertExecutionSuccess(INDEX_THIRD_ATTENDANCE);
        assertExecutionSuccess(lastAttendanceIndex);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().size() + 1);
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(outOfBoundsIndex);

        assertExecutionFailure(toggleAttendanceCommand, Messages.MESSAGE_INVALID_ATTENDANCE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidEvent_failure() {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(INDEX_FIRST_ATTENDANCE);
        try {
            toggleAttendanceCommand.preprocessUndoableCommand();
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        try {
            model.deleteEvent(model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()));
        } catch (EventNotFoundException e) {
            throw new AssertionError("Deleting of event should not fail");
        }
        try {
            toggleAttendanceCommand.executeUndoableCommand();
        } catch (CommandException ce) {
            assertEquals(Messages.MESSAGE_EVENT_NOT_FOUND, ce.getMessage());
        }
    }

    @Test
    public void execute_invalidPerson_failure() {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(INDEX_FIRST_ATTENDANCE);
        try {
            toggleAttendanceCommand.preprocessUndoableCommand();
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        try {
            model.deregisterPersonFromEvent(model.getSelectedEpicEvent().getEpicEvent().getAttendanceList()
                    .get(INDEX_FIRST_ATTENDANCE.getZeroBased()).getPerson(),
                    model.getSelectedEpicEvent().getEpicEvent());
        } catch (EventNotFoundException | PersonNotFoundInEventException e) {
            throw new AssertionError(
                    "Deregistering of person should not fail");
        }
        try {
            toggleAttendanceCommand.executeUndoableCommand();
        } catch (CommandException ce) {
            assertEquals(Messages.MESSAGE_PERSON_NOT_IN_EVENT, ce.getMessage());
        }
    }

    @Test
    public void equals() {
        ToggleAttendanceCommand toggleAttendanceCommandA = new ToggleAttendanceCommand(INDEX_FIRST_ATTENDANCE);
        ToggleAttendanceCommand toggleAttendanceCommandB = new ToggleAttendanceCommand(INDEX_SECOND_ATTENDANCE);

        // same object -> returns true
        assertTrue(toggleAttendanceCommandA.equals(toggleAttendanceCommandA));

        // same values -> returns true
        ToggleAttendanceCommand toggleAttendanceCommandCopy = new ToggleAttendanceCommand(INDEX_FIRST_ATTENDANCE);
        assertTrue(toggleAttendanceCommandA.equals(toggleAttendanceCommandCopy));

        // different types -> returns false
        assertFalse(toggleAttendanceCommandA.equals(1));

        // null -> returns false
        assertFalse(toggleAttendanceCommandA.equals(null));

        // different index -> returns false
        assertFalse(toggleAttendanceCommandA.equals(toggleAttendanceCommandB));
    }

    /**
     * Executes a {@code ToggleAttendanceCommand} with the given {@code index},
     * and checks that the attendance is correctly toggled
     */
    private void assertExecutionSuccess(Index index) {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(index);
        boolean initialHasAttended = model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().get(index.getZeroBased()).hasAttended();
        try {
            CommandResult commandResult = toggleAttendanceCommand.execute();
            assertEquals(String.format(ToggleAttendanceCommand.MESSAGE_SUCCESS,
                    toggleAttendanceCommand.getAttendanceToToggle().getPerson().getFullName(),
                    toggleAttendanceCommand.getAttendanceToToggle().getEvent().getName()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        // check if the correct attendance object was toggled
        assertTrue(toggleAttendanceCommand.getAttendanceToToggle().equals(model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().get(index.getZeroBased())));

        // check if the toggling occurred correctly
        assertTrue(initialHasAttended != model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().get(index.getZeroBased()).hasAttended());
    }

    /**
     * Executes a {@code ToggleAttendanceCommand} and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(ToggleAttendanceCommand toggleAttendanceCommand, String expectedMessage) {
        try {
            toggleAttendanceCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code ToggleAttendanceCommand} with parameters {@code index}.
     */
    private ToggleAttendanceCommand prepareCommand(Index index) {
        ToggleAttendanceCommand toggleAttendanceCommand = new ToggleAttendanceCommand(index);
        toggleAttendanceCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return toggleAttendanceCommand;
    }
}
