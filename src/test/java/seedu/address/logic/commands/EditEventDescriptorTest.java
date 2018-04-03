package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_SEMINAR;

import org.junit.Test;

import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.testutil.EditEventDescriptorBuilder;

//@@author jiangyue12392
public class EditEventDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditEventDescriptor descriptorWithSameValues = new EditEventDescriptor(DESC_GRADUATION);
        assertTrue(DESC_GRADUATION.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_GRADUATION.equals(DESC_GRADUATION));

        // null -> returns false
        assertFalse(DESC_GRADUATION.equals(null));

        // different types -> returns false
        assertFalse(DESC_GRADUATION.equals(5));

        // different values -> returns false
        assertFalse(DESC_GRADUATION.equals(DESC_SEMINAR));

        // different name -> returns false
        EditEventDescriptor editedGraduation = new EditEventDescriptorBuilder(DESC_GRADUATION)
                .withName(VALID_EVENT_NAME_SEMINAR).build();
        assertFalse(DESC_GRADUATION.equals(editedGraduation));

        // different tags -> returns false
        editedGraduation = new EditEventDescriptorBuilder(DESC_GRADUATION).withTags(VALID_EVENT_TAG_SEMINAR).build();
        assertFalse(DESC_GRADUATION.equals(editedGraduation));
    }
}
