# raynoldng
###### \java\guitests\guihandles\AttendanceCardHandle.java
``` java
/**
 * Provides a handle to an attendance card in the attendance list panel.
 */
public class AttendanceCardHandle extends PersonCardHandle {

    private static final String IMAGEVIEW_FIELD_ID = "#attendanceToggleImage";

    private final ImageView attendanceToggleImage;

    public AttendanceCardHandle(Node cardNode) {
        super(cardNode);
        attendanceToggleImage = getChildNode(IMAGEVIEW_FIELD_ID);
    }

    public ImageView getAttendanceToggleImage() {
        return attendanceToggleImage;
    }
}
```
###### \java\guitests\guihandles\AttendanceListPanelHandle.java
``` java
/**
 * Provides a handle for {@code AttendanceListPanel} containing the list of {@code AttendanceCard}.
 */
public class AttendanceListPanelHandle extends NodeHandle<ListView<AttendanceCard>> {
    public static final String ATTENDANCE_LIST_VIEW_ID = "#attendanceListView";

    private Optional<AttendanceCard> lastRememberedSelectedAttedanceCard;

    public AttendanceListPanelHandle(ListView<AttendanceCard> attendanceListPanelNode) {
        super(attendanceListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code AttendanceCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public AttendanceCardHandle getHandleToSelectedCard() {
        List<AttendanceCard> attendanceList = getRootNode().getSelectionModel().getSelectedItems();

        if (attendanceList.size() != 1) {
            throw new AssertionError("Attendance list size expected 1.");
        }

        return new AttendanceCardHandle(attendanceList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<AttendanceCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the epicEvent.
     */
    public void navigateToCard(Attendance attendance) {
        List<AttendanceCard> cards = getRootNode().getItems();
        Optional<AttendanceCard> matchingCard = cards.stream().filter(card ->
                card.getAttendance().equals(attendance)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("Event does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the Attendace card handle of a Attendance associated with the {@code index} in the list.
     */
    public AttendanceCardHandle getAttendanceCardHandle(int index) {
        return getAttendanceCardHandle(getRootNode().getItems().get(index).getAttendance());
    }

    /**
     * Returns the {@code AttendanceCardHandle} of the specified {@code attendance} in the list.
     */
    public AttendanceCardHandle getAttendanceCardHandle(Attendance attendance) {
        Optional<AttendanceCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.getAttendance().equals(attendance))
                .map(card -> new AttendanceCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Attendee does not exist."));
    }

    /**
     * Selects the {@code AttendanceCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code AttendanceCard} in the list.
     */
    public void rememberSelectedAttendanceCard() {
        List<AttendanceCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedAttedanceCard = Optional.empty();
        } else {
            lastRememberedSelectedAttedanceCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code AttendanceCard} is different from the value remembered by the most recent
     * {@code rememberSelectedAttendanceCard()} call.
     */
    public boolean isSelectedAttendanceCardChanged() {
        List<AttendanceCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedAttedanceCard.isPresent();
        } else {
            return !lastRememberedSelectedAttedanceCard.isPresent()
                    || !lastRememberedSelectedAttedanceCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
```
###### \java\guitests\guihandles\AttendanceListPanelHeaderHandle.java
``` java
/**
 *  Handle to header text of attendance list panel to check for content correctness
 */
public class AttendanceListPanelHeaderHandle extends NodeHandle<Label> {

    public static final String ATTENDANCE_STATUS_ID = "#attendanceStatus";
    public static final String ATTENDANCE_STATUS_FORMAT = "Attendees%s: (%d/%d)";

    public AttendanceListPanelHeaderHandle(Label rootNode) {
        super(rootNode);
    }

    /**
     * Returns the text in the header.
     */
    public String getText() {
        return getRootNode().getText();
    }
}
```
###### \java\guitests\guihandles\EpicEventCardHandle.java
``` java
/**
 * Provides a handle to a person card in the event list panel.
 */
public class EpicEventCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final List<Label> tagLabels;

    public EpicEventCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.nameLabel = getChildNode(NAME_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
```
###### \java\seedu\address\logic\commands\FindRegistrantCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindRegistrantCommand}.
 */
