package seedu.address.logic.commands;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;

/** lists all attendees in a given event */
public class ListAttendeesCommand extends Command {

    public static final String COMMAND_WORD = "list-attendees";

    public static final String MESSAGE_SUCCESS = "Listed all persons in %1$s";
    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";

    private final String eventName;
    private EpicEvent eventToListAttendeesFor;

    public ListAttendeesCommand(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<EpicEvent> events = model.getEventList();

        List<EpicEvent> matchedEvents = events.stream()
                .filter(e -> e.getName().toString().equals(eventName))
                .collect(Collectors.toList());

        if (matchedEvents.isEmpty()) {
            throw new CommandException(MESSAGE_EVENT_NOT_FOUND);
        }

        eventToListAttendeesFor = matchedEvents.get(0);

        Predicate<Person> isInEvent = person ->
            eventToListAttendeesFor.hasPerson(person);

        model.updateFilteredPersonList(isInEvent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, eventName));
    }
}
