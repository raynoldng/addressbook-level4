package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.EventNotFoundException;

//@@author jiangyue12392
/**
 * Deletes an event identified using it's last displayed index from the event planner.
 */
public class DeleteEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the last event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";

    private final Index targetIndex;

    private EpicEvent eventToDelete;

    public DeleteEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    //@@author bayweiheng
    /**
     * Used for generating the oppositeCommand of an AddEventCommand
     */
    public DeleteEventCommand(EpicEvent eventToDelete) {
        this.eventToDelete = eventToDelete;
        this.targetIndex = null;
    }
    //@@author jiangyue12392

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(eventToDelete);
        try {
            model.deleteEvent(eventToDelete);
            model.clearSelectedEpicEvent();
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<EpicEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        eventToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    //@@author bayweiheng
    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new AddEventCommand(eventToDelete);
    }
    //@@author jiangyue12392

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEventCommand) other).targetIndex) // state check
                && Objects.equals(this.eventToDelete, ((DeleteEventCommand) other).eventToDelete));
    }
}

