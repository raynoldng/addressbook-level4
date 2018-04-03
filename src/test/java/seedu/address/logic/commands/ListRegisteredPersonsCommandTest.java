package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEventPlanner;

import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EventPlanner;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;

//@@author bayweiheng
/**
 * Contains unit tests for ListRegisteredPersonsCommand.
 */
public class ListRegisteredPersonsCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void execute() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        EpicEvent mainEvent = model.getEventList().get(1);
        for (Person person: model.getFilteredPersonList()) {
            try {
                model.registerPersonForEvent(person, mainEvent);
            } catch (Exception e) {
                throw new AssertionError("not possible");
            }
        }
        ListRegisteredPersonsCommand command = prepareCommand(mainEvent.getName().toString());
        assertCommandSuccess(command, expectedMessage, model.getFilteredPersonList());
    }

    /**
     * Parses {@code userInput} into a {@code ListRegisteredPersonsCommand}.
     */
    private ListRegisteredPersonsCommand prepareCommand(String eventName) {
        ListRegisteredPersonsCommand command =
                new ListRegisteredPersonsCommand(eventName);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code EventPlanner} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(
            ListRegisteredPersonsCommand command, String expectedMessage, List<Person> expectedList) {
        EventPlanner expectedEventPlanner = new EventPlanner(model.getEventPlanner());
        CommandResult commandResult = null;
        try {
            commandResult = command.execute();
        } catch (CommandException e) {
            throw new AssertionError("not possible");
        }

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedEventPlanner, model.getEventPlanner());
    }

}
