package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using it's last displayed index from the event planner.
 */
public class DeletePersonCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    //@@author bayweiheng
    public static final String MESSAGE_STILL_REGISTERED = "This person is still registered for an event!"
            + " Please deregister the person from all events first";
    //@@author
    private final Index targetIndex;

    private Person personToDelete;

    public DeletePersonCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    //@@author bayweiheng
    /**
     * Used for generating the oppositeCommand of an AddCommand
     */
    public DeletePersonCommand(Person personToDelete) {
        this.personToDelete = personToDelete;
        this.targetIndex = null;
    }
    //@@author

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(personToDelete);
        try {
            model.deletePerson(personToDelete);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    //@@author bayweiheng
    /**
     * Finds the person to delete from the supplied index.
     * If the person is still registered for an event, he/she is not allowed to be deleted,
     * and an exception will be thrown.
     */
    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToDelete = lastShownList.get(targetIndex.getZeroBased());
        if (personToDelete.getNumberOfEventsRegisteredFor() > 0) {
            throw new CommandException(MESSAGE_STILL_REGISTERED);
        }
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new AddPersonCommand(personToDelete);
    }
    //@@author

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeletePersonCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeletePersonCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDelete, ((DeletePersonCommand) other).personToDelete));
    }
}
