# jiangyue12392
###### \java\seedu\address\logic\commands\DeleteEventCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteEventCommand}.
 */
public class DeleteEventCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
        System.out.println(getTypicalEventPlanner().getEventList().size());
        System.out.println(model.getEventPlanner().getEventList().size());
        EpicEvent eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        DeleteEventCommand deleteEventCommand = prepareCommand(INDEX_FIRST_EVENT);

        String expectedMessage = String.format(DeleteEventCommand.MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete);

        ModelManager expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());
        expectedModel.deleteEvent(eventToDelete);

        assertCommandSuccess(deleteEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        DeleteEventCommand deleteEventCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        EpicEvent eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        DeleteEventCommand deleteEventCommand = prepareCommand(INDEX_FIRST_EVENT);

        String expectedMessage = String.format(DeleteEventCommand.MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete);

        Model expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());
        expectedModel.deleteEvent(eventToDelete);
        showNoEvent(expectedModel);

        assertCommandSuccess(deleteEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        Index outOfBoundIndex = INDEX_SECOND_EVENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEventPlanner().getEventList().size());

        DeleteEventCommand deleteEventCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

```
###### \java\seedu\address\logic\commands\DeleteEventCommandTest.java
``` java

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        DeleteEventCommand deleteEventCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteEventCommand not pushed into undoRedoStack
        assertCommandFailure(deleteEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

```
###### \java\seedu\address\logic\commands\DeleteEventCommandTest.java
``` java

    @Test
    public void equals() throws Exception {
        DeleteEventCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_EVENT);
        DeleteEventCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_EVENT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteEventCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_EVENT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different event -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteEventCommand} with the parameter {@code index}.
     */
    private DeleteEventCommand prepareCommand(Index index) {
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(index);
        deleteEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteEventCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEvent(Model model) {
        model.updateFilteredEventList(p -> false);

        assertTrue(model.getFilteredEventList().isEmpty());
    }
}
```
###### \java\seedu\address\logic\commands\EditEventCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests
 * for EditEventCommand.
 */
public class EditEventCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        EpicEvent editedEvent = new EpicEventBuilder().build();
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(editedEvent).build();
        EditEventCommand editEventCommand = prepareCommand(INDEX_FIRST_EVENT, descriptor);

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new EventPlanner(model.getEventPlanner()), new UserPrefs());
        expectedModel.updateEvent(model.getFilteredEventList().get(0), editedEvent);

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastEvent = Index.fromOneBased(model.getFilteredEventList().size());
        EpicEvent lastEvent = model.getFilteredEventList().get(indexLastEvent.getZeroBased());

        EpicEventBuilder eventInList = new EpicEventBuilder(lastEvent);
        EpicEvent editedEvent = eventInList.withName(VALID_EVENT_NAME_SEMINAR)
                .withTags(VALID_EVENT_TAG_SEMINAR).build();

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR)
                .withTags(VALID_EVENT_TAG_SEMINAR).build();
        EditEventCommand editEventCommand = prepareCommand(indexLastEvent, descriptor);

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new EventPlanner(model.getEventPlanner()), new UserPrefs());
        expectedModel.updateEvent(lastEvent, editedEvent);

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditEventCommand editEventCommand = prepareCommand(INDEX_FIRST_EVENT, new EditEventDescriptor());
        EpicEvent editedEvent = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new EventPlanner(model.getEventPlanner()), new UserPrefs());

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        EpicEvent eventInFilteredList = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        EpicEvent editedEvent = new EpicEventBuilder(eventInFilteredList).withName(VALID_EVENT_NAME_SEMINAR).build();
        EditEventCommand editEventCommand = prepareCommand(INDEX_FIRST_EVENT,
                new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR).build());

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new EventPlanner(model.getEventPlanner()), new UserPrefs());
        expectedModel.updateEvent(model.getFilteredEventList().get(0), editedEvent);

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateEventUnfilteredList_failure() {
        EpicEvent firstEvent = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(firstEvent).build();
        EditEventCommand editEventCommand = prepareCommand(INDEX_SECOND_EVENT, descriptor);

        assertCommandFailure(editEventCommand, model, EditEventCommand.MESSAGE_DUPLICATE_EVENT);
    }

    @Test
    public void execute_duplicateEventFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        // edit event in filtered list into a duplicate in address book
        EpicEvent eventInList = model.getEventPlanner().getEventList().get(INDEX_SECOND_EVENT.getZeroBased());
        EditEventCommand editEventCommand = prepareCommand(INDEX_FIRST_EVENT,
                new EditEventDescriptorBuilder(eventInList).build());

        assertCommandFailure(editEventCommand, model, EditEventCommand.MESSAGE_DUPLICATE_EVENT);
    }

    @Test
    public void execute_invalidEventIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR).build();
        EditEventCommand editEventCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidEventIndexFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);
        Index outOfBoundIndex = INDEX_SECOND_EVENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEventPlanner().getEventList().size());

        EditEventCommand editEventCommand = prepareCommand(outOfBoundIndex,
                new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR).build());

        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

```
###### \java\seedu\address\logic\commands\EditEventCommandTest.java
``` java

    @Test
    public void equals() throws Exception {
        final EditEventCommand standardCommand = prepareCommand(INDEX_FIRST_EVENT, DESC_GRADUATION);

        // same values -> returns true
        EditEventDescriptor copyDescriptor = new EditEventDescriptor(DESC_GRADUATION);
        EditEventCommand commandWithSameValues = prepareCommand(INDEX_FIRST_EVENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditEventCommand(INDEX_SECOND_EVENT, DESC_GRADUATION)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditEventCommand(INDEX_FIRST_EVENT, DESC_SEMINAR)));
    }

    /**
     * Returns an {@code EditEventCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditEventCommand prepareCommand(Index index, EditEventDescriptor descriptor) {
        EditEventCommand editEventCommand = new EditEventCommand(index, descriptor);
        editEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editEventCommand;
    }
}
```
###### \java\seedu\address\logic\commands\EditEventDescriptorTest.java
``` java
public class EditEventDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditEventDescriptor descriptorWithSameValues = new EditEventDescriptor(DESC_GRADUATION);
        assertTrue(DESC_GRADUATION.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_GRADUATION.equals(DESC_GRADUATION));

        // null -> returns false
        assertFalse(DESC_GRADUATION.equals(null));

        // different types -> returns false
        assertFalse(DESC_GRADUATION.equals(5));

        // different values -> returns false
        assertFalse(DESC_GRADUATION.equals(DESC_SEMINAR));

        // different name -> returns false
        EditEventDescriptor editedGraduation = new EditEventDescriptorBuilder(DESC_GRADUATION)
                .withName(VALID_EVENT_NAME_SEMINAR).build();
        assertFalse(DESC_GRADUATION.equals(editedGraduation));

        // different tags -> returns false
        editedGraduation = new EditEventDescriptorBuilder(DESC_GRADUATION).withTags(VALID_EVENT_TAG_SEMINAR).build();
        assertFalse(DESC_GRADUATION.equals(editedGraduation));
    }
}
```
###### \java\seedu\address\logic\commands\FindEventCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindEventCommand}.
 */
