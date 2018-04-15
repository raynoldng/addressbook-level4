# bayweiheng
###### \java\seedu\address\logic\commands\CommandTestUtil.java
``` java
    /**
     * Returns true if the {@code Person} is in the {@code model} and false otherwise
     */
    public static void assertPersonInModel(Person person, Model model) {
        int index = model.getFilteredPersonList().indexOf(person);
        assertTrue(index != -1);
    }

    /**
     * Returns true if the {@code Person} is not in the {@code model} and false otherwise
     */
    public static void assertPersonNotInModel(Person person, Model model) {
        int index = model.getFilteredPersonList().indexOf(person);
        assertTrue(index == -1);
    }

    /**
     * Returns true if the {@code EpicEvent} is in the {@code model} and false otherwise
     */
    public static void assertEventInModel(EpicEvent event, Model model) {
        int index = model.getFilteredEventList().indexOf(event);
        assertTrue(index != -1);
    }

    /**
     * Returns true if the {@code EpicEvent} is not in the {@code model} and false otherwise
     */
    public static void assertEventNotInModel(EpicEvent event, Model model) {
        int index = model.getFilteredEventList().indexOf(event);
        assertTrue(index == -1);
    }

    /**
     * Attempts to execute the {@code Command}. Prints the error message and throws an AssertionError
     * upon failure.
     */
    public static void tryToExecute(Command command) {
        try {
            command.execute();
        } catch (CommandException e) {
            System.out.println(e.getMessage());
            throw new AssertionError("Failure when trying to execute command");
        }
    }

```
###### \java\seedu\address\logic\commands\DeleteEventCommandTest.java
``` java
    /**
     * Modified UndoRedo tests for DeleteEventCommand due to changed implementation.
     */
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        EpicEvent eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        DeleteEventCommand deleteEventCommand = prepareCommand(INDEX_FIRST_EVENT);

        // delete -> deletes first event in unfiltered event list
        deleteEventCommand.execute();
        undoRedoStack.push(deleteEventCommand);

        // undo -> adds deleted event back to model
        tryToExecute(undoCommand);
        assertEventInModel(eventToDelete, model);

        // redo -> deletes re-added event from model
        tryToExecute(redoCommand);
        assertEventNotInModel(eventToDelete, model);
    }
```
###### \java\seedu\address\logic\commands\DeleteEventCommandTest.java
``` java
    /**
     * Modified UndoRedo tests for DeleteEventCommand due to changed implementation.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameEventDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteEventCommand deleteEventCommand = prepareCommand(INDEX_FIRST_EVENT);

        showEventAtIndex(model, INDEX_SECOND_EVENT);
        EpicEvent eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        // delete -> deletes second event in unfiltered event list / first event in filtered event list
        deleteEventCommand.execute();
        undoRedoStack.push(deleteEventCommand);

        // undo -> adds deleted event back to model
        tryToExecute(undoCommand);
        assertEventInModel(eventToDelete, model);

        // redo -> deletes re-added event from model
        tryToExecute(redoCommand);
        assertEventNotInModel(eventToDelete, model);
    }
```
###### \java\seedu\address\logic\commands\DeletePersonCommandTest.java
``` java
    /**
     * Modified UndoRedo tests for DeletePersonCommand due to changed implementation.
     */
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeletePersonCommand deletePersonCommand = prepareCommand(INDEX_FIRST_PERSON);

        // delete -> deletes first person in unfiltered event list
        deletePersonCommand.execute();
        undoRedoStack.push(deletePersonCommand);

        // undo -> adds deleted person back to model
        tryToExecute(undoCommand);
        assertPersonInModel(personToDelete, model);

        // redo -> deletes re-added event from model
        tryToExecute(redoCommand);
        assertPersonNotInModel(personToDelete, model);
    }
```
###### \java\seedu\address\logic\commands\DeletePersonCommandTest.java
``` java
    /**
     * Modified UndoRedo tests for DeletePersonCommand due to changed implementation.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeletePersonCommand deletePersonCommand = prepareCommand(INDEX_FIRST_PERSON);

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // delete -> deletes second person in unfiltered person list / first person in filtered person list
        deletePersonCommand.execute();
        undoRedoStack.push(deletePersonCommand);

        // undo -> adds deleted person back to model
        tryToExecute(undoCommand);
        assertPersonInModel(personToDelete, model);

        // redo -> deletes re-added event from model
        tryToExecute(redoCommand);
        assertPersonNotInModel(personToDelete, model);
    }
```
###### \java\seedu\address\logic\commands\DeregisterPersonCommandTest.java
``` java

/** Contains unit tests for DeregisterPersonCommand */
public class DeregisterPersonCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(),
            new UserPrefs());

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        DeregisterPersonCommand deregisterPersonCommand = prepareCommand(outOfBoundIndex,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        assertCommandFailure(deregisterPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEventPlanner().getPersonList().size());

        DeregisterPersonCommand deregisterPersonCommand = prepareCommand(outOfBoundIndex,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        assertCommandFailure(deregisterPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        DeregisterPersonCommand deregisterFirstCommand = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        DeregisterPersonCommand deregisterSecondCommand = prepareCommand(INDEX_SECOND_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        DeregisterPersonCommand deregisterThirdCommand = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_SECOND_EVENT.getZeroBased()).getName().toString());

        // same object -> returns true
        assertTrue(deregisterFirstCommand.equals(deregisterFirstCommand));

        // same values -> returns true
        DeregisterPersonCommand registerFirstCommandCopy = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());
        assertTrue(deregisterFirstCommand.equals(registerFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        registerFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deregisterFirstCommand.equals(registerFirstCommandCopy));

        // different types -> returns false
        assertFalse(deregisterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deregisterFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deregisterFirstCommand.equals(deregisterSecondCommand));

        // different event -> returns false
        assertFalse(deregisterFirstCommand.equals(deregisterThirdCommand));
    }

    /**
     * Returns a {@code RegisterEventCommand} with parameters {@code index}, {@code eventName}.
     */
    private DeregisterPersonCommand prepareCommand(Index index, String eventName) {
        DeregisterPersonCommand deregisterPersonCommand = new DeregisterPersonCommand(index, eventName);
        deregisterPersonCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deregisterPersonCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

}
```
###### \java\seedu\address\logic\commands\EditEventCommandTest.java
``` java
    /**
    * Modified UndoRedo tests for EditEventCommand due to changed implementation.
    */
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        EpicEvent editedEvent = new EpicEventBuilder().build();
        EpicEvent eventToEdit = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());

        // this is needed since edit Event is now mutable
        EpicEvent eventToEditCopy = new EpicEvent(eventToEdit);
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(editedEvent).build();
        EditEventCommand editEventCommand = prepareCommand(INDEX_FIRST_EVENT, descriptor);

        // edit -> first Event edited
        editEventCommand.execute();
        undoRedoStack.push(editEventCommand);

        // undo -> edits first Event back
        tryToExecute(undoCommand);
        assertEventNotInModel(editedEvent, model);
        assertEventInModel(eventToEditCopy, model);

        // redo -> same first Event edited again
        tryToExecute(redoCommand);
        assertEventNotInModel(eventToEditCopy, model);
        assertEventInModel(editedEvent, model);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_EVENT_NAME_SEMINAR).build();
        EditEventCommand editEventCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editEventCommand not pushed into undoRedoStack
        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
    * 1. Edits an {@code EpicEvent} from a filtered list.
    * 2. Undo the edit.
    * 3. The unfiltered list should be shown now. Verify that the index of the previously edited EpicEvent in the
    * unfiltered list is different from the index at the filtered list.
    * 4. Redo the edit. This ensures {@code RedoCommand} edits the EpicEvent object regardless of indexing.
    */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameEventEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        EpicEvent editedEvent = new EpicEventBuilder().build();
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(editedEvent).build();
        EditEventCommand editEventCommand = prepareCommand(INDEX_FIRST_EVENT, descriptor);

        showEventAtIndex(model, INDEX_SECOND_EVENT);
        EpicEvent eventToEdit = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());

        // this is needed since edit event is now mutable
        EpicEvent eventToEditCopy = new EpicEvent(eventToEdit);

        // edit -> edits second event in unfiltered event list / first Event in filtered event list
        editEventCommand.execute();
        undoRedoStack.push(editEventCommand);

        // undo -> edits first event back
        tryToExecute(undoCommand);
        assertEventNotInModel(editedEvent, model);
        assertEventInModel(eventToEditCopy, model);

        // redo -> same first event edited again
        tryToExecute(redoCommand);
        assertEventNotInModel(eventToEditCopy, model);
        assertEventInModel(editedEvent, model);
    }
```
###### \java\seedu\address\logic\commands\EditPersonCommandTest.java
``` java
    /**
     * Modified UndoRedo tests for EditPersonCommand due to changed implementation.
     */
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person editedPerson = new PersonBuilder().build();
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // this is needed since edit person is now mutable
        Person personToEditCopy = new Person(personToEdit);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditPersonCommand editPersonCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        // edit -> first person edited
        editPersonCommand.execute();
        undoRedoStack.push(editPersonCommand);

        // undo -> edits first person back
        tryToExecute(undoCommand);
        assertPersonNotInModel(editedPerson, model);
        assertPersonInModel(personToEditCopy, model);

        // redo -> same first person edited again
        tryToExecute(redoCommand);
        assertPersonNotInModel(personToEditCopy, model);
        assertPersonInModel(editedPerson, model);
    }
```
###### \java\seedu\address\logic\commands\EditPersonCommandTest.java
``` java
    /**
     * Modified UndoRedo tests for EditPersonCommand due to changed implementation.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditPersonCommand editPersonCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // this is needed since edit person is now mutable
        Person personToEditCopy = new Person(personToEdit);

        // edit -> edits second person in unfiltered person list / first person in filtered person list
        editPersonCommand.execute();
        undoRedoStack.push(editPersonCommand);

        // undo -> edits first person back
        tryToExecute(undoCommand);
        assertPersonNotInModel(editedPerson, model);
        assertPersonInModel(personToEditCopy, model);

        // redo -> same first person edited again
        tryToExecute(redoCommand);
        assertPersonNotInModel(personToEditCopy, model);
        assertPersonInModel(editedPerson, model);
    }
```
###### \java\seedu\address\logic\commands\ListRegisteredPersonsCommandTest.java
``` java
/**
 * Contains unit tests for ListRegisteredPersonsCommand.
 */
public class ListRegisteredPersonsCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void execute() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        EpicEvent mainEvent = model.getEventList().get(1);
        for (Person person: model.getFilteredPersonList()) {
            try {
                model.registerPersonForEvent(person, mainEvent);
            } catch (Exception e) {
                throw new AssertionError("not possible");
            }
        }
        ListRegisteredPersonsCommand command = prepareCommand(mainEvent.getName().toString());
        assertCommandSuccess(command, expectedMessage, model.getFilteredPersonList());
    }

    /**
     * Parses {@code userInput} into a {@code ListRegisteredPersonsCommand}.
     */
    private ListRegisteredPersonsCommand prepareCommand(String eventName) {
        ListRegisteredPersonsCommand command =
                new ListRegisteredPersonsCommand(eventName);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code EventPlanner} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(
            ListRegisteredPersonsCommand command, String expectedMessage, List<Person> expectedList) {
        EventPlanner expectedEventPlanner = new EventPlanner(model.getEventPlanner());
        CommandResult commandResult = null;
        try {
            commandResult = command.execute();
        } catch (CommandException e) {
            throw new AssertionError("not possible");
        }

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedEventPlanner, model.getEventPlanner());
    }

}
```
###### \java\seedu\address\logic\commands\RegisterPersonCommandTest.java
``` java

/** Contains unit tests for RegisterPersonCommand */
public class RegisterPersonCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        RegisterPersonCommand registerPersonCommand = prepareCommand(outOfBoundIndex,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        assertCommandFailure(registerPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEventPlanner().getPersonList().size());

        RegisterPersonCommand registerPersonCommand = prepareCommand(outOfBoundIndex,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        assertCommandFailure(registerPersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        RegisterPersonCommand registerFirstCommand = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        RegisterPersonCommand registerSecondCommand = prepareCommand(INDEX_SECOND_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());

        RegisterPersonCommand registerThirdCommand = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_SECOND_EVENT.getZeroBased()).getName().toString());

        // same object -> returns true
        assertTrue(registerFirstCommand.equals(registerFirstCommand));

        // same values -> returns true
        RegisterPersonCommand registerFirstCommandCopy = prepareCommand(INDEX_FIRST_PERSON,
                model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased()).getName().toString());
        assertTrue(registerFirstCommand.equals(registerFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        registerFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(registerFirstCommand.equals(registerFirstCommandCopy));

        // different types -> returns false
        assertFalse(registerFirstCommand.equals(1));

        // null -> returns false
        assertFalse(registerFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(registerFirstCommand.equals(registerSecondCommand));

        // different event -> returns false
        assertFalse(registerFirstCommand.equals(registerThirdCommand));
    }

    /**
     * Returns a {@code RegisterEventCommand} with parameters {@code index}, {@code eventName}.
     */
    private RegisterPersonCommand prepareCommand(Index index, String eventName) {
        RegisterPersonCommand registerPersonCommand = new RegisterPersonCommand(index, eventName);
        registerPersonCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return registerPersonCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

}
```
###### \java\systemtests\ClearCommandSystemTest.java
``` java

        // Case: undo clearing event planner -> original event planner restored
        // This serves as a system test for RestoreCommand as well
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command,  expectedResultMessage, defaultModel);
        assertSelectedCardUnchanged();

        // Case: redo clearing event planner -> cleared
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedResultMessage, new ModelManager());
        assertSelectedCardUnchanged();

        // Case: selects first card in person list and clears event planner -> cleared and no card selected
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original event planner
        selectPerson(Index.fromOneBased(1));
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardDeselected();

        // Case: filters the person list before clearing -> entire event planner cleared
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original event planner
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

```
