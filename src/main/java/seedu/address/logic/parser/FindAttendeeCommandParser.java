package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;



/**
 * Parses input arguments and creates a new FindPersonCommand object
 */
public class FindAttendeeCommandParser implements Parser<FindAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindAttendanceCommand
     * and returns an FindAttendanceCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindAttendanceCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAttendanceCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindAttendanceCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
