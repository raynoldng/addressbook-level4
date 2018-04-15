package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;

//@@author bayweiheng

/**
 * Registers a person to an event.
 */
public class DeregisterPersonCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deregister";

    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";
    public static final String MESSAGE_PERSON_NOT_IN_EVENT = "This person was not registered for the event";
    public static final String MESSAGE_SUCCESS = "Deregistered person %1$s from event %2$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deregisters the person identified by the index number used in the last person listing"
            + " from a particular event.\n"
            + "Parameters: INDEX (must be a positive integer), EVENT_NAME (must match an event's name"
            + " in EPIC exactly)\n"
            + "Example: " + COMMAND_WORD + " 1" + " AY201718 Graduation";

    private Index targetIndex;
    private String eventName;

    private Person personToDeregister;
    private EpicEvent eventToDeregisterFor;

    /**
     * Creates an RegisterPersonCommand to register the Person at targetIndex in the last person
     * listing for the EpicEvent with name eventName
     */
    public DeregisterPersonCommand(Index targetIndex, String eventName) {
        requireAllNonNull(targetIndex, eventName);
        this.targetIndex = targetIndex;
        this.eventName = eventName;
    }

    public DeregisterPersonCommand(Person personToDeregister, EpicEvent eventToDeregisterFor) {
        this.personToDeregister = personToDeregister;
        this.eventToDeregisterFor = eventToDeregisterFor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(personToDeregister, eventToDeregisterFor);
        try {
            model.deregisterPersonFromEvent(personToDeregister, eventToDeregisterFor);
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        } catch (PersonNotFoundInEventException dpe) {
            throw new CommandException(MESSAGE_PERSON_NOT_IN_EVENT);
        }

        model.visuallySelectEpicEvent(eventToDeregisterFor);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToDeregister, eventName));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToDeregister = lastShownPersonList.get(targetIndex.getZeroBased());

        List<EpicEvent> events = model.getEventList();

        List<EpicEvent> matchedEvents = events.stream()
                .filter(e -> e.getName().toString().equals(eventName))
                .collect(Collectors.toList());

        if (matchedEvents.isEmpty()) {
            throw new CommandException(MESSAGE_EVENT_NOT_FOUND);
        }

        eventToDeregisterFor = matchedEvents.get(0);
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new RegisterPersonCommand(personToDeregister, eventToDeregisterFor);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeregisterPersonCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeregisterPersonCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDeregister, ((DeregisterPersonCommand) other).personToDeregister)
                && this.eventName.equals(((DeregisterPersonCommand) other).eventName)
                && Objects.equals(this.eventToDeregisterFor, ((DeregisterPersonCommand) other).eventToDeregisterFor));
    }
}
