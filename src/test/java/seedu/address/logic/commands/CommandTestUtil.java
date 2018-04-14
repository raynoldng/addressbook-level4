package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EventPlanner;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.EventNameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonNameContainsKeywordsPredicate;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.EditEventDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_PERSON_NAME_AMY = "Amy Bee";
    public static final String VALID_PERSON_NAME_BOB = "Bob Choo";
    public static final String VALID_PERSON_PHONE_AMY = "11111111";
    public static final String VALID_PERSON_PHONE_BOB = "22222222";
    public static final String VALID_PERSON_EMAIL_AMY = "amy@example.com";
    public static final String VALID_PERSON_EMAIL_BOB = "bob@example.com";
    public static final String VALID_PERSON_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_PERSON_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_PERSON_TAG_HUSBAND = "husband";
    public static final String VALID_PERSON_TAG_FRIEND = "friend";
    public static final String VALID_EVENT_NAME_GRADUATION = "AY201718 Graduation";
    public static final String VALID_EVENT_NAME_SEMINAR = "Seminar";
    public static final String VALID_EVENT_TAG_GRADUATION = "graduation";
    public static final String VALID_EVENT_TAG_SEMINAR = "seminar";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_PERSON_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_PERSON_NAME_BOB;
    public static final String NAME_DESC_GRADUATION = " " + PREFIX_NAME + VALID_EVENT_NAME_GRADUATION;
    public static final String NAME_DESC_SEMINAR = " " + PREFIX_NAME + VALID_EVENT_NAME_SEMINAR;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PERSON_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PERSON_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_PERSON_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_PERSON_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_PERSON_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_PERSON_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_PERSON_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_PERSON_TAG_HUSBAND;
    public static final String TAG_DESC_GRADUATION = " " + PREFIX_TAG + VALID_EVENT_TAG_GRADUATION;
    public static final String TAG_DESC_SEMINAR = " " + PREFIX_TAG + VALID_EVENT_TAG_SEMINAR;


    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditPersonCommand.EditPersonDescriptor DESC_AMY;
    public static final EditPersonCommand.EditPersonDescriptor DESC_BOB;
    public static final EditEventCommand.EditEventDescriptor DESC_GRADUATION;
    public static final EditEventCommand.EditEventDescriptor DESC_SEMINAR;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_PERSON_NAME_AMY)
                .withPhone(VALID_PERSON_PHONE_AMY).withEmail(VALID_PERSON_EMAIL_AMY)
                .withAddress(VALID_PERSON_ADDRESS_AMY).withTags(VALID_PERSON_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_PERSON_NAME_BOB)
                .withPhone(VALID_PERSON_PHONE_BOB).withEmail(VALID_PERSON_EMAIL_BOB)
                .withAddress(VALID_PERSON_ADDRESS_BOB)
                .withTags(VALID_PERSON_TAG_HUSBAND, VALID_PERSON_TAG_FRIEND).build();
        DESC_GRADUATION = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_GRADUATION)
                .withTags(VALID_EVENT_TAG_GRADUATION).build();
        DESC_SEMINAR = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR)
                .withTags(VALID_EVENT_TAG_SEMINAR).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book and the filtered person list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        EventPlanner expectedEventPlanner = new EventPlanner(actualModel.getEventPlanner());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedEventPlanner, actualModel.getEventPlanner());
            assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the attendee at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showAttendeeAtIndex(Model model, Index targetIndex) {
        FilteredList<Attendance> attendanceFilteredList = model.getSelectedEpicEvent().getFilteredAttendees();
        assertTrue(targetIndex.getZeroBased() < attendanceFilteredList.size());

        Attendance attendee = attendanceFilteredList.get(targetIndex.getZeroBased());
        final String[] splitName = attendee.getPerson().getFullName().name.split("\\s+");
        model.updateFilteredAttendanceList(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, attendanceFilteredList.size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getFullName().name.split("\\s+");
        model.updateFilteredPersonList(new PersonNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Deletes the first person in {@code model}'s filtered list from {@code model}'s address book.
     */
    public static void deleteFirstPerson(Model model) {
        Person firstPerson = model.getFilteredPersonList().get(0);
        try {
            model.deletePerson(firstPerson);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Person in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the event at the given {@code targetIndex} in the
     * {@code model}'s event planner.
     */
    public static void showEventAtIndex(Model model, Index targetIndex) {
        //TODO: Change implementation so that the event is truly shown by index instead of finding the first name
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEventList().size());

        EpicEvent event = model.getFilteredEventList().get(targetIndex.getZeroBased());
        final String[] splitName = event.getName().name.split("\\s+");
        model.updateFilteredEventList(new EventNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredEventList().size());
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }

    // @@author bayweiheng
    /**
     * Returns true if the {@code Person} is in the {@code model} and false otherwise
     */
    public static void assertPersonInModel(Person person, Model model) {
        int index = model.getFilteredPersonList().indexOf(person);
        assertTrue(index != -1);
    }

    /**
     * Returns true if the {@code Person} is not in the {@code model} and false otherwise
     */
    public static void assertPersonNotInModel(Person person, Model model) {
        int index = model.getFilteredPersonList().indexOf(person);
        assertTrue(index == -1);
    }

    /**
     * Returns true if the {@code EpicEvent} is in the {@code model} and false otherwise
     */
    public static void assertEventInModel(EpicEvent event, Model model) {
        int index = model.getFilteredEventList().indexOf(event);
        assertTrue(index != -1);
    }

    /**
     * Returns true if the {@code EpicEvent} is not in the {@code model} and false otherwise
     */
    public static void assertEventNotInModel(EpicEvent event, Model model) {
        int index = model.getFilteredEventList().indexOf(event);
        assertTrue(index == -1);
    }

    /**
     * Attempts to execute the {@code Command}. Prints the error message and throws an AssertionError
     * upon failure.
     */
    public static void tryToExecute(Command command) {
        try {
            command.execute();
        } catch (CommandException e) {
            System.out.println(e.getMessage());
            throw new AssertionError("Failure when trying to execute command");
        }
    }

    //@@author
}
