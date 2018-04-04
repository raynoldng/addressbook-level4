// @@author raynoldng
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.EpicEventCard;

/**
 * Represents a selection change in the Epic Event List Panel
 */
public class EpicEventPanelSelectionChangedEvent extends BaseEvent {


    private final EpicEventCard newSelection;

    public EpicEventPanelSelectionChangedEvent(EpicEventCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public EpicEventCard getNewSelection() {
        return newSelection;
    }
}
