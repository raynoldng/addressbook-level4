package systemtests;

import static org.junit.Assert.assertTrue;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.SelectEventCommand.MESSAGE_SELECT_EVENT_SUCCESS;
import static seedu.address.testutil.TypicalEpicEvents.KEYWORD_MATCHING_OLYMPIAD;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEvents;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ATTENDANCE;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectEventCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.event.EpicEvent;


public class SelectEventCommandSystemTest extends EventPlannerSystemTest {
    @Test
    public void select() {
        /* ------------------------ Perform select operations on the shown unfiltered list -------------------------- */

        /* Case: select the first card in the person list, command with leading spaces and trailing spaces
         * -> selected
         */

        String command = "   " + SelectEventCommand.COMMAND_WORD + " " + INDEX_FIRST_ATTENDANCE.getOneBased() + "   ";
        assertCommandSuccess(command, INDEX_FIRST_ATTENDANCE);

        /* Case: select the last card in the event list -> selected */
        Index epicEventCount = Index.fromOneBased(getTypicalEvents().size());
        command = SelectEventCommand.COMMAND_WORD + " " + epicEventCount.getOneBased();
        assertCommandSuccess(command, epicEventCount);

        /* Case: undo previous selection -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo selecting last card in the list -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: select the middle card in the person list -> selected */
        Index middleIndex = Index.fromOneBased(epicEventCount.getOneBased() / 2);
        command = SelectEventCommand.COMMAND_WORD + " " + middleIndex.getOneBased();
        assertCommandSuccess(command, middleIndex);

        /* Case: select the current selected card -> selected */
        assertCommandSuccess(command, middleIndex);

        /* ------------------------ Perform select operations on the shown filtered list ---------------------------- */

        /* Case: filtered person list, select index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showEventsWithName(KEYWORD_MATCHING_OLYMPIAD);
        int invalidIndex = getModel().getEventPlanner().getEventList().size();
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " " + invalidIndex,
                MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);

        /* Case: filtered event list, select index within bounds of event planner and person list -> selected */
        Index validIndex = Index.fromOneBased(1);
        assertTrue(validIndex.getZeroBased() < getModel().getFilteredEventList().size());
        command = SelectEventCommand.COMMAND_WORD + " " + validIndex.getOneBased();
        assertCommandSuccess(command, validIndex);

        /* ----------------------------------- Perform invalid select operations ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " " + 0,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " " + -1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredEventList().size() + 1;
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " " + invalidIndex,
                 MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " 1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeLeCt 1", MESSAGE_UNKNOWN_COMMAND);

        /* Case: select from empty event planner -> rejected */
        clearEventPlanner();
        assertCommandFailure(SelectEventCommand.COMMAND_WORD + " " + INDEX_FIRST_ATTENDANCE.getOneBased(),
                MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected person.<br>
     * 4. {@code Model}, {@code Storage} and {@code EpicEventListPanel} remain unchanged.<br>
     * 5. Selected card is at {@code expectedSelectedCardIndex} <br>
     * 6. Status bar remains unchanged.<br>
     * 7. Selected Epic Event has been updated accordingly.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see EventPlannerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_EVENT_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getEventListPanel().getSelectedCardIndex();

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedEpicEventCardUnchanged();
        } else {
            assertSelectedEpicEventCardChanged(expectedSelectedCardIndex);
        }

        EpicEvent expectedEpicEvent = expectedModel.getFilteredEventList()
                .get(expectedSelectedCardIndex.getZeroBased());
        EpicEvent selectedEpicEvent = getModel().getSelectedEpicEvent().getEpicEvent();

        assertTrue(expectedEpicEvent.equals(selectedEpicEvent));

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
