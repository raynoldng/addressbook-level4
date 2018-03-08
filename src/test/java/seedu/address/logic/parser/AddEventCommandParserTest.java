package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SEMINAR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.event.EpicEvent;
import seedu.address.testutil.EpicEventBuilder;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        EpicEvent expectedEvent = new EpicEventBuilder().withName(VALID_NAME_GRADUATION).withTags(VALID_TAG_GRADUATION).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEvent));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_SEMINAR + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEvent));

        // multiple tags - all accepted
        EpicEvent expectedEventMultipleTags = new EpicEventBuilder().withName(VALID_NAME_GRADUATION)
                .withTags(VALID_TAG_SEMINAR, VALID_TAG_GRADUATION).build();
        assertParseSuccess(parser, NAME_DESC_GRADUATION + TAG_DESC_SEMINAR + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEventMultipleTags));
    }

}
