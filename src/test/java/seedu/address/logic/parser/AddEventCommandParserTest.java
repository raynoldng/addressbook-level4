package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERSON_NAME_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.Name;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EpicEventBuilder;

//@@author william6364

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        EpicEvent expectedEvent = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
                .withTags(VALID_EVENT_TAG_GRADUATION).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEvent));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_SEMINAR + NAME_DESC_GRADUATION + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEvent));

        // multiple tags - all accepted
        EpicEvent expectedEventMultipleTags = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
                .withTags(VALID_EVENT_TAG_SEMINAR, VALID_EVENT_TAG_GRADUATION).build();
        assertParseSuccess(parser, NAME_DESC_GRADUATION + TAG_DESC_SEMINAR + TAG_DESC_GRADUATION,
                new AddEventCommand(expectedEventMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        EpicEvent expectedEvent = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION).withTags().build();
        assertParseSuccess(parser, NAME_DESC_GRADUATION , new AddEventCommand(expectedEvent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_PERSON_NAME_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + TAG_DESC_SEMINAR + TAG_DESC_GRADUATION,
                Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_GRADUATION + INVALID_TAG_DESC + VALID_EVENT_TAG_GRADUATION,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_TAG_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_GRADUATION + TAG_DESC_SEMINAR
                + TAG_DESC_GRADUATION, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
    }

}
