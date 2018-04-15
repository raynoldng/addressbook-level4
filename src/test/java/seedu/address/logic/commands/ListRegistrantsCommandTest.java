package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showAttendeeAtIndex;
import static seedu.address.testutil.TypicalEpicEvents.GRADUATIONAY18_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author raynoldng
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListRegistrantsCommand.
 */
public class ListRegistrantsCommandTest {

    private Model model;
    private Model expectedModel;
    private ListRegistrantsCommand listRegistrantsCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setSelectedEpicEvent(GRADUATIONAY18_INDEX);
        expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());

        listRegistrantsCommand = new ListRegistrantsCommand();
        listRegistrantsCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listRegistrantsCommand, model, ListRegistrantsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showAttendeeAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(listRegistrantsCommand, model, ListRegistrantsCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
