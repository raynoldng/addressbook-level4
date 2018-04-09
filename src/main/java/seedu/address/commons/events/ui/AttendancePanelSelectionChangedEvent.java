// @@author raynoldng
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.AttendanceCard;

/**
 * Represents a selection change in the Epic Event List Panel
 */
public class AttendancePanelSelectionChangedEvent extends BaseEvent {


    private final AttendanceCard newSelection;

    public AttendancePanelSelectionChangedEvent(AttendanceCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public AttendanceCard getNewSelection() {
        return newSelection;
    }
}
