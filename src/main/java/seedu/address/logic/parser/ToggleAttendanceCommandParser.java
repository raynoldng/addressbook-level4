package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ToggleAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author william6364
/**
 * Parses input arguments and creates a new ToggleAttendanceCommand object
 */
public class ToggleAttendanceCommandParser implements Parser<ToggleAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ToggleAttendanceCommand
     * and returns an ToggleAttendanceCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ToggleAttendanceCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ToggleAttendanceCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ToggleAttendanceCommand.MESSAGE_USAGE));
        }
    }
}
