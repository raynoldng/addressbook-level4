package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalEpicEvents.GRADUATIONAY18;
import static seedu.address.testutil.TypicalEpicEvents.ORIENTATION;
import static seedu.address.testutil.TypicalEpicEvents.ORIENTATION_INDEX;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.HOON;

import org.junit.Test;

import javafx.collections.transformation.FilteredList;

import seedu.address.logic.commands.FindRegistrantCommand;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;

//@@author raynoldng
public class FindRegistrantCommandSystemTest extends EventPlannerSystemTest {

    @Test
    public void find() {

        // First: assert that first event (Graduation) is the selected event
        assert(getModel().getSelectedEpicEvent().getEpicEvent().equals(GRADUATIONAY18));

        Model expectedModel = getModel();

        /* Case: find person where person list is not displaying the person we are finding -> 1 person found */
        String command = FindRegistrantCommand.COMMAND_WORD + " Carl";
        Attendance carlAttendance = new Attendance(CARL, GRADUATIONAY18);
        ModelHelper.setFilteredAttendanceList(expectedModel, carlAttendance);
        assertCommandSuccess(command, expectedModel);

        /* Case: find multiple persons in address book, 2 keywords -> 2 persons found */
        command = FindRegistrantCommand.COMMAND_WORD + " Benson Daniel";
        Attendance bensonAttendance = new Attendance(BENSON, GRADUATIONAY18);
        Attendance danielAttendance = new Attendance(DANIEL, GRADUATIONAY18);
        ModelHelper.setFilteredAttendanceList(expectedModel, bensonAttendance, danielAttendance);
        assertCommandSuccess(command, expectedModel);

        /* Case: switch to event that has only has registered */
        getModel().setSelectedEpicEvent(ORIENTATION_INDEX);
        Attendance hoonOrientationAttendance = new Attendance(HOON, ORIENTATION);
        command = FindRegistrantCommand.COMMAND_WORD + " Hoon";
        ModelHelper.setFilteredAttendanceList(expectedModel, hoonOrientationAttendance);
        assertCommandSuccess(command, expectedModel);

        /* Case: mixed case command word -> rejected */
        command = "FiNd-AttendAnCe Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_PERSONS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        FilteredList<Attendance> filteredList = expectedModel.getSelectedEpicEvent().getFilteredAttendees();
        String expectedResultMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW, filteredList.size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertAttendanceListHeaderDisplaysExpected(getModel());
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
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
