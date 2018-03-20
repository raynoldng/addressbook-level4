package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyEventPlanner;

/** Indicates the EventPlanner in the model has changed*/
public class EventPlannerChangedEvent extends BaseEvent {

    public final ReadOnlyEventPlanner data;

    public EventPlannerChangedEvent(ReadOnlyEventPlanner data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getPersonList().size() + ", number of events "
                + data.getEventList().size() + ", number of person tags " + data.getPersonTagList().size()
                + ", number of event tags " + data.getEventTagList().size();
    }
}
