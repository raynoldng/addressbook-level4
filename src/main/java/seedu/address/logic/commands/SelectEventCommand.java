
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;

// @@author raynoldng
/**
 * Selects a event identified using it's last displayed index from the event planner.
 */
public class SelectEventCommand extends Command {

    public static final String COMMAND_WORD = "select-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the event identified by the index number used in the last event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_EVENT_SUCCESS = "Selected Event: %1$s";

    private final Index targetIndex;

    public SelectEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<EpicEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        model.setSelectedEpicEvent(targetIndex.getZeroBased());
        EventsCenter.getInstance().post(new JumpToEventListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_EVENT_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectEventCommand) other).targetIndex)); // state check
    }
}
