package seedu.address.model.event;

import java.util.Observable;

/**
 * Wrapper class for EpicEvent to listen for reassignments of selectedEpicEvent
 */
public class ObservableEpicEvent extends Observable {
    private EpicEvent epicEvent;

    public ObservableEpicEvent(EpicEvent epicEvent) {
        this.epicEvent = epicEvent;
    }

    public EpicEvent getEpicEvent() {
        return epicEvent;
    }

    public void setEpicEvent(EpicEvent epicEvent) {
        this.epicEvent = epicEvent;
        setChanged();
        notifyObservers();
    }
}
