package seedu.address.ui;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.AttendancePanelSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToAttendanceListRequestEvent;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.ObservableEpicEvent;



// @@author raynoldng
/**
 * Panel containing the list of persons.
 */
public class AttendanceListPanel extends UiPart<Region> {
    private static final String FXML = "AttendanceListPanel.fxml";
    private static final String ATTENDANCE_STATUS_FORMAT = "Attendees%s: (%d/%d)";
    private final Logger logger = LogsCenter.getLogger(AttendanceListPanel.class);

    @FXML
    private ListView<AttendanceCard> attendanceListView;

    @FXML
    private Label attendanceStatus;

    /**
     * Observer of selectedEpicEvent to update AttendanceListPanel
     */
    class EpicEventObserver implements Observer {

        private ObservableEpicEvent observableEpicEvent;
        /**
         * Observer that looks for changes to selectedEvent
         */
        public EpicEventObserver(ObservableEpicEvent observableEpicEvent) {
            this.observableEpicEvent = observableEpicEvent;
        }

        @Override
        public void update(Observable observable, Object o) {
            updateConnection();
        }
        public ObservableEpicEvent getObservableEpicEvent() {
            return observableEpicEvent;
        }

    }

    private final EpicEventObserver selectedEpicEventObserver;

    public AttendanceListPanel(ObservableEpicEvent selectedEpicEvent) {
        super(FXML);
        selectedEpicEventObserver = new EpicEventObserver(selectedEpicEvent);
        selectedEpicEvent.addObserver(selectedEpicEventObserver);
        setConnections();
        updateAttendanceStatus();
        registerAsAnEventHandler(this);
    }

    /**
     * Updates list view data and refreshes attendance panel header.
     */
    public void updateConnection() {
        setConnections();
        updateAttendanceStatus();
    }

    private void setConnections() {

        /*
         * There is no clean way to add an extractor to an existing ObservableList.
         * Using public static <E> ObservableList<E> observableList(List<E> list, Callback<E,Observable[]> extractor)
         * will not report mutations in the backed list.
         * As suggested in: https://stackoverflow.com/questions/34602457/add-extractor-to-existing-observablelist
         * A new ObservableList is created with the extract and then bind to actual attendance List
         */

        EpicEvent selectedEpicEvent = selectedEpicEventObserver.getObservableEpicEvent().getEpicEvent();

        // callback to listen for changes to the person or attendance status
        Callback<Attendance, javafx.beans.Observable[]> extractor = attendance -> new javafx.beans.Observable[] {
                attendance.getPerson(), attendance.getHasAttendedEventProperty()};
        ObservableList<Attendance> attendanceList = FXCollections.observableArrayList(extractor);
        ObservableList<Attendance> backedAttendanceList = selectedEpicEventObserver.getObservableEpicEvent()
                .getFilteredAttendees();
        Bindings.bindContentBidirectional(attendanceList, backedAttendanceList);

        ObservableList<AttendanceCard> mappedList = EasyBind.map(
                attendanceList, (attendee) -> new AttendanceCard(attendee,
                        attendanceList.indexOf(attendee) + 1));

        attendanceListView.setItems(mappedList);
        attendanceListView.setCellFactory(listView -> new AttendanceListViewCell());

        setEventHandlerForSelectionChangeEvent();

        attendanceList.addListener((InvalidationListener) change -> {
            updateAttendanceStatus();
        });
    }

    /** Update attendance header text **/
    private void updateAttendanceStatus() {
        int total = attendanceListView.getItems().size();
        ObservableList<Attendance> actualList = selectedEpicEventObserver.getObservableEpicEvent()
                .getEpicEvent().getAttendanceList();
        boolean isFiltered = actualList.size() != total;
        int numAttended = (int) attendanceListView.getItems().stream()
                .filter(attendanceCard -> attendanceCard.getAttendance().hasAttended())
                .count();
        attendanceStatus.setText(String.format(ATTENDANCE_STATUS_FORMAT, isFiltered ? "(filtered)" : "", numAttended,
                total));
    }

    private void setEventHandlerForSelectionChangeEvent() {
        attendanceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in attendance list panel changed to : '" + newValue + "'");
                        raise(new AttendancePanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            attendanceListView.scrollTo(index);
            attendanceListView.getSelectionModel().clearAndSelect(index);
        });
    }


    @Subscribe
    private void handleJumpToAttendanceListRequestEvent(JumpToAttendanceListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code AttendanceCard}.
     */
    class AttendanceListViewCell extends ListCell<AttendanceCard> {

        @Override
        protected void updateItem(AttendanceCard attendanceCard, boolean empty) {
            super.updateItem(attendanceCard, empty);

            if (empty || attendanceCard == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(attendanceCard.getRoot());
            }
        }
    }

}