public class FindEventCommandTest {
    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void equals() {
        EventNameContainsKeywordsPredicate firstPredicate =
                new EventNameContainsKeywordsPredicate(Collections.singletonList("first"));
        EventNameContainsKeywordsPredicate secondPredicate =
                new EventNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindEventCommand findFirstCommand = new FindEventCommand(firstPredicate);
        FindEventCommand findSecondCommand = new FindEventCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindEventCommand findFirstCommandCopy = new FindEventCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different event -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noEventFound() {
        String expectedMessage = String.format(MESSAGE_EVENTS_LISTED_OVERVIEW, 0);
        FindEventCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multipleEventsFound() {
        String expectedMessage = String.format(MESSAGE_EVENTS_LISTED_OVERVIEW, 4);
        FindEventCommand command = prepareCommand("seminar olympiad");
        assertCommandSuccess(command, expectedMessage,
                Arrays.asList(FOODSEMINAR, IOTSEMINAR, MATHOLYMPIAD, PHYSICSOLYMPIAD));
    }

    /**
     * Parses {@code userInput} into a {@code FindEventCommand}.
     */
    private FindEventCommand prepareCommand(String userInput) {
        FindEventCommand command =
                new FindEventCommand(new EventNameContainsKeywordsPredicate(
                        Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Event>} is equal to {@code expectedList}<br>
     *     - the {@code EventPlanner} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindEventCommand command, String expectedMessage, List<EpicEvent> expectedList) {
        EventPlanner expectedEventPlanner = new EventPlanner(model.getEventPlanner());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredEventList());
        assertEquals(expectedEventPlanner, model.getEventPlanner());
    }
}
```
###### \java\seedu\address\logic\parser\DeleteEventCommandParserTest.java
``` java
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteEventCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteEventCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteEventCommandParserTest {

    private DeleteEventCommandParser parser = new DeleteEventCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteEventCommand() {
        assertParseSuccess(parser, "1", new DeleteEventCommand(INDEX_FIRST_EVENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteEventCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\logic\parser\EditEventCommandParserTest.java
``` java
public class EditEventCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE);

    private EditEventCommandParser parser = new EditEventCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_EVENT_NAME_SEMINAR, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditEventCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + VALID_EVENT_NAME_SEMINAR, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + VALID_EVENT_NAME_SEMINAR, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code EpicEvent} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_GRADUATION + TAG_DESC_SEMINAR + TAG_EMPTY,
                Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_GRADUATION + TAG_EMPTY + TAG_DESC_SEMINAR,
                Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_GRADUATION + TAG_DESC_SEMINAR,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + TAG_EMPTY + TAG_DESC_SEMINAR,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_EVENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION;

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_GRADUATION)
                .withTags(VALID_EVENT_TAG_GRADUATION).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_EVENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_GRADUATION;
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_GRADUATION).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_SEMINAR;
        descriptor = new EditEventDescriptorBuilder().withTags(VALID_EVENT_TAG_SEMINAR).build();
        expectedCommand = new EditEventCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_EVENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_SEMINAR + NAME_DESC_GRADUATION;

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_GRADUATION).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        Index targetIndex = INDEX_FIRST_EVENT;
        String userInput = targetIndex.getOneBased() + INVALID_NAME_DESC + NAME_DESC_SEMINAR;
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_EVENT;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withTags().build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
###### \java\seedu\address\logic\parser\FindEventCommandParserTest.java
``` java
public class FindEventCommandParserTest {

    private FindEventCommandParser parser = new FindEventCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindEventCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindEventCommand expectedFindEventCommand =
                new FindEventCommand(new EventNameContainsKeywordsPredicate(Arrays.asList("Seminar", "Graduation")));
        assertParseSuccess(parser, "Seminar Graduation", expectedFindEventCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Seminar \n \t Graduation  \t", expectedFindEventCommand);
    }

}
```
###### \java\seedu\address\storage\XmlAdaptedAttendanceTest.java
``` java
public class XmlAdaptedAttendanceTest {
    private static final Attendance VALID_ATTENDANCE = new Attendance(BENSON, false);

    @Test
    public void toModelType_validAttendanceDetails_returnsAttendance() throws Exception {
        XmlAdaptedAttendance attendance = new XmlAdaptedAttendance(new XmlAdaptedPerson(BENSON), false);
        assertEquals(VALID_ATTENDANCE, attendance.toModelType());
    }

    @Test
    public void toModelType_nullPerson_throwsIllegalValueException() {
        XmlAdaptedAttendance attendance = new XmlAdaptedAttendance(null, true);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, attendance::toModelType);
    }

    @Test
    public void toModelType_nullHasAttended_throwsIllegalValueException() {
        XmlAdaptedAttendance attendance = new XmlAdaptedAttendance(new XmlAdaptedPerson(BENSON), null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "Attendance Status");
        Assert.assertThrows(IllegalValueException.class, expectedMessage, attendance::toModelType);
    }

}

```
###### \java\seedu\address\storage\XmlAdaptedEpicEventTest.java
``` java
public class XmlAdaptedEpicEventTest {
    private static final String INVALID_NAME = "M@th Olympiad";
    private static final String INVALID_TAG = "#olympiad";

    private static final String VALID_NAME = MATHOLYMPIAD.getName().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = MATHOLYMPIAD.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validEpicEventDetails_returnsEpicEvent() throws Exception {
        XmlAdaptedEpicEvent event = new XmlAdaptedEpicEvent(MATHOLYMPIAD);
        assertEquals(MATHOLYMPIAD, event.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedEpicEvent event =
                new XmlAdaptedEpicEvent(INVALID_NAME, null, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedEpicEvent event = new XmlAdaptedEpicEvent(null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedEpicEvent event =
                new XmlAdaptedEpicEvent(VALID_NAME, null, invalidTags);
        Assert.assertThrows(IllegalValueException.class, event::toModelType);
    }

}
```
###### \java\seedu\address\storage\XmlAdaptedPersonTest.java
``` java
    @Test
    public void toModelType_invalidNumberOfEvents_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        INVALID_NUMBEROFEVENTS, VALID_TAGS);
        String expectedMessage = "Number of events registered for must be a positive number!";
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullNumberOfEvents_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "numberOfEventsRegisteredFor");
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }
```
###### \java\seedu\address\testutil\EditEventDescriptorBuilder.java
``` java

/**
 * A utility class to help with building EditEventDescriptor objects.
 */
public class EditEventDescriptorBuilder {

    private EditEventDescriptor descriptor;

    public EditEventDescriptorBuilder() {
        descriptor = new EditEventDescriptor();
    }

    public EditEventDescriptorBuilder(EditEventDescriptor descriptor) {
        this.descriptor = new EditEventDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditEventDescriptor} with fields containing {@code event}'s details
     */
    public EditEventDescriptorBuilder(EpicEvent event) {
        descriptor = new EditEventDescriptor();
        descriptor.setName(event.getName());
        descriptor.setTags(event.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditEventDescriptor} that we are building.
     */
    public EditEventDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditEventDescriptor}
     * that we are building.
     */
    public EditEventDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditEventDescriptor build() {
        return descriptor;
    }
}
```
###### \java\seedu\address\testutil\TypicalEpicEvents.java
``` java

/**
 * A utility class containing a list of {@code EpicEvent} objects to be used in tests.
 */
public class TypicalEpicEvents {

    public static final EpicEvent GRADUATIONAY18 = new EpicEventBuilder().withName("AY201718 Graduation Ceremony")
            .withAttendees(Arrays.asList(ALICE, CARL, DANIEL, ELLE, FIONA, GEORGE)).withTags("graduation").build();
    public static final EpicEvent FOODSEMINAR = new EpicEventBuilder().withName("Food Seminar")
            .withTags("seminar", "food").build();
    public static final EpicEvent IOTSEMINAR = new EpicEventBuilder().withName("IoT Seminar")
            .withTags("seminar", "IoT").build();
    public static final EpicEvent MATHOLYMPIAD = new EpicEventBuilder().withName("Math Olympiad")
            .withTags("competition", "math").build();
    public static final EpicEvent PHYSICSOLYMPIAD = new EpicEventBuilder().withName("Physics Olympiad")
            .withTags("competition", "physics").build();
    public static final EpicEvent CAREERTALK = new EpicEventBuilder().withName("Career Talk")
            .withTags("talk", "career").build();
    public static final EpicEvent ORIENTATION = new EpicEventBuilder().withName("Orientation")
            .withAttendees(Arrays.asList(CARL)).build();

    public static final int GRADUATIONAY18_INDEX = 0;
    public static final int ORIENTATION_INDEX = 6;

    // Manually added - Event's details found in {@code CommandTestUtil}
    public static final EpicEvent GRADUATION = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
            .withTags(VALID_EVENT_TAG_GRADUATION).build();
    public static final EpicEvent SEMINAR = new EpicEventBuilder().withName(VALID_EVENT_NAME_SEMINAR)
            .withTags(VALID_EVENT_TAG_SEMINAR).build();

    public static final String KEYWORD_MATCHING_OLYMPIAD = "Olympiad"; // A keyword that matches OLYMPIAD

    private TypicalEpicEvents() {} // prevents instantiation

    /**
     * Returns an {@code EventPlanner} with all the typical events.
     */
    public static EventPlanner getTypicalEventPlanner() {
        EventPlanner ep = new EventPlanner();
        for (Person person : getTypicalPersons()) {
            try {
                ep.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        for (EpicEvent event : getTypicalEvents()) {
            try {
                ep.addEvent(event);
            } catch (DuplicateEventException e) {
                throw new AssertionError("not possible");
            }
        }

        return ep;
    }

    public static List<EpicEvent> getTypicalEvents() {
        return new ArrayList<>(Arrays.asList(GRADUATIONAY18, FOODSEMINAR, IOTSEMINAR, MATHOLYMPIAD, PHYSICSOLYMPIAD,
                CAREERTALK, ORIENTATION));

    }
}
```
