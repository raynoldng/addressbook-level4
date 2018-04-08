package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedEpicEvent.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalEpicEvents.MATHOLYMPIAD;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Name;
import seedu.address.testutil.Assert;

//@@author jiangyue12392
public class XmlAdaptedEpicEventTest {
    private static final String INVALID_NAME = "M@th Olympiad";
    private static final String INVALID_TAG = "#olympiad";

    private static final String VALID_NAME = MATHOLYMPIAD.getName().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = MATHOLYMPIAD.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validEpicEventDetails_returnsEpicEvent() throws Exception {
        XmlAdaptedEpicEvent event = new XmlAdaptedEpicEvent(MATHOLYMPIAD);
        assertEquals(MATHOLYMPIAD, event.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedEpicEvent event =
                new XmlAdaptedEpicEvent(INVALID_NAME, null, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedEpicEvent event = new XmlAdaptedEpicEvent(null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, event::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedEpicEvent event =
                new XmlAdaptedEpicEvent(VALID_NAME, null, invalidTags);
        Assert.assertThrows(IllegalValueException.class, event::toModelType);
    }

}
