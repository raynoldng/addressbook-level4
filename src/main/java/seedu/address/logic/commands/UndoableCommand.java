package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.commands.exceptions.CommandException;


/**
 * Represents a command which can be undone and redone.
 */
public abstract class UndoableCommand extends Command {

    //@@author bayweiheng
    /**
     * Represents the Command to be executed to revert the changes made by the UndoableCommand.
     * We mandate that the oppositeCommand of an UndoableCommand be undoable as well.
     */
    protected UndoableCommand oppositeCommand;
    //@@author

    protected abstract CommandResult executeUndoableCommand() throws CommandException;

    /**
     * This method is called before the execution of {@code UndoableCommand}.
     * {@code UndoableCommand}s that require this preprocessing step should override this method.
     */
    protected void preprocessUndoableCommand() throws CommandException {}

    //@@author bayweiheng
    /**
     * Undoes the intention of the previous command, and updates the person panel
     * to show all persons. The relative ordering of Persons/EpicEvents may be altered after undoing of
     * a delete command.
     */
    protected final void undo() {
        requireAllNonNull(model);
        try {
            oppositeCommand.executeUndoableCommand();
        } catch (CommandException ce) {
            throw new AssertionError("This command should not fail.");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    protected abstract void generateOppositeCommand();

    /**
     * Executes the command and updates person panel to show all persons.
     */
    protected final void redo() {
        requireNonNull(model);
        try {
            executeUndoableCommand();
        } catch (CommandException ce) {
            throw new AssertionError("The command has been successfully executed previously; "
                    + "it should not fail now.");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public final CommandResult execute() throws CommandException {
        preprocessUndoableCommand();
        generateOppositeCommand();
        oppositeCommand.setData(model, history, undoRedoStack);
        return executeUndoableCommand();
    }
    //@@author
}
