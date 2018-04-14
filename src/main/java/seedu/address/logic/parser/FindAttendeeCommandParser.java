package seedu.address.logic.parser;

import seedu.address.logic.commands.FindAttendaneeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;

import java.util.Arrays;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new FindPersonCommand object
 */
public class FindAttendeeCommandParser implements Parser<FindAttendaneeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindAttendaneeCommand
     * and returns an FindAttendaneeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindAttendaneeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAttendaneeCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindAttendaneeCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
