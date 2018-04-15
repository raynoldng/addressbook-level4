# william6364
###### \java\seedu\address\logic\commands\AddEventCommandIntegrationTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code AddEventCommand}.
 */
public class AddEventCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newEvent_success() throws Exception {
        EpicEvent validEvent = new EpicEventBuilder().build();

        Model expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());
        expectedModel.addEvent(validEvent);

        assertCommandSuccess(prepareCommand(validEvent, model), model,
                String.format(AddEventCommand.MESSAGE_SUCCESS, validEvent), expectedModel);
    }

    @Test
    public void execute_duplicateEvent_throwsCommandException() throws Exception {
        EpicEvent validEvent = new EpicEventBuilder().build();
        model.addEvent(validEvent);

        EpicEvent eventInList = model.getEventPlanner().getEventList().get(0);
        assertCommandFailure(prepareCommand(eventInList, model), model, AddEventCommand.MESSAGE_DUPLICATE_EVENT);
    }

    /**
     * Generates a new {@code AddEventCommand} which upon execution, adds {@code event} into the {@code model}.
     */
    private AddEventCommand prepareCommand(EpicEvent event, Model model) {
        AddEventCommand command = new AddEventCommand(event);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\AddEventCommandTest.java
``` java

public class AddEventCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
        model.setSelectedEpicEvent(INDEX_FIRST_EVENT.getZeroBased());
    }

    /**
     * Executes a {@code AddEventCommand} with the given {@code index},
     * and checks that the event is correctly added
     */
    @Test
    public void execute_eventAcceptedByModel_addSuccessful() {
        EpicEvent event = new EpicEvent(new Name("New Event"), new TreeSet<>());
        AddEventCommand addEventCommand = prepareCommand(event);
        try {
            CommandResult commandResult = addEventCommand.execute();
            assertEquals(String.format(AddEventCommand.MESSAGE_SUCCESS, event), commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        assertTrue(model.getEventList().contains(event));
    }

    /**
     * Executes an {@code AddEventCommand} with a duplicate event and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    @Test
    public void execute_duplicateEvent_throwsCommandException() {
        AddEventCommand addEventCommand = prepareCommand(model.getEventList().get(0));
        try {
            addEventCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(AddEventCommand.MESSAGE_DUPLICATE_EVENT, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    @Test
    public void equals() {
        EpicEvent eventA = new EpicEventBuilder().withName("Event A").build();
        EpicEvent eventB = new EpicEventBuilder().withName("Event B").build();
        AddEventCommand addEventACommand = new AddEventCommand(eventA);
        AddEventCommand addEventBCommand = new AddEventCommand(eventB);

        // same object -> returns true
        assertTrue(addEventACommand.equals(addEventACommand));

        // same values -> returns true
        AddEventCommand addEventACommandCopy = new AddEventCommand(eventA);
        assertTrue(addEventACommand.equals(addEventACommandCopy));

        // different types -> returns false
        assertFalse(addEventACommand.equals(1));

        // null -> returns false
        assertFalse(addEventACommand.equals(null));

        // different person -> returns false
        assertFalse(addEventACommand.equals(addEventBCommand));
    }

    /**
     * Returns a {@code AddEventCommand} with parameters {@code event}.
     */
    private AddEventCommand prepareCommand(EpicEvent event) {
        AddEventCommand addEventCommand = new AddEventCommand(event);
        addEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addEventCommand;
    }
}
```
###### \java\seedu\address\logic\commands\ToggleAttendanceCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code ToggleAttendanceCommand}.
 */
public class ToggleAttendanceCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
        model.setSelectedEpicEvent(INDEX_FIRST_EVENT.getZeroBased());
    }

    @Test
    public void execute_validIndex_success() {
        Index lastAttendanceIndex = Index.fromOneBased(model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().size());
        assertExecutionSuccess(INDEX_FIRST_ATTENDANCE);
        assertExecutionSuccess(INDEX_THIRD_ATTENDANCE);
        assertExecutionSuccess(lastAttendanceIndex);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().size() + 1);
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(outOfBoundsIndex);

        assertExecutionFailure(toggleAttendanceCommand, Messages.MESSAGE_INVALID_ATTENDANCE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidEvent_failure() {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(INDEX_FIRST_ATTENDANCE);
        try {
            toggleAttendanceCommand.preprocessUndoableCommand();
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        try {
            model.deleteEvent(model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()));
        } catch (EventNotFoundException e) {
            throw new AssertionError("Deleting of event should not fail");
        }
        try {
            toggleAttendanceCommand.executeUndoableCommand();
        } catch (CommandException ce) {
            assertEquals(Messages.MESSAGE_EVENT_NOT_FOUND, ce.getMessage());
        }
    }

    @Test
    public void execute_invalidPerson_failure() {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(INDEX_FIRST_ATTENDANCE);
        try {
            toggleAttendanceCommand.preprocessUndoableCommand();
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        try {
            model.deregisterPersonFromEvent(model.getSelectedEpicEvent().getEpicEvent().getAttendanceList()
                    .get(INDEX_FIRST_ATTENDANCE.getZeroBased()).getPerson(),
                    model.getSelectedEpicEvent().getEpicEvent());
        } catch (EventNotFoundException | PersonNotFoundInEventException e) {
            throw new AssertionError(
                    "Deregistering of person should not fail");
        }
        try {
            toggleAttendanceCommand.executeUndoableCommand();
        } catch (CommandException ce) {
            assertEquals(Messages.MESSAGE_PERSON_NOT_IN_EVENT, ce.getMessage());
        }
    }

    @Test
    public void equals() {
        ToggleAttendanceCommand toggleAttendanceCommandA = new ToggleAttendanceCommand(INDEX_FIRST_ATTENDANCE);
        ToggleAttendanceCommand toggleAttendanceCommandB = new ToggleAttendanceCommand(INDEX_SECOND_ATTENDANCE);

        // same object -> returns true
        assertTrue(toggleAttendanceCommandA.equals(toggleAttendanceCommandA));

        // same values -> returns true
        ToggleAttendanceCommand toggleAttendanceCommandCopy = new ToggleAttendanceCommand(INDEX_FIRST_ATTENDANCE);
        assertTrue(toggleAttendanceCommandA.equals(toggleAttendanceCommandCopy));

        // different types -> returns false
        assertFalse(toggleAttendanceCommandA.equals(1));

        // null -> returns false
        assertFalse(toggleAttendanceCommandA.equals(null));

        // different index -> returns false
        assertFalse(toggleAttendanceCommandA.equals(toggleAttendanceCommandB));
    }

    /**
     * Executes a {@code ToggleAttendanceCommand} with the given {@code index},
     * and checks that the attendance is correctly toggled
     */
    private void assertExecutionSuccess(Index index) {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(index);
        boolean initialHasAttended = model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().get(index.getZeroBased()).hasAttended();
        try {
            CommandResult commandResult = toggleAttendanceCommand.execute();
            assertEquals(String.format(ToggleAttendanceCommand.MESSAGE_SUCCESS,
                    toggleAttendanceCommand.getAttendanceToToggle().getPerson().getFullName(),
                    toggleAttendanceCommand.getAttendanceToToggle().getEvent().getName()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        // check if the correct attendance object was toggled
        assertTrue(toggleAttendanceCommand.getAttendanceToToggle().equals(model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().get(index.getZeroBased())));

        // check if the toggling occurred correctly
        assertTrue(initialHasAttended != model.getSelectedEpicEvent().getEpicEvent()
                .getAttendanceList().get(index.getZeroBased()).hasAttended());
    }

    /**
     * Executes a {@code ToggleAttendanceCommand} and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(ToggleAttendanceCommand toggleAttendanceCommand, String expectedMessage) {
        try {
            toggleAttendanceCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code ToggleAttendanceCommand} with parameters {@code index}.
     */
    private ToggleAttendanceCommand prepareCommand(Index index) {
        ToggleAttendanceCommand toggleAttendanceCommand = new ToggleAttendanceCommand(index);
        toggleAttendanceCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return toggleAttendanceCommand;
    }
}
```
###### \java\seedu\address\logic\parser\AddEventCommandParserTest.java
``` java

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        EpicEvent expectedEvent = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
                .withTags(VALID_EVENT_TAG_GRADUATION).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEvent));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_SEMINAR + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEvent));

        // multiple tags - all accepted
        EpicEvent expectedEventMultipleTags = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
                .withTags(VALID_EVENT_TAG_SEMINAR, VALID_EVENT_TAG_GRADUATION).build();
        assertParseSuccess(parser, NAME_DESC_GRADUATION + TAG_DESC_SEMINAR + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEventMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        EpicEvent expectedEvent = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION).withTags().build();
        assertParseSuccess(parser, NAME_DESC_GRADUATION , new AddEventCommand(expectedEvent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_PERSON_NAME_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + TAG_DESC_SEMINAR + TAG_DESC_GRADUATION,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_GRADUATION + INVALID_TAG_DESC + VALID_EVENT_TAG_GRADUATION,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_TAG_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_GRADUATION + TAG_DESC_SEMINAR
                + TAG_DESC_GRADUATION, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\address\logic\parser\ToggleAttendanceCommandParserTest.java
``` java

/**
 * Test scope: similar to {@code SelectCommandParserTest}.
 * @see SelectCommandParserTest
 */
public class ToggleAttendanceCommandParserTest {

    private ToggleAttendanceCommandParser parser = new ToggleAttendanceCommandParser();

    @Test
    public void parse_validArgs_returnsToggleAttendanceCommand() {
        assertParseSuccess(parser, "1", new ToggleAttendanceCommand(INDEX_FIRST_ATTENDANCE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ToggleAttendanceCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\model\EventPlannerTest.java
``` java
    @Test
    public void resetData_withDuplicateEvents_throwsAssertionError() {
        // Repeat GRADUATION twice
        List<Person> newPersons = Arrays.asList();
        List<EpicEvent> newEvents = Arrays.asList(GRADUATION, GRADUATION);
        List<Tag> newTags = new ArrayList<>(GRADUATION.getTags());
        EventPlannerStub newData = new EventPlannerStub(newPersons, newEvents, newTags, newTags);

        thrown.expect(AssertionError.class);
        eventPlanner.resetData(newData);
    }

```
###### \java\seedu\address\model\EventPlannerTest.java
``` java

    @Test
    public void getEventList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getEventList().remove(0);
    }

    @Test
    public void getPersonTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getPersonTagList().remove(0);
    }

    @Test
    public void getEventTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getEventTagList().remove(0);
    }

    /**
     * A stub ReadOnlyEventPlanner whose persons and tags lists can violate interface constraints.
     */
    private static class EventPlannerStub implements ReadOnlyEventPlanner {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<EpicEvent> events = FXCollections.observableArrayList();
        private final ObservableList<Tag> personTags = FXCollections.observableArrayList();
        private final ObservableList<Tag> eventTags = FXCollections.observableArrayList();

        EventPlannerStub(Collection<Person> persons, Collection<EpicEvent> events, Collection<? extends Tag> personTags,
                         Collection<? extends Tag> eventTags) {
            this.persons.setAll(persons);
            this.events.setAll(events);
            this.personTags.setAll(personTags);
            this.eventTags.setAll(eventTags);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<EpicEvent> getEventList() {
            return events;
        }

        @Override
        public ObservableList<Tag> getPersonTagList() {
            return personTags;
        }

        @Override
        public ObservableList<Tag> getEventTagList() {
            return eventTags;
        }
    }

}
```
###### \java\seedu\address\model\UniqueAttendanceListTest.java
``` java

