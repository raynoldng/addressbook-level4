package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author raynoldng
/**
 * Indicates a request to clear selection in Event List Panel
 */
public class ClearEventListSelectionEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
