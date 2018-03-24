package seedu.address.model.attendance.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Attendance objects.
 */
public class DuplicateAttendanceException extends DuplicateDataException {
    public DuplicateAttendanceException() {
        super("Operation would result in duplicate attendance");
    }
}
