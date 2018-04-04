# william6364
###### /java/seedu/address/logic/parser/AddEventCommandParserTest.java
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
###### /java/seedu/address/logic/parser/ToggleAttendanceCommandParserTest.java
``` java

/**
 * Test scope: similar to {@code SelectCommandParserTest}.
 * @see SelectCommandParserTest
 */
public class ToggleAttendanceCommandParserTest {

    private ToggleAttendanceCommandParser parser = new ToggleAttendanceCommandParser();

    @Test
    public void parse_validArgs_returnsSelectCommand() {
        assertParseSuccess(parser, "1", new ToggleAttendanceCommand(INDEX_FIRST_ATTENDANCE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ToggleAttendanceCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/commands/AddEventCommandIntegrationTest.java
``` java

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
###### /java/seedu/address/logic/commands/AddEventCommandTest.java
``` java

public class AddEventCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullEvent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddEventCommand(null);
    }

    @Test
    public void execute_eventAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingEventAdded modelStub = new ModelStubAcceptingEventAdded();
        EpicEvent validEvent = new EpicEventBuilder().build();

        CommandResult commandResult = getAddEventCommandForEpicEvent(validEvent, modelStub).execute();

        assertEquals(String.format(AddEventCommand.MESSAGE_SUCCESS, validEvent), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validEvent), modelStub.eventsAdded);
    }

    @Test
    public void execute_duplicateEvent_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateEventException();
        EpicEvent validEvent = new EpicEventBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddEventCommand.MESSAGE_DUPLICATE_EVENT);

        getAddEventCommandForEpicEvent(validEvent, modelStub).execute();
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
     * Generates a new AddEventCommand with the details of the given event.
     */
    private AddEventCommand getAddEventCommandForEpicEvent(EpicEvent event, Model model) {
        AddEventCommand command = new AddEventCommand(event);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A Model stub that always throw a DuplicateEventException when trying to add a event.
     */
    private class ModelStubThrowingDuplicateEventException extends ModelStub {
        @Override
        public void addEvent(EpicEvent person) throws DuplicateEventException {
            throw new DuplicateEventException();
        }

        @Override
        public ReadOnlyEventPlanner getEventPlanner() {
            return new EventPlanner();
        }
    }

    /**
     * A Model stub that always accept the event being added.
     */
    private class ModelStubAcceptingEventAdded extends ModelStub {
        final ArrayList<EpicEvent> eventsAdded = new ArrayList<>();

        @Override
        public void addEvent(EpicEvent event) throws DuplicateEventException {
            requireNonNull(event);
            eventsAdded.add(event);
        }

        @Override
        public ReadOnlyEventPlanner getEventPlanner() {
            return new EventPlanner();
        }
    }
}
```
###### /java/seedu/address/logic/commands/ToggleAttendanceCommandTest.java
``` java

public class ToggleAttendanceCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
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
        boolean initialHasAttended = toggleAttendanceCommand.getAttendanceToToggle().hasAttended();
        try {
            CommandResult commandResult = toggleAttendanceCommand.execute();
            assertEquals(String.format(ToggleAttendanceCommand.MESSAGE_SUCCESS,
                    toggleAttendanceCommand.getAttendanceToToggle().getPerson().getName(),
                    toggleAttendanceCommand.getAttendanceToToggle().getEvent().getName()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        assertTrue(initialHasAttended != toggleAttendanceCommand.getAttendanceToToggle().hasAttended());
    }

    /**
     * Executes a {@code ToggleAttendanceCommand} with the given {@code index},
     * and checks that a {@code CommandException} is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ToggleAttendanceCommand toggleAttendanceCommand = prepareCommand(index);

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
###### /java/seedu/address/testutil/EpicEventUtil.java
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
###### /java/seedu/address/testutil/EpicEventBuilder.java
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
