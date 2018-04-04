# bayweiheng
###### /java/seedu/address/logic/commands/RegisterPersonCommandTest.java
``` java

/** Contains unit tests for RegisterPersonCommand */
public class RegisterPersonCommandTest {

    private Model model = new ModelManager(getTypicalEventPlanner(), new UserPrefs());
    private Model duplicatedModel = new ModelManager(getTypicalEventPlanner(), new UserPrefs());

    //TODO: Re-code tests after architecture stable
    /*@Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Person personToRegister = duplicatedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EpicEvent eventToRegisterFor = duplicatedModel.getFilteredEventList()
                .get(INDEX_SECOND_EVENT.getZeroBased());
        String eventName = model.getFilteredEventList()
                .get(INDEX_SECOND_EVENT.getZeroBased()).getName().toString();
        RegisterPersonCommand registerPersonCommand = prepareCommand(INDEX_FIRST_PERSON, eventName);

        String expectedMessage = String.format(RegisterPersonCommand.MESSAGE_SUCCESS,
                personToRegister, eventName);

        ModelManager expectedModel = new ModelManager(duplicatedModel.getEventPlanner(), new UserPrefs());
        expectedModel.registerPersonForEvent(personToRegister, eventToRegisterFor);

        //assertCommandSuccess(registerPersonCommand, model, expectedMessage, expectedModel);
    }*/

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
###### /java/seedu/address/logic/commands/DeregisterPersonCommandTest.java
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
###### /java/seedu/address/logic/commands/ListRegisteredPersonsCommandTest.java
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
