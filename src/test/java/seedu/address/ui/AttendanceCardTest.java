package seedu.address.ui;

import guitests.guihandles.AttendanceCardHandle;
import guitests.guihandles.EpicEventCardHandle;
import javafx.collections.ObservableList;
import org.junit.Test;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;
import seedu.address.testutil.EpicEventBuilder;
import seedu.address.testutil.TypicalEpicEvents;
import seedu.address.testutil.TypicalPersons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertAttendanceEventCardDisplaysAttendance;
import static seedu.address.ui.testutil.GuiTestAssert.assertEpicEventCardDisplaysEpicEvent;

// @@author raynoldng
public class AttendanceCardTest extends GuiUnitTest {

    private static int INDEX_ZERO = 0;

    @Test
    public void display() {
        EpicEvent event = TypicalEpicEvents.EVENT_WITH_ATTENDEES;

        Attendance AliceAttendance = event.getAttendanceList().get(0);
        AttendanceCard attendanceCard = new AttendanceCard(AliceAttendance, INDEX_ZERO);
        uiPartRule.setUiPart(attendanceCard);
        assertAttendanceCardDisplay(attendanceCard, AliceAttendance, 0);
    }

    @Test
    public void equals() {
        EpicEvent event = TypicalEpicEvents.CAREERTALK;
        try {
            event.registerPerson(TypicalPersons.ALICE);
            event.registerPerson(TypicalPersons.BOB);
        } catch (DuplicateAttendanceException e) {
            e.printStackTrace();
        }
        // there should only be one attendee
        assertEquals(event.getAttendanceList().size(), 2);
        Attendance AliceAttendance = event.getAttendanceList().get(0);
        Attendance BobAttendance = event.getAttendanceList().get(1);

        AttendanceCard attendanceCard = new AttendanceCard(AliceAttendance, 0);
        AttendanceCard copy = new AttendanceCard(AliceAttendance, 0);

        // same attendance, same index -> return true
        assertTrue(attendanceCard.equals(copy));

        // same object -> returns true
        assertTrue(attendanceCard.equals(attendanceCard));

        // null -> returns false
        assertFalse(attendanceCard.equals(null));

        // different types -> return false
        assertFalse(attendanceCard.equals(0));

        // different card, same index -> returns false
        assertFalse(attendanceCard.equals(new AttendanceCard(BobAttendance, 0)));

        // same event, different index -> returns false
        assertFalse(attendanceCard.equals(new AttendanceCard(AliceAttendance, 1)));
    }

    /**
     * Asserts that {@code attendanceCard} displays the details of {@code expectedAttendance} correctly and matches
     * {@code expectedId}.
     */
    private void assertAttendanceCardDisplay(AttendanceCard attendanceCard, Attendance expectedAttendance,
                                             int expectedId) {
        guiRobot.pauseForHuman();

        AttendanceCardHandle attendanceCardHandle = new AttendanceCardHandle(attendanceCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", attendanceCardHandle.getId());

        // verify person details are displayed correctly
        assertAttendanceEventCardDisplaysAttendance(expectedAttendance, attendanceCardHandle);
    }
}
