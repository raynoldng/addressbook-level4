package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.EpicEventPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.attendance.Attendance;


/**
 * Panel containing the list of persons.
 */
public class AttendanceListPanel extends UiPart<Region> {
    private static final String FXML = "AttendanceListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AttendanceListPanel.class);

    @FXML
    private ListView<PersonCard> attendanceListView;

    public AttendanceListPanel(ObservableList<Attendance> attendanceList) {
        super(FXML);
        setConnections(attendanceList);
        registerAsAnEventHandler(this);
    }

    public void updateConnection(ObservableList<Attendance> attendanceList) {
        setConnections(attendanceList);
    }

    private void setConnections(ObservableList<Attendance> attendanceList) {
        ObservableList<PersonCard> mappedList = EasyBind.map(
                attendanceList, (attendee) -> new PersonCard(attendee.getPerson(),
                        attendanceList.indexOf(attendee) + 1));
        attendanceListView.setItems(mappedList);
        attendanceListView.setCellFactory(listView -> new PersonListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        attendanceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                        raise(new PersonPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    @Subscribe
    private void handleEpicEventPanelSelectionChangedEvent(EpicEventPanelSelectionChangedEvent event) {
        ObservableList<Attendance> attendees = event.getNewSelection().epicEvent.getAttendanceList();
        updateConnection(attendees);
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
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<PersonCard> {

        @Override
        protected void updateItem(PersonCard person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(person.getRoot());
            }
        }
    }

}
