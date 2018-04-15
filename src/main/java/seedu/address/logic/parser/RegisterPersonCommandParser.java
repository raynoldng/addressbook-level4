package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RegisterPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author bayweiheng

/**
 * Parses input arguments and creates a new RegisterPersonCommand object
 */
public class RegisterPersonCommandParser implements Parser<RegisterPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RegisterPersonCommand
     * and returns an RegisterPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RegisterPersonCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] indexAndEventName = trimmedArgs.split("\\s+", 2);

        if (indexAndEventName.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RegisterPersonCommand.MESSAGE_USAGE));
        }
        String eventName = indexAndEventName[1];

        try {
            Index index = ParserUtil.parseIndex(indexAndEventName[0]);
            return new RegisterPersonCommand(index, eventName);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RegisterPersonCommand.MESSAGE_USAGE));
        }
    }

}
