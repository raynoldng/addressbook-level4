package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEventPlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ATTENDANCE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ATTENDANCE;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

public class ToggleAttendanceCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
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
        boolean initialHasAttended = toggleAttendanceCommand.getAttendanceToToggle().hasAttended();
        try {
            CommandResult commandResult = toggleAttendanceCommand.execute();
            assertEquals(String.format(ToggleAttendanceCommand.MESSAGE_SUCCESS,
                    toggleAttendanceCommand.getAttendanceToToggle().getPerson().getName(),
                    toggleAttendanceCommand.getAttendanceToToggle().getEvent().getName()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        assertTrue(initialHasAttended != toggleAttendanceCommand.getAttendanceToToggle().hasAttended());
    }

    /**
     * Executes a {@code ToggleAttendanceCommand} with the given {@code index},
     * and checks that a {@code CommandException} is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(index);

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
