package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Adds an event to the event planner.
 */
public class AddEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the event planner. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "AY2017/18 Graduation Ceremony "
            + PREFIX_TAG + "graduation ";

    public static final String MESSAGE_SUCCESS = "New event added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the event planner";

    private final EpicEvent toAdd;

    /**
     * Creates an AddEventCommand to add the specified {@code EpicEvent}
     */
    public AddEventCommand(EpicEvent event) {
        requireNonNull(event);
        toAdd = event;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addEvent(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateEventException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEventCommand // instanceof handles nulls
                && toAdd.equals(((AddEventCommand) other).toAdd));
    }
}
