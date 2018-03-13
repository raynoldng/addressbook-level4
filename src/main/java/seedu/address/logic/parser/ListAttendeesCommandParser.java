package seedu.address.logic.parser;

import seedu.address.logic.commands.ListAttendeesCommand;

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
