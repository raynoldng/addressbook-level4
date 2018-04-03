package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.ReadOnlyEventPlanner;

//@@author bayweiheng

/** Restores the event planner to its previous state.
 * Used only to undo a ClearCommand.
 */
public class RestoreCommand extends UndoableCommand {

    public static final String MESSAGE_SUCCESS = "Event planner has been restored!";

    private ReadOnlyEventPlanner previousEventPlanner;

    public RestoreCommand(ReadOnlyEventPlanner previousEventPlanner) {
        this.previousEventPlanner = previousEventPlanner;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.resetData(previousEventPlanner);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new ClearCommand();
    }

}
