package seedu.address.commons.events.ui;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.BaseEvent;

//@@author raynoldng
/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToAttendanceListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToAttendanceListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
