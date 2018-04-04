# jiangyue12392
###### /java/seedu/address/logic/commands/EditEventDescriptorTest.java
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
###### /java/seedu/address/logic/commands/FindEventCommandTest.java
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
###### /java/seedu/address/logic/commands/EditEventCommandTest.java
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

    //TODO: Undo/Redo Test for editEventCommand
    /**
     @Test
     public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
     UndoRedoStack undoRedoStack = new UndoRedoStack();
     UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
     RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
     Person editedPerson = new PersonBuilder().build();
     Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
     EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
     EditPersonCommand editPersonCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);
     Model expectedModel = new ModelManager(new EventPlanner(model.getEventPlanner()), new UserPrefs());

     // edit -> first person edited
     editPersonCommand.execute();
     undoRedoStack.push(editPersonCommand);

     // undo -> reverts addressbook back to previous state and filtered person list to show all persons
     assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

     // redo -> same first person edited again
     expectedModel.updatePerson(personToEdit, editedPerson);
     assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
     }

     @Test
     public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
     UndoRedoStack undoRedoStack = new UndoRedoStack();
     UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
     RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
     Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
     EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_PERSON_NAME_BOB).build();
     EditPersonCommand editPersonCommand = prepareCommand(outOfBoundIndex, descriptor);

     // execution failed -> editPersonCommand not pushed into undoRedoStack
     assertCommandFailure(editPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

     // no commands in undoRedoStack -> undoCommand and redoCommand fail
     assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
     assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
     }*/

    /**
     * 1. Edits a {@code Person} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the person object regardless of indexing.
     */

    /**
     @Test
     public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
     UndoRedoStack undoRedoStack = new UndoRedoStack();
     UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
     RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
     Person editedPerson = new PersonBuilder().build();
     EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
     EditPersonCommand editPersonCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);
     Model expectedModel = new ModelManager(new EventPlanner(model.getEventPlanner()), new UserPrefs());

     showPersonAtIndex(model, INDEX_SECOND_PERSON);
     Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
     // edit -> edits second person in unfiltered person list / first person in filtered person list
     editPersonCommand.execute();
     undoRedoStack.push(editPersonCommand);

     // undo -> reverts addressbook back to previous state and filtered person list to show all persons
     assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

     expectedModel.updatePerson(personToEdit, editedPerson);
     assertNotEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);
     // redo -> edits same second person in unfiltered person list
     assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
     }*/

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
###### /java/seedu/address/logic/commands/DeleteEventCommandTest.java
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

    // TODO: Re-code tests after undo-redo functionality implemented
    /**
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        EpicEvent eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        DeleteEventCommand deleteEventCommand = prepareCommand(INDEX_FIRST_EVENT);
        Model expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());

        // delete -> first event deleted
        deleteEventCommand.execute();
        undoRedoStack.push(deleteEventCommand);

        // undo -> reverts event planner back to previous state and filtered event list to show all events
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first event deleted again
        expectedModel.deleteEvent(eventToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
     */

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

    /**
     * 1. Deletes a {@code Event} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted event in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the event object regardless of indexing.
     */
    // TODO: modify undo/redo function to properly undo/redo the deleteEvent command.
    /*@Test
    public void executeUndoRedo_validIndexFilteredList_sameEventDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteEventCommand deleteEventCommand = prepareCommand(INDEX_FIRST_EVENT);
        Model expectedModel = new ModelManager(model.getEventPlanner(), new UserPrefs());

        showEventAtIndex(model, INDEX_SECOND_EVENT);
        EpicEvent eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        // delete -> deletes second event in unfiltered event list / first event in filtered event list
        deleteEventCommand.execute();
        undoRedoStack.push(deleteEventCommand);

        // undo -> reverts addressbook back to previous state and filtered event list to show all events
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteEvent(eventToDelete);
        assertNotEquals(eventToDelete, model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()));
        // redo -> deletes same second event in unfiltered event list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }*/

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
###### /java/seedu/address/testutil/TypicalEpicEvents.java
``` java
/**
 * A utility class containing a list of {@code EpicEvent} objects to be used in tests.
 */
public class TypicalEpicEvents {

    public static final EpicEvent GRADUATIONAY18 = new EpicEventBuilder().withName("AY201718 Graduation Ceremony")
            .withAttendees(getTypicalPersons()).withTags("graduation").build();
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
    public static final EpicEvent ORIENTATION = new EpicEventBuilder().withName("Orientation").build();

    // Manually added
    public static final EpicEvent SOCORIENTATION = new EpicEventBuilder().withName("SOC Orientation")
            .withTags("orientation", "SOC").build();
    public static final EpicEvent IOTTALK = new EpicEventBuilder().withName("IoT Talk").build();

    // Manually added - Event's details found in {@code CommandTestUtil}
    public static final EpicEvent GRADUATION = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
            .withTags(VALID_EVENT_TAG_GRADUATION).build();
    public static final EpicEvent SEMINAR = new EpicEventBuilder().withName(VALID_EVENT_NAME_SEMINAR)
            .withTags(VALID_EVENT_TAG_SEMINAR).build();

    public static final String KEYWORD_MATCHING_OLYMPIAD = "Olympiad"; // A keyword that matches MEIER

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
