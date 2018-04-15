package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SEMINAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalEpicEvents.CAREERTALK;
import static seedu.address.testutil.TypicalEpicEvents.GRADUATION;
import static seedu.address.testutil.TypicalEpicEvents.KEYWORD_MATCHING_OLYMPIAD;
import static seedu.address.testutil.TypicalEpicEvents.MATHOLYMPIAD;
import static seedu.address.testutil.TypicalEpicEvents.ORIENTATION;
import static seedu.address.testutil.TypicalEpicEvents.SEMINAR;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.Name;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EpicEventUtil;

//@@author william6364

public class AddEventCommandSystemTest extends EventPlannerSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add an event without tags to a non-empty address book, command with leading spaces and trailing spaces
         * -> added
         */
        EpicEvent toAdd = GRADUATION;
        String command = "   " + AddEventCommand.COMMAND_WORD + "  " + NAME_DESC_GRADUATION + " " + TAG_DESC_GRADUATION;
        assertCommandSuccess(command, toAdd);

        // Case: undo adding Graduation to the list -> Graduation deleted
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        // Case: redo adding Graduation to the list -> Graduation added again
        command = RedoCommand.COMMAND_WORD;
        model.addEvent(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add an event with a different name -> added */
        assertCommandSuccess(SEMINAR);

        /* Case: add to empty address book -> added */
        clearEventPlanner();
        assertCommandSuccess(MATHOLYMPIAD);

        /* Case: add an event with tags, command with parameters in reverse order -> added */
        toAdd = SEMINAR;
        command = AddEventCommand.COMMAND_WORD + TAG_DESC_SEMINAR + NAME_DESC_SEMINAR;
        assertCommandSuccess(command, toAdd);

        /* Case: add an event, missing tags -> added */
        assertCommandSuccess(ORIENTATION);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the event list before adding -> added */
        showEventsWithName(KEYWORD_MATCHING_OLYMPIAD);
        assertCommandSuccess(GRADUATION);

        /* ------------------------ Perform add operation while an event card is selected --------------------------- */

        /* Case: selects first card in the event list, add an event -> added, card selection remains unchanged */
        selectEvent(Index.fromOneBased(1));
        assertCommandSuccess(CAREERTALK);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate event -> rejected */
        command = EpicEventUtil.getAddEventCommand(SEMINAR);
        assertCommandFailure(command, AddEventCommand.MESSAGE_DUPLICATE_EVENT);

        /* Case: add a duplicate event except with different tags -> rejected */
        // "graduation" is an existing tag used in the default model, see TypicalEpicEvents#GRADUATION
        // This test will fail if a new tag that is not in the model is used, see the bug documented in
        // EventPlanner#addEvent(EpicEvent)
        command = EpicEventUtil.getAddEventCommand(ORIENTATION) + " " + PREFIX_TAG.getPrefix() + "graduation";
        assertCommandFailure(command, AddEventCommand.MESSAGE_DUPLICATE_EVENT);

        /* Case: missing name -> rejected */
        command = AddEventCommand.COMMAND_WORD + TAG_DESC_GRADUATION;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds-event " + EpicEventUtil.getEventDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddEventCommand.COMMAND_WORD + INVALID_NAME_DESC;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddEventCommand.COMMAND_WORD + NAME_DESC_GRADUATION + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddEventCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddEventCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code EventListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(EpicEvent toAdd) {
        assertCommandSuccess(EpicEventUtil.getAddEventCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(EpicEvent)}. Executes {@code command}
     * instead.
     * @see AddEventCommandSystemTest#assertCommandSuccess(EpicEvent)
     */
    private void assertCommandSuccess(String command, EpicEvent toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addEvent(toAdd);
        } catch (DuplicateEventException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddEventCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, EpicEvent)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code EventListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddEventCommandSystemTest#assertCommandSuccess(String, EpicEvent)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code EventListPanel} remain unchanged.<br>
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
