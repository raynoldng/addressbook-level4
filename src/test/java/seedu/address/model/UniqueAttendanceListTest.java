package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.attendance.UniqueAttendanceList;

public class UniqueAttendanceListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAttendanceList uniqueAttendanceList = new UniqueAttendanceList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueAttendanceList.asObservableList().remove(0);
    }
}
