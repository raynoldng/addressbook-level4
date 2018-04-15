
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static seedu.address.logic.commands.CommandTestUtil.showEventAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_EVENT;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

// @@author raynoldng
/**
 * Contains integration tests (interaction with the Model) for {@code SelectEventCommand}.
 */
public class SelectEventCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastEpicEventIndex = Index.fromOneBased(model.getFilteredEventList().size());
        assertExecutionSuccess(INDEX_FIRST_EVENT);
        assertExecutionSuccess(INDEX_THIRD_EVENT);

        assertExecutionSuccess(lastEpicEventIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        assertExecutionSuccess(INDEX_FIRST_EVENT);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        Index outOfBoundsIndex = INDEX_SECOND_EVENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getEventPlanner().getEventList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectEventCommand selectFirstEventCommand = new SelectEventCommand(INDEX_FIRST_EVENT);
        SelectEventCommand selectSecondEventCommand = new SelectEventCommand(INDEX_SECOND_EVENT);

        // same object -> returns true
        assertTrue(selectFirstEventCommand.equals(selectFirstEventCommand));

        // same values -> returns true
        SelectEventCommand selectFirstEventCommandCopy = new SelectEventCommand(INDEX_FIRST_EVENT);
        assertTrue(selectFirstEventCommand.equals(selectFirstEventCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstEventCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstEventCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstEventCommand.equals(selectSecondEventCommand));
    }

    /**
     * Executes a {@code SelectEventCommand} with the given {@code index}, and checks that
     * {@code JumpToListRequestEvent} is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectEventCommand selectEventCommand = prepareCommand(index);

        try {
            CommandResult commandResult = selectEventCommand.execute();
            assertEquals(String.format(SelectEventCommand.MESSAGE_SELECT_EVENT_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToEventListRequestEvent lastEvent = (JumpToEventListRequestEvent)
                eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }
    /**
     * Executes a {@code SelectEventCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectEventCommand selectEventCommand = prepareCommand(index);

        try {
            selectEventCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectEventCommand} with parameters {@code index}.
     */
    private SelectEventCommand prepareCommand(Index index) {
        SelectEventCommand selectEventCommand = new SelectEventCommand(index);
        selectEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectEventCommand;
    }
}