public class UniqueAttendanceListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAttendanceList uniqueAttendanceList = new UniqueAttendanceList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueAttendanceList.asObservableList().remove(0);
    }
}
```
###### \java\seedu\address\model\UniqueEpicEventListTest.java
``` java

public class UniqueEpicEventListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueEpicEventList uniqueEpicEventList = new UniqueEpicEventList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueEpicEventList.asObservableList().remove(0);
    }
}
```
###### \java\seedu\address\testutil\EpicEventBuilder.java
``` java

/**
 * A utility class to help with building EpicEvent objects.
 */
public class EpicEventBuilder {

    public static final String DEFAULT_NAME = "MOCK AY201718 Graduation Ceremony";
    public static final String DEFAULT_TAGS = "graduation";

    private Name name;
    private Set<Tag> tags;
    private List<Person> attendees;

    public EpicEventBuilder() {
        name = new Name(DEFAULT_NAME);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
    }

    /**
     * Initializes the EpicEventBuilder with the data of {@code eventToCopy}.
     */
    public EpicEventBuilder(EpicEvent eventToCopy) {
        name = eventToCopy.getName();
        tags = new HashSet<>(eventToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code attendees} of the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withAttendees(List<Person> attendees) {
        this.attendees = attendees;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Builds the {@code EpicEvent} with the {@code Name}, {@code tags}, {@code attendees} set
     */
    public EpicEvent build() {
        EpicEvent event = new EpicEvent(name, tags);
        if (attendees != null) {
            for (Person person : attendees) {
                try {
                    event.registerPerson(person);
                } catch (DuplicateAttendanceException e) {
                    throw new AssertionError("Same person registered multiple twice");
                }
            }
        }
        return event;
    }

}
```
###### \java\seedu\address\testutil\EpicEventUtil.java
``` java

/**
 * A utility class for EpicEvent.
 */
public class EpicEventUtil {

    /**
     * Returns an add command string for adding the {@code event}.
     */
    public static String getAddEventCommand(EpicEvent event) {
        return AddEventCommand.COMMAND_WORD + " " + getEventDetails(event);
    }

    /**
     * Returns the part of command string for the given {@code event}'s details.
     */
    public static String getEventDetails(EpicEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + event.getName().name + " ");
        event.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
```
###### \java\systemtests\AddEventCommandSystemTest.java
``` java

public class AddEventCommandSystemTest extends EventPlannerSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add an event without tags to a non-empty address book, command with leading spaces and trailing spaces
         * -> added
         */
        EpicEvent toAdd = GRADUATION;
        String command = "   " + AddEventCommand.COMMAND_WORD + "  " + NAME_DESC_GRADUATION + " " + TAG_DESC_GRADUATION;
        assertCommandSuccess(command, toAdd);

        // Case: undo adding Graduation to the list -> Graduation deleted
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        // Case: redo adding Graduation to the list -> Graduation added again
        command = RedoCommand.COMMAND_WORD;
        model.addEvent(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add an event with a different name -> added */
        assertCommandSuccess(SEMINAR);

        /* Case: add to empty address book -> added */
        clearEventPlanner();
        assertCommandSuccess(MATHOLYMPIAD);

        /* Case: add an event with tags, command with parameters in reverse order -> added */
        toAdd = SEMINAR;
        command = AddEventCommand.COMMAND_WORD + TAG_DESC_SEMINAR + NAME_DESC_SEMINAR;
        assertCommandSuccess(command, toAdd);

        /* Case: add an event, missing tags -> added */
        assertCommandSuccess(ORIENTATION);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the event list before adding -> added */
        showEventsWithName(KEYWORD_MATCHING_OLYMPIAD);
        assertCommandSuccess(GRADUATION);

        /* ------------------------ Perform add operation while an event card is selected --------------------------- */

        /* Case: selects first card in the event list, add an event -> added, card selection remains unchanged */
        selectEvent(Index.fromOneBased(1));
        assertCommandSuccess(CAREERTALK);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate event -> rejected */
        command = EpicEventUtil.getAddEventCommand(SEMINAR);
        assertCommandFailure(command, AddEventCommand.MESSAGE_DUPLICATE_EVENT);

        /* Case: add a duplicate event except with different tags -> rejected */
        // "graduation" is an existing tag used in the default model, see TypicalEpicEvents#GRADUATION
        // This test will fail if a new tag that is not in the model is used, see the bug documented in
        // EventPlanner#addEvent(EpicEvent)
        command = EpicEventUtil.getAddEventCommand(ORIENTATION) + " " + PREFIX_TAG.getPrefix() + "graduation";
        assertCommandFailure(command, AddEventCommand.MESSAGE_DUPLICATE_EVENT);

        /* Case: missing name -> rejected */
        command = AddEventCommand.COMMAND_WORD + TAG_DESC_GRADUATION;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds-event " + EpicEventUtil.getEventDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddEventCommand.COMMAND_WORD + INVALID_NAME_DESC;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        command = AddEventCommand.COMMAND_WORD + NAME_DESC_GRADUATION + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddEventCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddEventCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code EventListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(EpicEvent toAdd) {
        assertCommandSuccess(EpicEventUtil.getAddEventCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(EpicEvent)}. Executes {@code command}
     * instead.
     * @see AddEventCommandSystemTest#assertCommandSuccess(EpicEvent)
     */
    private void assertCommandSuccess(String command, EpicEvent toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addEvent(toAdd);
        } catch (DuplicateEventException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddEventCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, EpicEvent)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code EventListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddEventCommandSystemTest#assertCommandSuccess(String, EpicEvent)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code EventListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EventPlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
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
###### \java\systemtests\EventPlannerSystemTest.java
``` java
    /**
     * Displays all events with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showEventsWithName(String keyword) {
        executeCommand(FindEventCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredEventList().size()
                < getModel().getEventPlanner().getEventList().size());
    }

    /**
     * Selects the event at {@code index} of the displayed list.
     */
    protected void selectEvent(Index index) {
        executeCommand(SelectEventCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getEventListPanel().getSelectedCardIndex());
    }

```
