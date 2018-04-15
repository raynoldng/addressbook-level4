package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEventPlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author bayweiheng

/** Contains unit tests for RegisterPersonCommand */
public class RegisterPersonCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        RegisterPersonCommand registerPersonCommand = prepareCommand(outOfBoundIndex,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        assertCommandFailure(registerPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEventPlanner().getPersonList().size());

        RegisterPersonCommand registerPersonCommand = prepareCommand(outOfBoundIndex,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        assertCommandFailure(registerPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        RegisterPersonCommand registerFirstCommand = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        RegisterPersonCommand registerSecondCommand = prepareCommand(INDEX_SECOND_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        RegisterPersonCommand registerThirdCommand = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_SECOND_EVENT.getZeroBased()).getName().toString());

        // same object -> returns true
        assertTrue(registerFirstCommand.equals(registerFirstCommand));

        // same values -> returns true
        RegisterPersonCommand registerFirstCommandCopy = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());
        assertTrue(registerFirstCommand.equals(registerFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        registerFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(registerFirstCommand.equals(registerFirstCommandCopy));

        // different types -> returns false
        assertFalse(registerFirstCommand.equals(1));

        // null -> returns false
        assertFalse(registerFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(registerFirstCommand.equals(registerSecondCommand));

        // different event -> returns false
        assertFalse(registerFirstCommand.equals(registerThirdCommand));
    }

    /**
     * Returns a {@code RegisterEventCommand} with parameters {@code index}, {@code eventName}.
     */
    private RegisterPersonCommand prepareCommand(Index index, String eventName) {
        RegisterPersonCommand registerPersonCommand = new RegisterPersonCommand(index, eventName);
        registerPersonCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return registerPersonCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

}
