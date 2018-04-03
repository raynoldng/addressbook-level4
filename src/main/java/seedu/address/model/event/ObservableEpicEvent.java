package seedu.address.model.event;

import java.util.Observable;

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
