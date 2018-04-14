package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.ui.testutil.GuiTestAssert.assertAttendanceEventCardDisplaysAttendance;

import org.junit.Test;

import guitests.guihandles.AttendanceCardHandle;

import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.testutil.TypicalEpicEvents;
import seedu.address.testutil.TypicalPersons;



// @@author raynoldng
public class AttendanceCardTest extends GuiUnitTest {

    @Test
    public void display() {
        EpicEvent event = TypicalEpicEvents.GRADUATIONAY18;

        Attendance aliceAttendance = event.getAttendanceList().get(0);
        AttendanceCard attendanceCard = new AttendanceCard(aliceAttendance, 0);
        uiPartRule.setUiPart(attendanceCard);
        assertAttendanceCardDisplay(attendanceCard, aliceAttendance, 0);
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
        Attendance aliceAttendance = event.getAttendanceList().get(0);
        Attendance bobAttendance = event.getAttendanceList().get(1);

        AttendanceCard attendanceCard = new AttendanceCard(aliceAttendance, 0);
        AttendanceCard copy = new AttendanceCard(aliceAttendance, 0);

        // same attendance, same index -> return true
        assertTrue(attendanceCard.equals(copy));

        // same object -> returns true
        assertTrue(attendanceCard.equals(attendanceCard));

        // null -> returns false
        assertFalse(attendanceCard.equals(null));

        // different types -> return false
        assertFalse(attendanceCard.equals(0));

        // different card, same index -> returns false
        assertFalse(attendanceCard.equals(new AttendanceCard(bobAttendance, 0)));

        // same event, different index -> returns false
        assertFalse(attendanceCard.equals(new AttendanceCard(aliceAttendance, 1)));
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
