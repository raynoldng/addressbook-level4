package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedAttendance.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Person;
import seedu.address.testutil.Assert;

//@@author jiangyue12392
public class XmlAdaptedAttendanceTest {
    private static final Attendance VALID_ATTENDANCE = new Attendance(BENSON, false);

    @Test
    public void toModelType_validAttendanceDetails_returnsAttendance() throws Exception {
        XmlAdaptedAttendance attendance = new XmlAdaptedAttendance(new XmlAdaptedPerson(BENSON), false);
        assertEquals(VALID_ATTENDANCE, attendance.toModelType());
    }

    @Test
    public void toModelType_nullPerson_throwsIllegalValueException() {
        XmlAdaptedAttendance attendance = new XmlAdaptedAttendance(null, true);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, attendance::toModelType);
    }

    @Test
    public void toModelType_nullHasAttended_throwsIllegalValueException() {
        XmlAdaptedAttendance attendance = new XmlAdaptedAttendance(new XmlAdaptedPerson(BENSON), null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "Attendance Status");
        Assert.assertThrows(IllegalValueException.class, expectedMessage, attendance::toModelType);
    }

}

