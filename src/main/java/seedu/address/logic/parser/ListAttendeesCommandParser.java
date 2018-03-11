package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ListAttendeesCommand;
import seedu.address.logic.commands.RegisterPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListAttendeesCommand object
 */
public class ListAttendeesCommandParser implements Parser<ListAttendeesCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListAttendeesCommand
     * and returns an ListAttendeesCommand object for execution.
     */
    public ListAttendeesCommand parse(String args) {
        String trimmedArgs = args.trim();
        return new ListAttendeesCommand(trimmedArgs);
    }

}
