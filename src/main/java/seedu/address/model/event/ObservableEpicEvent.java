
package seedu.address.model.event;

import java.util.Observable;

import javafx.collections.transformation.FilteredList;
import seedu.address.model.attendance.Attendance;
// @@author raynoldng
/**
 * Wrapper class for EpicEvent to listen for reassignments of selectedEpicEvent
 */
public class ObservableEpicEvent extends Observable {
    private EpicEvent epicEvent;
    private FilteredList<Attendance> filteredAttendees;

    public ObservableEpicEvent(EpicEvent epicEvent) {
        updateConnections(epicEvent);
    }

    public EpicEvent getEpicEvent() {
        return epicEvent;
    }

    public void setEpicEvent(EpicEvent epicEvent) {
        updateConnections(epicEvent);
    }

    public FilteredList<Attendance> getFilteredAttendees() {
        return filteredAttendees;
    }

    /**
     * updates {@code epicEvent} and {@code filteredAttendees}, notifies observers
     */
    private void updateConnections(EpicEvent epicEvent) {
        this.epicEvent = epicEvent;
        this.filteredAttendees = new FilteredList<>(this.epicEvent.getAttendanceList());
        setChanged();
        notifyObservers();
    }
}
