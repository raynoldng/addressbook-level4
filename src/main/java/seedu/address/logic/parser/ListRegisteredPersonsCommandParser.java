package seedu.address.logic.parser;

import seedu.address.logic.commands.ListRegisteredPersonsCommand;

//@@author bayweiheng

/**
 * Parses input arguments and creates a new ListRegisteredPersonsCommand object
 */
public class ListRegisteredPersonsCommandParser implements Parser<ListRegisteredPersonsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListRegisteredPersonsCommand
     * and returns an ListRegisteredPersonsCommand object for execution.
     */
    public ListRegisteredPersonsCommand parse(String args) {
        String trimmedArgs = args.trim();
        return new ListRegisteredPersonsCommand(trimmedArgs);
    }

}
