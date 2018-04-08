package seedu.address.ui;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.util.Callback;
import org.fxmisc.easybind.EasyBind;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.ObservableEpicEvent;

// @@author raynoldng
/**
 * Panel containing the list of persons.
 */
public class AttendanceListPanel extends UiPart<Region> {
    private static final String FXML = "AttendanceListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AttendanceListPanel.class);

    @FXML
    private ListView<AttendanceCard> attendanceListView;

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
        registerAsAnEventHandler(this);
    }

    public void updateConnection() {
        setConnections();
    }

    private void setConnections() {
        EpicEvent selectedEpicEvent = selectedEpicEventObserver.getObservableEpicEvent().getEpicEvent();
        // Panel auto refresh UI when a perons toggles his attendance or changes his contact info
//        ObservableList<Attendance> attendanceList = FXCollections.observableArrayList(
//            attendance -> new javafx.beans.Observable[] {attendance.getPerson(),
//                    attendance.getHasAttendedEventProperty()}
//        );

        // My attempt to use obersvables to auto update UI, does not handle register events as
//        Callback<Attendance, javafx.beans.Observable[]> extractor = attendance -> new javafx.beans.Observable[] {
//                attendance.getPerson(), attendance.getHasAttendedEventProperty()};
//        ObservableList<Attendance> attendanceList = FXCollections.observableArrayList(extractor);
//        ObservableList<Attendance> backedAttendanceList = selectedEpicEvent.getAttendanceList();
//        Bindings.bindContentBidirectional(attendanceList, backedAttendanceList);

        // This does not work as changes to the backed list will not trigger change events
        // Original code, handles register and deregister properly but not attendance object change events
        ObservableList<Attendance> attendanceList = selectedEpicEvent.getAttendanceList();

        ObservableList<AttendanceCard> mappedList = EasyBind.map(
                attendanceList, (attendee) -> new AttendanceCard(attendee,
                        attendanceList.indexOf(attendee) + 1));

        attendanceListView.setItems(mappedList);
        attendanceListView.setCellFactory(listView -> new AttendanceListViewCell());

        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        attendanceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in attendance list panel changed to : '" + newValue + "'");
                        raise(new PersonPanelSelectionChangedEvent(newValue));
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
