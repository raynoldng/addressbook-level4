package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

// @@author raynoldng
/**
 * Represents a toggle of attendee's attendance
 */
public class AttendanceCardToggleEvent extends BaseEvent {

    public final int targetIndex;

    public AttendanceCardToggleEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
