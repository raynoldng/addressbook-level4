
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
import seedu.address.commons.events.ui.ClearEventListSelectionEvent;
import seedu.address.commons.events.ui.EpicEventPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.model.event.EpicEvent;
// @@author raynoldng
/**
 * Panel containing the list of events.
 */
public class EpicEventListPanel extends UiPart<Region> {
    private static final String FXML = "EpicEventListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EpicEventListPanel.class);

    @FXML
    private ListView<EpicEventCard> epicEventListView;

    public EpicEventListPanel(ObservableList<EpicEvent> epicEventList) {
        super(FXML);
        setConnections(epicEventList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<EpicEvent> epicEventList) {
        ObservableList<EpicEventCard> mappedList = EasyBind.map(
                epicEventList, (event) -> new EpicEventCard(event, epicEventList.indexOf(event) + 1));
        epicEventListView.setItems(mappedList);
        epicEventListView.setCellFactory(listView -> new EventListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        epicEventListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in events list panel changed to : '" + newValue + "'");
                        raise(new EpicEventPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code EventCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            epicEventListView.scrollTo(index);
            epicEventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToEventListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    @Subscribe
    private void handleClearEventListSelectionEvent(ClearEventListSelectionEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        for (int selectedIndex : epicEventListView.getSelectionModel().getSelectedIndices()) {
            epicEventListView.getSelectionModel().clearSelection(selectedIndex);
        }
    }


    /**
     * Custom {@code ListCell} that displays the graphics of a {@code EventCard}.
     */
    class EventListViewCell extends ListCell<EpicEventCard> {

        @Override
        protected void updateItem(EpicEventCard event, boolean empty) {
            super.updateItem(event, empty);

            if (empty || event == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(event.getRoot());
            }
        }
    }

}
