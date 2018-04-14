package seedu.address.ui;

import static org.junit.Assert.assertEquals;

import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.ui.testutil.GuiTestAssert.assertAttendanceCardEquals;
import static seedu.address.ui.testutil.GuiTestAssert.assertAttendanceEventCardDisplaysAttendance;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.AttendanceCardHandle;
import guitests.guihandles.AttendanceListPanelHandle;

import javafx.collections.ObservableList;

import seedu.address.commons.events.ui.JumpToAttendanceListRequestEvent;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.event.ObservableEpicEvent;
import seedu.address.testutil.TypicalEpicEvents;


//@@author raynoldng

/**
 * Panel containing the list of attendees.
 */
public class AttendanceListPanelTest extends GuiUnitTest {
    private static final ObservableEpicEvent selectedEvent =
            new ObservableEpicEvent(TypicalEpicEvents.GRADUATIONAY18);
    private static final JumpToAttendanceListRequestEvent JUMP_TO_SECOND_ATTENDANCE =
            new JumpToAttendanceListRequestEvent(INDEX_SECOND_PERSON);

    private ObservableList<Attendance> attendanceList = selectedEvent.getEpicEvent().getAttendanceList();

    private AttendanceListPanelHandle attendanceListPanelHandle;

    @Before
    public void setUp() {
        AttendanceListPanel attendanceListPanel = new AttendanceListPanel(selectedEvent);
        uiPartRule.setUiPart(attendanceListPanel);

        attendanceListPanelHandle = new AttendanceListPanelHandle(getChildNode(attendanceListPanel.getRoot(),
                AttendanceListPanelHandle.ATTENDANCE_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < attendanceList.size(); i++) {
            attendanceListPanelHandle.navigateToCard(attendanceList.get(i));
            Attendance expectedAttendance = attendanceList.get(i);
            AttendanceCardHandle actualCard = attendanceListPanelHandle.getAttendanceCardHandle(i);

            assertAttendanceEventCardDisplaysAttendance(expectedAttendance, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_ATTENDANCE);
        guiRobot.pauseForHuman();

        AttendanceCardHandle expectedCard =
                attendanceListPanelHandle.getAttendanceCardHandle(INDEX_SECOND_EVENT.getZeroBased());
        AttendanceCardHandle selectedCard = attendanceListPanelHandle.getHandleToSelectedCard();
        assertAttendanceCardEquals(expectedCard, selectedCard);
    }
}
