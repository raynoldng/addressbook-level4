// @@author raynoldng
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;

import org.junit.Test;

import seedu.address.logic.commands.SelectEventCommand;

/**
 * Test scope: similar to {@code DeletePersonCommandParserTest}.
 * @see DeletePersonCommandParserTest
 */
public class SelectEventCommandParserTest {

    private SelectEventCommandParser parser = new SelectEventCommandParser();

    @Test
    public void parse_validArgs_returnsSelectEventCommand() {
        assertParseSuccess(parser, "1", new SelectEventCommand(INDEX_FIRST_EVENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectEventCommand.MESSAGE_USAGE));
    }
}
