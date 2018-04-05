package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.EpicEvent;
import seedu.address.testutil.EpicEventBuilder;

//@@author william6364
/**
 * Contains integration tests (interaction with the Model) for {@code AddEventCommand}.
 */
public class AddEventCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newEvent_success() throws Exception {
        EpicEvent validEvent = new EpicEventBuilder().build();

        Model expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());
        expectedModel.addEvent(validEvent);

        assertCommandSuccess(prepareCommand(validEvent, model), model,
                String.format(AddEventCommand.MESSAGE_SUCCESS, validEvent), expectedModel);
    }

    @Test
    public void execute_duplicateEvent_throwsCommandException() throws Exception {
        EpicEvent validEvent = new EpicEventBuilder().build();
        model.addEvent(validEvent);

        EpicEvent eventInList = model.getEventPlanner().getEventList().get(0);
        assertCommandFailure(prepareCommand(eventInList, model), model, AddEventCommand.MESSAGE_DUPLICATE_EVENT);
    }

    /**
     * Generates a new {@code AddEventCommand} which upon execution, adds {@code event} into the {@code model}.
     */
    private AddEventCommand prepareCommand(EpicEvent event, Model model) {
        AddEventCommand command = new AddEventCommand(event);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
