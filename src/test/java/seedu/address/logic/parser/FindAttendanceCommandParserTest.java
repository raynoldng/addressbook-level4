package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.logic.commands.FindAttendanceCommand;
import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;
import seedu.address.model.event.EventNameContainsKeywordsPredicate;

import java.util.Arrays;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

//@@author raynoldng
public class FindAttendanceCommandParserTest {

    private FindAttendanceCommandParser parser = new FindAttendanceCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindAttendanceCommand expectedFindAttendanceCommand =
                new FindAttendanceCommand(new AttendanceNameContainsKeywordsPredicate(Arrays.asList("John", "Cena")));
        assertParseSuccess(parser, "John Cena", expectedFindAttendanceCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n John \n \t Cena \t", expectedFindAttendanceCommand);
    }

}
