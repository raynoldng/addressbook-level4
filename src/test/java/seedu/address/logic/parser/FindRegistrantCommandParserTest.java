package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.commands.FindRegistrantCommand;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;

//@@author raynoldng
public class FindRegistrantCommandParserTest {

    private FindRegistrantCommandParser parser = new FindRegistrantCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindRegistrantCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindRegistrantCommand expectedFindRegistrantCommand =
                new FindRegistrantCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList("John", "Cena")));
        assertParseSuccess(parser, "John Cena", expectedFindRegistrantCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n John \n \t Cena \t", expectedFindRegistrantCommand);
    }

}
