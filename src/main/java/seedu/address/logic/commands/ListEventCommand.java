package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EVENTS;

import seedu.address.model.event.EpicEvent;

/**
 * Lists all events in the event planner to the user.
 */
public class ListEventCommand extends Command implements FocusOnEpicEventsList {

    public static final String COMMAND_WORD = "list-events";

    public static final String MESSAGE_SUCCESS = "Listed all events";

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        model.visuallySelectEpicEvent(EpicEvent.getDummyEpicEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }


}
