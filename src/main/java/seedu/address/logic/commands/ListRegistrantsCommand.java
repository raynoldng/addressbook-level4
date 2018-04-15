package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ATTENDEES;

//@@author raynoldng
/**
 * Lists all attendees of selected event in the event planner to the user.
 */
public class ListRegistrantsCommand extends Command {

    public static final String COMMAND_WORD = "list-registrants";

    public static final String MESSAGE_SUCCESS = "Listed all registrants";


    @Override
    public CommandResult execute() {
        model.updateFilteredAttendanceList(PREDICATE_SHOW_ALL_ATTENDEES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