public class FindRegistrantCommandTest {
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setSelectedEpicEvent(GRADUATIONAY18_INDEX);
    }

    @Test
    public void equals() {
        AttendanceNameContainsKeywordsPredicate firstPredicate =
                new AttendanceNameContainsKeywordsPredicate(Collections.singletonList("first"));
        AttendanceNameContainsKeywordsPredicate secondPredicate =
                new AttendanceNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindRegistrantCommand firstFindRegistrantCommand = new FindRegistrantCommand(firstPredicate);
        FindRegistrantCommand secondFindRegistrantCommand = new FindRegistrantCommand(secondPredicate);

        // same object -> returns true
        assertTrue(firstFindRegistrantCommand.equals(firstFindRegistrantCommand));

        // same values -> returns true
        FindRegistrantCommand firstFindRegistrantCommandCopy = new FindRegistrantCommand(firstPredicate);
        assertTrue(firstFindRegistrantCommand.equals(firstFindRegistrantCommandCopy));

        // different types -> returns false
        assertFalse(firstFindRegistrantCommand.equals(1));

        // null -> returns false
        assertFalse(firstFindRegistrantCommand.equals(null));

        // different person -> returns false
        assertFalse(firstFindRegistrantCommand.equals(secondFindRegistrantCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindRegistrantCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindRegistrantCommand command = prepareCommand("Kurz Elle Kunz");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(new Attendance(CARL, GRADUATIONAY18),
                new Attendance(ELLE, GRADUATIONAY18), new Attendance(FIONA, GRADUATIONAY18)));

    }

    /**
     * Parses {@code userInput} into a {@code FindAttendeeCommand}.
     */
    private FindRegistrantCommand prepareCommand(String userInput) {
        FindRegistrantCommand command =
                new FindRegistrantCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(userInput
                        .split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code EventPlanner} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindRegistrantCommand command, String expectedMessage,
                                      List<Attendance> expectedList) {
        EventPlanner expectedEventPlanner = new EventPlanner(model.getEventPlanner());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getSelectedEpicEvent().getFilteredAttendees());
        assertEquals(expectedEventPlanner, model.getEventPlanner());
    }
}
```
###### \java\seedu\address\logic\commands\ListRegistrantsCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListRegistrantsCommand.
 */
public class ListRegistrantsCommandTest {

    private Model model;
    private Model expectedModel;
    private ListRegistrantsCommand listRegistrantsCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setSelectedEpicEvent(GRADUATIONAY18_INDEX);
        expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());

        listRegistrantsCommand = new ListRegistrantsCommand();
        listRegistrantsCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listRegistrantsCommand, model, ListRegistrantsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showAttendeeAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(listRegistrantsCommand, model, ListRegistrantsCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
```
###### \java\seedu\address\logic\commands\SelectEventCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code SelectEventCommand}.
 */
public class SelectEventCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastEpicEventIndex = Index.fromOneBased(model.getFilteredEventList().size());
        assertExecutionSuccess(INDEX_FIRST_EVENT);
        assertExecutionSuccess(INDEX_THIRD_EVENT);

        assertExecutionSuccess(lastEpicEventIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        assertExecutionSuccess(INDEX_FIRST_EVENT);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        Index outOfBoundsIndex = INDEX_SECOND_EVENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getEventPlanner().getEventList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectEventCommand selectFirstEventCommand = new SelectEventCommand(INDEX_FIRST_EVENT);
        SelectEventCommand selectSecondEventCommand = new SelectEventCommand(INDEX_SECOND_EVENT);

        // same object -> returns true
        assertTrue(selectFirstEventCommand.equals(selectFirstEventCommand));

        // same values -> returns true
        SelectEventCommand selectFirstEventCommandCopy = new SelectEventCommand(INDEX_FIRST_EVENT);
        assertTrue(selectFirstEventCommand.equals(selectFirstEventCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstEventCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstEventCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstEventCommand.equals(selectSecondEventCommand));
    }

    /**
     * Executes a {@code SelectEventCommand} with the given {@code index}, and checks that
     * {@code JumpToListRequestEvent} is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectEventCommand selectEventCommand = prepareCommand(index);

        try {
            CommandResult commandResult = selectEventCommand.execute();
            assertEquals(String.format(SelectEventCommand.MESSAGE_SELECT_EVENT_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToEventListRequestEvent lastEvent = (JumpToEventListRequestEvent)
                eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }
    /**
     * Executes a {@code SelectEventCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectEventCommand selectEventCommand = prepareCommand(index);

        try {
            selectEventCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectEventCommand} with parameters {@code index}.
     */
    private SelectEventCommand prepareCommand(Index index) {
        SelectEventCommand selectEventCommand = new SelectEventCommand(index);
        selectEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectEventCommand;
    }
}
```
###### \java\seedu\address\logic\parser\FindRegistrantCommandParserTest.java
``` java
public class FindRegistrantCommandParserTest {

