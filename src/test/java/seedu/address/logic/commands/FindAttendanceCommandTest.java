package seedu.address.logic.commands;

import org.junit.Before;
import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.EventPlanner;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonNameContainsKeywordsPredicate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalEpicEvents.GRADUATIONAY18;
import static seedu.address.testutil.TypicalEpicEvents.GRADUATIONAY18_INDEX;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

//@@author raynoldng
/**
 * Contains integration tests (interaction with the Model) for {@code FindAttendanceCommand}.
 */
public class FindAttendanceCommandTest {
//    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setSelectedEpicEvent(GRADUATIONAY18_INDEX);
    }

    @Test
    public void equals() {
        AttendanceNameContainsKeywordsPredicate firstPredicate =
                new AttendanceNameContainsKeywordsPredicate(Collections.singletonList("first"));
        AttendanceNameContainsKeywordsPredicate secondPredicate =
                new AttendanceNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindAttendanceCommand firstFindAttendanceCommand = new FindAttendanceCommand(firstPredicate);
        FindAttendanceCommand secondFindAttendanceCommand = new FindAttendanceCommand(secondPredicate);

        // same object -> returns true
        assertTrue(firstFindAttendanceCommand.equals(firstFindAttendanceCommand));

        // same values -> returns true
        FindAttendanceCommand firstFindAttendanceCommandCopy = new FindAttendanceCommand(firstPredicate);
        assertTrue(firstFindAttendanceCommand.equals(firstFindAttendanceCommandCopy));

        // different types -> returns false
        assertFalse(firstFindAttendanceCommand.equals(1));

        // null -> returns false
        assertFalse(firstFindAttendanceCommand.equals(null));

        // different person -> returns false
        assertFalse(firstFindAttendanceCommand.equals(secondFindAttendanceCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindAttendanceCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindAttendanceCommand command = prepareCommand("Kurz Elle Kunz");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(new Attendance(CARL, GRADUATIONAY18),
                new Attendance(ELLE, GRADUATIONAY18), new Attendance(FIONA, GRADUATIONAY18)));

    }

    /**
     * Parses {@code userInput} into a {@code FindAttendeeCommand}.
     */
    private FindAttendanceCommand prepareCommand(String userInput) {
        FindAttendanceCommand command =
                new FindAttendanceCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(userInput
                        .split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code EventPlanner} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindAttendanceCommand command, String expectedMessage,
                                      List<Attendance> expectedList) {
        EventPlanner expectedEventPlanner = new EventPlanner(model.getEventPlanner());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getSelectedEpicEvent().getFilteredAttendees());
        assertEquals(expectedEventPlanner, model.getEventPlanner());
    }
}
