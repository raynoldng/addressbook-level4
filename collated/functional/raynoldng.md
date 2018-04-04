# raynoldng
###### /resources/view/MainWindow.fxml
``` fxml
          <VBox fx:id="epicEventList" minWidth="340" prefWidth="340" SplitPane.resizableWithParent="false">
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <StackPane fx:id="epicEventListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
          </VBox>

          <VBox fx:id="attendanceList" minWidth="340" prefWidth="340" SplitPane.resizableWithParent="false">
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <StackPane fx:id="attendanceListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
          </VBox>
```
###### /java/seedu/address/ui/EpicEventListPanel.java
``` java
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
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.event.EpicEvent;
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

    public ObservableList<Attendance> getEventAttendee(int index) {
        return epicEventListView.getItems().get(index).epicEvent.getAttendanceList();
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
```
###### /java/seedu/address/ui/EpicEventCard.java
``` java
package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.event.EpicEvent;

/**
 * An UI component that displays information of an {@code EpicEvent}.
 */
public class EpicEventCard extends UiPart<Region> {

    private static final String FXML = "EpicEventListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on EventPlanner level 4</a>
     */

    public final EpicEvent epicEvent;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;

    public EpicEventCard(EpicEvent epicEvent, int displayedIndex) {
        super(FXML);
        this.epicEvent = epicEvent;
        id.setText(displayedIndex + ". ");
        name.setText(epicEvent.getName().name);
        epicEvent.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EpicEventCard)) {
            return false;
        }

        // state check
        EpicEventCard card = (EpicEventCard) other;
        return id.getText().equals(card.id.getText())
                && epicEvent.equals(card.epicEvent);
    }
}
```
###### /java/seedu/address/ui/AttendanceListPanel.java
``` java
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

    @Subscribe
    private void handleAttendanceCardToggleEvent(AttendanceCardToggleEvent attendanceCardToggleEvent) {
        AttendanceCard card = attendanceListView.getItems().get(attendanceCardToggleEvent.targetIndex);
        card.toggleImage();
        attendanceListView.refresh();
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
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    @Subscribe
    private void handleEpicEventPanelSelectionChangedEvent(EpicEventPanelSelectionChangedEvent event) {
        logic.setSelectedEpicEvent(event.getNewSelection().epicEvent);

    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToEventListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        logic.setSelectedEpicEvent(event.targetIndex);

    }
```
###### /java/seedu/address/ui/AttendanceCard.java
``` java
/**
 * An UI component that displays information of a {@code Person}.
 */
public class AttendanceCard extends PersonCard {

    protected static final String FXML = "AttendanceListCard.fxml";

    private static final String ICON_ATTENDED_URL = "/images/green_tick.png";
    private static final String ICON_NOT_ATTENDED_URL = "/images/red_cross.png";

    private static final Image ICON_ATTENDED = new Image(ICON_ATTENDED_URL);
    private static final Image ICON_NOT_ATTENDED = new Image(ICON_NOT_ATTENDED_URL);
    private static final List<Image> images = Arrays.asList(ICON_ATTENDED, ICON_NOT_ATTENDED);

    @FXML
    private ImageView attendanceToggleImage;

    private Attendance attendance;
    private IntegerProperty intValue;

    public AttendanceCard(Attendance attendee, int displayedIndex) {
        super(attendee.getPerson(), displayedIndex, FXML);
        this.attendance = attendee;
        intValue = new SimpleIntegerProperty();

        intValue.set(attendee.hasAttended() ? 0 : 1);
        attendanceToggleImage.imageProperty().bind(Bindings.createObjectBinding(() -> images.get(intValue.getValue())));
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void toggleImage() {
        intValue.set(attendance.hasAttended() ? 1 : 0);
    }
}
```
###### /java/seedu/address/ui/PersonCard.java
``` java
    public PersonCard(Person person, int displayedIndex, String fxml) {
        super(fxml);
        this.person = person;
        initializeCardDetails(person, displayedIndex);
    }

    /** initilialize card labels **/
    private void initializeCardDetails(Person person, int displayedIndex) {
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().name);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
```
###### /java/seedu/address/commons/events/ui/EpicEventPanelSelectionChangedEvent.java
``` java
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
```
###### /java/seedu/address/commons/events/ui/AttendanceCardToggleEvent.java
``` java
/**
 * Represents a toggle of attendee's attendance
 */
public class AttendanceCardToggleEvent extends BaseEvent {

    public final int targetIndex;

    public AttendanceCardToggleEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/logic/Logic.java
``` java
    /** Returns selected EpicEvent **/
    ObservableEpicEvent getSelectedEpicEvent();

    void setSelectedEpicEvent(int index);

    void setSelectedEpicEvent(EpicEvent epicEvent);
```
###### /java/seedu/address/logic/parser/SelectEventCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SelectEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class SelectEventCommandParser implements Parser<SelectEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectEventCommand
     * and returns an SelectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectEventCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectEventCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/commands/SelectEventCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;


/**
 * Selects a event identified using it's last displayed index from the event planner.
 */
public class SelectEventCommand extends Command {

    public static final String COMMAND_WORD = "select-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the event identified by the index number used in the last event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_EVENT_SUCCESS = "Selected Event: %1$s";

    private final Index targetIndex;

    public SelectEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<EpicEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        model.setSelectedEpicEvent(targetIndex.getZeroBased());

        EventsCenter.getInstance().post(new JumpToEventListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_EVENT_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectEventCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/LogicManager.java
``` java
    @Override
    public ObservableEpicEvent getSelectedEpicEvent() {
        return model.getSelectedEpicEvent();
    }

    @Override
    public void setSelectedEpicEvent(int index) {
        model.setSelectedEpicEvent(index);
    }

    @Override
    public void setSelectedEpicEvent(EpicEvent epicEvent) {
        model.setSelectedEpicEvent(epicEvent);
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void setSelectedEpicEvent(int index) {
        selectedEpicEvent.setEpicEvent(filteredEvents.get(index));
    }

    @Override
    public void setSelectedEpicEvent(EpicEvent epicEvent) {
        selectedEpicEvent.setEpicEvent(epicEvent);
    }

    @Override
    public ObservableEpicEvent getSelectedEpicEvent() {
        return selectedEpicEvent;
    }

```
###### /java/seedu/address/model/event/ObservableEpicEvent.java
``` java
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
```
###### /java/seedu/address/model/Model.java
``` java
    ObservableEpicEvent getSelectedEpicEvent();

    void setSelectedEpicEvent(int index);

    void setSelectedEpicEvent(EpicEvent epicEvent);
```