    private FindRegistrantCommandParser parser = new FindRegistrantCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindRegistrantCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindRegistrantCommand expectedFindRegistrantCommand =
                new FindRegistrantCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList("John", "Cena")));
        assertParseSuccess(parser, "John Cena", expectedFindRegistrantCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n John \n \t Cena \t", expectedFindRegistrantCommand);
    }

}
```
###### \java\seedu\address\logic\parser\SelectEventCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;

import org.junit.Test;

import seedu.address.logic.commands.SelectEventCommand;

/**
 * Test scope: similar to {@code DeletePersonCommandParserTest}.
 * @see DeletePersonCommandParserTest
 */
public class SelectEventCommandParserTest {

    private SelectEventCommandParser parser = new SelectEventCommandParser();

    @Test
    public void parse_validArgs_returnsSelectEventCommand() {
        assertParseSuccess(parser, "1", new SelectEventCommand(INDEX_FIRST_EVENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectEventCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\testutil\TypicalPersons.java
``` java
    /**
     * Returns an {@code EventPlanner} with all the typical persons.
     */
    public static EventPlanner getTypicalAddressBookWithoutEvents() {
        EventPlanner ab = new EventPlanner();
        for (Person person : getTypicalPersons()) {
            try {
                ab.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }
```
###### \java\seedu\address\ui\AttendanceCardTest.java
``` java
public class AttendanceCardTest extends GuiUnitTest {

    @Test
    public void display() {
        EpicEvent event = TypicalEpicEvents.GRADUATIONAY18;

        Attendance aliceAttendance = event.getAttendanceList().get(0);
        AttendanceCard attendanceCard = new AttendanceCard(aliceAttendance, 0);
        uiPartRule.setUiPart(attendanceCard);
        assertAttendanceCardDisplay(attendanceCard, aliceAttendance, 0);
    }

    @Test
    public void equals() {
        EpicEvent event = TypicalEpicEvents.CAREERTALK;
        try {
            event.registerPerson(TypicalPersons.ALICE);
            event.registerPerson(TypicalPersons.BOB);
        } catch (DuplicateAttendanceException e) {
            e.printStackTrace();
        }
        // there should only be one attendee
        assertEquals(event.getAttendanceList().size(), 2);
        Attendance aliceAttendance = event.getAttendanceList().get(0);
        Attendance bobAttendance = event.getAttendanceList().get(1);

        AttendanceCard attendanceCard = new AttendanceCard(aliceAttendance, 0);
        AttendanceCard copy = new AttendanceCard(aliceAttendance, 0);

        // same attendance, same index -> return true
        assertTrue(attendanceCard.equals(copy));

        // same object -> returns true
        assertTrue(attendanceCard.equals(attendanceCard));

        // null -> returns false
        assertFalse(attendanceCard.equals(null));

        // different types -> return false
        assertFalse(attendanceCard.equals(0));

        // different card, same index -> returns false
        assertFalse(attendanceCard.equals(new AttendanceCard(bobAttendance, 0)));

        // same event, different index -> returns false
        assertFalse(attendanceCard.equals(new AttendanceCard(aliceAttendance, 1)));
    }

    /**
     * Asserts that {@code attendanceCard} displays the details of {@code expectedAttendance} correctly and matches
     * {@code expectedId}.
     */
    private void assertAttendanceCardDisplay(AttendanceCard attendanceCard, Attendance expectedAttendance,
                                             int expectedId) {
        guiRobot.pauseForHuman();

        AttendanceCardHandle attendanceCardHandle = new AttendanceCardHandle(attendanceCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", attendanceCardHandle.getId());

        // verify person details are displayed correctly
        assertAttendanceEventCardDisplaysAttendance(expectedAttendance, attendanceCardHandle);
    }
}
```
###### \java\seedu\address\ui\AttendanceListPanelTest.java
``` java

/**
 * Panel containing the list of attendees.
 */
public class AttendanceListPanelTest extends GuiUnitTest {
    private static final ObservableEpicEvent selectedEvent =
            new ObservableEpicEvent(TypicalEpicEvents.GRADUATIONAY18);
    private static final JumpToAttendanceListRequestEvent JUMP_TO_SECOND_ATTENDANCE =
            new JumpToAttendanceListRequestEvent(INDEX_SECOND_PERSON);

    private ObservableList<Attendance> attendanceList = selectedEvent.getEpicEvent().getAttendanceList();

    private AttendanceListPanelHandle attendanceListPanelHandle;

    @Before
    public void setUp() {
        AttendanceListPanel attendanceListPanel = new AttendanceListPanel(selectedEvent);
        uiPartRule.setUiPart(attendanceListPanel);

        attendanceListPanelHandle = new AttendanceListPanelHandle(getChildNode(attendanceListPanel.getRoot(),
                AttendanceListPanelHandle.ATTENDANCE_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < attendanceList.size(); i++) {
            attendanceListPanelHandle.navigateToCard(attendanceList.get(i));
            Attendance expectedAttendance = attendanceList.get(i);
            AttendanceCardHandle actualCard = attendanceListPanelHandle.getAttendanceCardHandle(i);

            assertAttendanceEventCardDisplaysAttendance(expectedAttendance, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_ATTENDANCE);
        guiRobot.pauseForHuman();

        AttendanceCardHandle expectedCard =
                attendanceListPanelHandle.getAttendanceCardHandle(INDEX_SECOND_EVENT.getZeroBased());
        AttendanceCardHandle selectedCard = attendanceListPanelHandle.getHandleToSelectedCard();
        assertAttendanceCardEquals(expectedCard, selectedCard);
    }
}
```
###### \java\seedu\address\ui\EpicEventCardTest.java
``` java
public class EpicEventCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        EpicEvent eventWithoutTags = new EpicEventBuilder().withTags(new String[0]).build();
        EpicEventCard eventCard = new EpicEventCard(eventWithoutTags, 1);
        uiPartRule.setUiPart(eventCard);
        assertEpicEventCardDisplay(eventCard, eventWithoutTags, 1);

        // with tags
        EpicEvent eventWithTags = new EpicEventBuilder().build();
        eventCard = new EpicEventCard(eventWithTags, 2);
        uiPartRule.setUiPart(eventCard);
        assertEpicEventCardDisplay(eventCard, eventWithTags, 2);
    }

    @Test
    public void equals() {
        EpicEvent event = new EpicEventBuilder().build();
        EpicEventCard eventCard = new EpicEventCard(event, 0);

        // same event, same index -> return true
        EpicEventCard copy = new EpicEventCard(event, 0);
        assertTrue(eventCard.equals(eventCard));

        // same object -> returns true
        assertTrue(eventCard.equals(eventCard));

        // null -> returns false
        assertFalse(eventCard.equals(null));

        // different types -> return false
        assertFalse(eventCard.equals(0));

        // different card, same index -> returns false
        EpicEvent differentEvent = new EpicEventBuilder().withName("differentEvent").build();
        assertFalse(eventCard.equals(new EpicEventCard(differentEvent, 0)));

        // same event, different index -> returns false
        assertFalse(eventCard.equals(new EpicEventCard(event, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertEpicEventCardDisplay(EpicEventCard eventCard, EpicEvent expectedEvent, int expectedId) {
        guiRobot.pauseForHuman();

        EpicEventCardHandle epicCardHandle = new EpicEventCardHandle(eventCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", epicCardHandle.getId());

        // verify person details are displayed correctly
        assertEpicEventCardDisplaysEpicEvent(expectedEvent, epicCardHandle);
    }
}
```
###### \java\seedu\address\ui\EpicEventListPanelTest.java
``` java
/**
 * Panel containing the list of events.
 */
public class EpicEventListPanelTest extends GuiUnitTest {
    private static final ObservableList<EpicEvent> TYPICAL_EVENTS =
            FXCollections.observableList(getTypicalEvents());

    private static final JumpToEventListRequestEvent JUMP_TO_SECOND_EVENT =
            new JumpToEventListRequestEvent(INDEX_SECOND_EVENT);

    private EpicEventListPanelHandle epicEventListPanelHandle;

    @Before
    public void setUp() {
        EpicEventListPanel epicEventListPanel = new EpicEventListPanel(TYPICAL_EVENTS);
        uiPartRule.setUiPart(epicEventListPanel);

        epicEventListPanelHandle = new EpicEventListPanelHandle(getChildNode(epicEventListPanel.getRoot(),
                EpicEventListPanelHandle.EPIC_EVENT_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_EVENTS.size(); i++) {
            epicEventListPanelHandle.navigateToCard(TYPICAL_EVENTS.get(i));
            EpicEvent expectedEpicEvent = TYPICAL_EVENTS.get(i);
            EpicEventCardHandle actualCard = epicEventListPanelHandle.getEpicEventCardHandle(i);

            assertEpicEventCardDisplaysEpicEvent(expectedEpicEvent, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        EpicEventCardHandle expectedCard =
                epicEventListPanelHandle.getEpicEventCardHandle(INDEX_SECOND_EVENT.getZeroBased());
        EpicEventCardHandle selectedCard = epicEventListPanelHandle.getHandleToSelectedCard();
        assertEpicEventCardEquals(expectedCard, selectedCard);
    }
}
```
###### \java\systemtests\EventPlannerSystemTest.java
``` java
    /**
     * Asserts that the {@code AttendanceListPanel} displays the expected message, i.e.: filtered if so and attendance
     * count is displayed correctly
     */
    protected void assertAttendanceListHeaderDisplaysExpected(Model expectedModel) {
        boolean isFiltered = expectedModel.getSelectedEpicEvent().getFilteredAttendees().getPredicate()
                != PREDICATE_SHOW_ALL_ATTENDEES;

        if (isFiltered) {
            assert(getAttendanceListPanelHeader().getText().contains("(filtered)"));
        }

        int numAttended = (int) expectedModel.getSelectedEpicEvent().getFilteredAttendees().stream()
                .filter(attendance -> attendance.hasAttended())
                .count();
        int total = expectedModel.getSelectedEpicEvent().getFilteredAttendees().size();
        String expected = String.format(ATTENDANCE_STATUS_FORMAT, isFiltered ? "(filtered)" : "", numAttended,
                total);
        assertEquals(expected, getAttendanceListPanelHeader().getText());

    }
```
###### \java\systemtests\FindRegistrantCommandSystemTest.java
``` java
public class FindRegistrantCommandSystemTest extends EventPlannerSystemTest {

    @Test
    public void find() {

        // First: assert that first event (Graduation) is the selected event
        assert(getModel().getSelectedEpicEvent().getEpicEvent().equals(GRADUATIONAY18));

        Model expectedModel = getModel();

        /* Case: find person where person list is not displaying the person we are finding -> 1 person found */
        String command = FindRegistrantCommand.COMMAND_WORD + " Carl";
        Attendance carlAttendance = new Attendance(CARL, GRADUATIONAY18);
        ModelHelper.setFilteredAttendanceList(expectedModel, carlAttendance);
        assertCommandSuccess(command, expectedModel);

        /* Case: find multiple persons in address book, 2 keywords -> 2 persons found */
        command = FindRegistrantCommand.COMMAND_WORD + " Benson Daniel";
        Attendance bensonAttendance = new Attendance(BENSON, GRADUATIONAY18);
        Attendance danielAttendance = new Attendance(DANIEL, GRADUATIONAY18);
        ModelHelper.setFilteredAttendanceList(expectedModel, bensonAttendance, danielAttendance);
        assertCommandSuccess(command, expectedModel);

        /* Case: switch to event that has only has registered */
        getModel().setSelectedEpicEvent(ORIENTATION_INDEX);
        Attendance hoonOrientationAttendance = new Attendance(HOON, ORIENTATION);
        command = FindRegistrantCommand.COMMAND_WORD + " Hoon";
        ModelHelper.setFilteredAttendanceList(expectedModel, hoonOrientationAttendance);
        assertCommandSuccess(command, expectedModel);

        /* Case: mixed case command word -> rejected */
        command = "FiNd-AttendAnCe Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_PERSONS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        FilteredList<Attendance> filteredList = expectedModel.getSelectedEpicEvent().getFilteredAttendees();
        String expectedResultMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW, filteredList.size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertAttendanceListHeaderDisplaysExpected(getModel());
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
###### \java\systemtests\ModelHelper.java
``` java
    public static void setEpicEventFilteredList(Model model, List<EpicEvent> toDisplay) {
        Optional<Predicate<EpicEvent>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredEventList(predicate.orElse(PREDICATE_MATCHING_NO_EPIC_EVENT));
    }

    public static void setFilteredAttendanceList(Model model, List<Attendance> toDisplay) {
        Optional<Predicate<Attendance>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredAttendanceList(predicate.orElse(PREDICATE_MATCHING_NO_ATTENDANCE));
    }

    public static void setFilteredAttendanceList(Model model, Attendance... toDisplay) {
        setFilteredAttendanceList(model, Arrays.asList(toDisplay));
    }
```
