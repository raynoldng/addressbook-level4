package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.person.Person;

//@@author bayweiheng

/**
 * Registers a person to an event.
 */
public class RegisterPersonCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "register";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Registers the person identified by the index number used in the last person listing"
            + " to a particular event.\n"
            + "Parameters: INDEX (must be a positive integer), EVENT_NAME (must match an event's name"
            + " in EPIC exactly)\n"
            + "Example: " + COMMAND_WORD + " 1" + " AY201718 Graduation";

    public static final String MESSAGE_SUCCESS = "Registered person %1$s for event %2$s";
    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person is already registered for the event";

    private Index targetIndex;
    private String eventName;

    private Person personToRegister;
    private EpicEvent eventToRegisterFor;

    /**
     * Creates an RegisterPersonCommand to register the Person at targetIndex in the last person
     * listing for the EpicEvent with name eventName
     */
    public RegisterPersonCommand(Index targetIndex, String eventName) {
        requireAllNonNull(targetIndex, eventName);
        this.targetIndex = targetIndex;
        this.eventName = eventName;
    }

    public RegisterPersonCommand(Person personToRegister, EpicEvent eventToRegisterFor) {
        this.personToRegister = personToRegister;
        this.eventToRegisterFor = eventToRegisterFor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(personToRegister, eventToRegisterFor);
        try {
            model.registerPersonForEvent(personToRegister, eventToRegisterFor);
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        } catch (DuplicateAttendanceException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.visuallySelectEpicEvent(eventToRegisterFor);

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToRegister, eventName));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToRegister = lastShownPersonList.get(targetIndex.getZeroBased());

        List<EpicEvent> events = model.getEventList();

        List<EpicEvent> matchedEvents = events.stream()
                .filter(e -> e.getName().toString().equals(eventName))
                .collect(Collectors.toList());

        if (matchedEvents.isEmpty()) {
            throw new CommandException(MESSAGE_EVENT_NOT_FOUND);
        }

        eventToRegisterFor = matchedEvents.get(0);
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new DeregisterPersonCommand(personToRegister, eventToRegisterFor);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RegisterPersonCommand // instanceof handles nulls
                && this.targetIndex.equals(((RegisterPersonCommand) other).targetIndex) // state check
                && Objects.equals(this.personToRegister, ((RegisterPersonCommand) other).personToRegister)
                && this.eventName.equals(((RegisterPersonCommand) other).eventName)
                && Objects.equals(this.eventToRegisterFor, ((RegisterPersonCommand) other).eventToRegisterFor));
    }
}
