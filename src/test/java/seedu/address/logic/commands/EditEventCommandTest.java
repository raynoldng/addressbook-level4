package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
//import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
//import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEventPlanner;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.model.EventPlanner;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.EpicEvent;
import seedu.address.testutil.EditEventDescriptorBuilder;
import seedu.address.testutil.EpicEventBuilder;

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
