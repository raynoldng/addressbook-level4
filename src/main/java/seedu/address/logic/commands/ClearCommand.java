package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.EventPlanner;

/**
 * Clears the event planner.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Event planner has been cleared!";

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.resetData(new EventPlanner());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    //@@author bayweiheng

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new RestoreCommand(new EventPlanner(model.getEventPlanner()));
    }
    //@@author
}
