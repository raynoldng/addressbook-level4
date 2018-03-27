package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import org.fxmisc.easybind.EasyBind;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToPersonListRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Person;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    private void setConnections(ObservableList<Attendance> attendanceList) {
        ObservableList<PersonCard> mappedList = EasyBind.map(
                attendanceList, (attendee) -> new PersonCard(attendee.getAttendee(), attendanceList.indexOf(attendee) + 1));
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

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            attendanceListView.scrollTo(index);
            attendanceListView.getSelectionModel().clearAndSelect(index);
        });
    }

//    @Subscribe
//    private void handleJumpToListRequestEvent(JumpToPersonListRequestEvent event) {
//        logger.info(LogsCenter.getEventHandlingLogMessage(event));
//        scrollTo(event.targetIndex);
//    }

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
