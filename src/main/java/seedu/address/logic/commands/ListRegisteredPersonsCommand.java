package seedu.address.logic.commands;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;

//@@author bayweiheng

/** Lists all registered persons in a given event, regardless of whether their attendance is marked */
public class ListRegisteredPersonsCommand extends Command {

    public static final String COMMAND_WORD = "list-registered";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the persons registered for the specified event on the Persons Pane.\n"
            + "Parameters: EVENT_NAME (must match an event's name in EPIC exactly)\n"
            + "Example: " + COMMAND_WORD + " IoT Seminar";

    public static final String MESSAGE_SUCCESS = "Listed all persons in %1$s";
    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";

    private final String eventName;
    private EpicEvent eventToListRegisteredPersonsFor;

    public ListRegisteredPersonsCommand(String eventName) {
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

        eventToListRegisteredPersonsFor = matchedEvents.get(0);

        Predicate<Person> isInEvent = person ->
            eventToListRegisteredPersonsFor.hasPerson(person);

        model.updateFilteredPersonList(isInEvent);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }
}
