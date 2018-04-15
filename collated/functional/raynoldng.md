# raynoldng
###### \java\seedu\address\commons\events\ui\AttendancePanelSelectionChangedEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.AttendanceCard;

/**
 * Represents a selection change in the Epic Event List Panel
 */
public class AttendancePanelSelectionChangedEvent extends BaseEvent {


    private final AttendanceCard newSelection;

    public AttendancePanelSelectionChangedEvent(AttendanceCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public AttendanceCard getNewSelection() {
        return newSelection;
    }
}
```
###### \java\seedu\address\commons\events\ui\ClearEventListSelectionEvent.java
``` java
/**
 * Indicates a request to clear selection in Event List Panel
 */
public class ClearEventListSelectionEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\EpicEventPanelSelectionChangedEvent.java
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
###### \java\seedu\address\commons\events\ui\JumpToAttendanceListRequestEvent.java
``` java
/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToAttendanceListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToAttendanceListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\address\logic\commands\FindRegistrantCommand.java
``` java
/**
 * Finds and lists all persons in event planner whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindRegistrantCommand extends Command {

    public static final String COMMAND_WORD = "find-registrant";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) in the selected event and displays them as a list with index"
            + " numbers.\nParameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final AttendanceNameContainsKeywordsPredicate predicate;

    public FindRegistrantCommand(AttendanceNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredAttendanceList(predicate);
        int numFound = model.getSelectedEpicEvent().getFilteredAttendees().size();
        return new CommandResult(getMessageForPersonListShownSummary(numFound));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindRegistrantCommand // instanceof handles nulls
                && this.predicate.equals(((FindRegistrantCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\ListEventCommand.java
``` java
/**
 * Lists all events in the event planner to the user.
 */
public class ListEventCommand extends Command {

    public static final String COMMAND_WORD = "list-events";

    public static final String MESSAGE_SUCCESS = "Listed all events";

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        model.clearSelectedEpicEvent();
        return new CommandResult(MESSAGE_SUCCESS);
    }


}
```
###### \java\seedu\address\logic\commands\ListRegistrantsCommand.java
``` java
/**
 * Lists all attendees of selected event in the event planner to the user.
 */
public class ListRegistrantsCommand extends Command {

    public static final String COMMAND_WORD = "list-registrants";

    public static final String MESSAGE_SUCCESS = "Listed all registrants";


    @Override
    public CommandResult execute() {
        model.updateFilteredAttendanceList(PREDICATE_SHOW_ALL_ATTENDEES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\SelectEventCommand.java
``` java
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
###### \java\seedu\address\logic\Logic.java
``` java
    /** Returns selected EpicEvent **/
    ObservableEpicEvent getSelectedEpicEvent();

    void setSelectedEpicEvent(int index);

    void setSelectedEpicEvent(EpicEvent epicEvent);
```
###### \java\seedu\address\logic\LogicManager.java
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
###### \java\seedu\address\logic\parser\FindRegistrantCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindRegistrantCommandParser object
 */
public class FindRegistrantCommandParser implements Parser<FindRegistrantCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindRegistrantCommand
     * and returns an FindRegistrantCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindRegistrantCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindRegistrantCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindRegistrantCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
```
###### \java\seedu\address\logic\parser\SelectEventCommandParser.java
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
###### \java\seedu\address\model\attendance\AttendanceNameContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class AttendanceNameContainsKeywordsPredicate implements Predicate<Attendance> {
    private final List<String> keywords;

    public AttendanceNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Attendance attendee) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(attendee.getPerson().getFullName().name,
                        keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AttendanceNameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((AttendanceNameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    public static EpicEvent getDummyEpicEvent() {
        return new EpicEvent(new Name("dummyEvent"), new HashSet<Tag>());
    }

```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    public ObservableList<Attendance> getAttendanceList() {
        return attendanceList.asObservableList();
    }

    public UniqueAttendanceList getUniqueAttendanceList() {
        return attendanceList;
    }

```
###### \java\seedu\address\model\event\ObservableEpicEvent.java
``` java
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
```
###### \java\seedu\address\model\Model.java
``` java
    ObservableEpicEvent getSelectedEpicEvent();

    void setSelectedEpicEvent(int index);

    void setSelectedEpicEvent(EpicEvent epicEvent);

    void updateFilteredAttendanceList(Predicate<Attendance> predicate);

    void visuallySelectEpicEvent(EpicEvent toAdd);

    void clearSelectedEpicEvent();
```
###### \java\seedu\address\model\ModelManager.java
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
    public void updateFilteredAttendanceList(Predicate<Attendance> predicate) {
        requireNonNull(predicate);
        selectedEpicEvent.getFilteredAttendees().setPredicate(predicate);
    }

    @Override
    public void visuallySelectEpicEvent(EpicEvent toSelect) {
        setSelectedEpicEvent(toSelect);
        int eventIndexInFilteredList = getFilteredEventList().indexOf(toSelect);
        if (eventIndexInFilteredList != -1) {
            EventsCenter.getInstance().post(new JumpToEventListRequestEvent(
                    Index.fromZeroBased(eventIndexInFilteredList)));
        } else {
            EventsCenter.getInstance().post(new ClearEventListSelectionEvent());
        }
    }

    @Override
    public void clearSelectedEpicEvent() {
        visuallySelectEpicEvent(EpicEvent.getDummyEpicEvent());
        EventsCenter.getInstance().post(new ClearEventListSelectionEvent());
    }

    @Override
    public ObservableEpicEvent getSelectedEpicEvent() {
        return selectedEpicEvent;
    }

```
###### \java\seedu\address\ui\AttendanceCard.java
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

}
```
###### \java\seedu\address\ui\AttendanceListPanel.java
``` java
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
```
###### \java\seedu\address\ui\EpicEventCard.java
``` java
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
###### \java\seedu\address\ui\EpicEventListPanel.java
``` java
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
```
###### \java\seedu\address\ui\MainWindow.java
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
###### \java\seedu\address\ui\PersonCard.java
``` java
    public PersonCard(Person person, int displayedIndex, String fxml) {
        super(fxml);
        this.person = person;
        initializeCardDetails(person, displayedIndex);
    }

    /** initilialize card labels **/
    private void initializeCardDetails(Person person, int displayedIndex) {
        id.setText(displayedIndex + ". ");
        name.setText(person.getFullName().name);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
```
###### \resources\view\MainWindow.fxml
``` fxml
          <VBox fx:id="epicEventList" minWidth="200" prefWidth="340" SplitPane.resizableWithParent="false">
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <StackPane fx:id="epicEventListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
          </VBox>

          <VBox fx:id="attendanceList" minWidth="280" prefWidth="340" SplitPane.resizableWithParent="false">
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <StackPane fx:id="attendanceListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
          </VBox>
```
