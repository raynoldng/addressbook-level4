package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindRegistrantCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;

//@@author raynoldng
/**
 * Parses input arguments and creates a new FindRegistrantCommandParser object
 */
public class FindRegistrantCommandParser implements Parser<FindRegistrantCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindRegistrantCommand
     * and returns an FindRegistrantCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindRegistrantCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindRegistrantCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindRegistrantCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
