package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertEpicEventCardDisplaysEpicEvent;

import org.junit.Test;

import guitests.guihandles.EpicEventCardHandle;

import seedu.address.model.event.EpicEvent;
import seedu.address.testutil.EpicEventBuilder;

// @@author raynoldng
public class EpicEventCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        EpicEvent eventWithoutTags = new EpicEventBuilder().withTags(new String[0]).build();
        EpicEventCard eventCard = new EpicEventCard(eventWithoutTags, 1);
        uiPartRule.setUiPart(eventCard);
        assertEpicEventCardDisplay(eventCard, eventWithoutTags, 1);

        // with tags
        EpicEvent eventWithTags = new EpicEventBuilder().build();
        eventCard = new EpicEventCard(eventWithTags, 2);
        uiPartRule.setUiPart(eventCard);
        assertEpicEventCardDisplay(eventCard, eventWithTags, 2);
    }

    @Test
    public void equals() {
        EpicEvent event = new EpicEventBuilder().build();
        EpicEventCard eventCard = new EpicEventCard(event, 0);

        // same event, same index -> return true
        EpicEventCard copy = new EpicEventCard(event, 0);
        assertTrue(eventCard.equals(eventCard));

        // same object -> returns true
        assertTrue(eventCard.equals(eventCard));

        // null -> returns false
        assertFalse(eventCard.equals(null));

        // different types -> return false
        assertFalse(eventCard.equals(0));

        // different card, same index -> returns false
        EpicEvent differentEvent = new EpicEventBuilder().withName("differentEvent").build();
        assertFalse(eventCard.equals(new EpicEventCard(differentEvent, 0)));

        // same event, different index -> returns false
        assertFalse(eventCard.equals(new EpicEventCard(event, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertEpicEventCardDisplay(EpicEventCard eventCard, EpicEvent expectedEvent, int expectedId) {
        guiRobot.pauseForHuman();

        EpicEventCardHandle epicCardHandle = new EpicEventCardHandle(eventCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", epicCardHandle.getId());

        // verify person details are displayed correctly
        assertEpicEventCardDisplaysEpicEvent(expectedEvent, epicCardHandle);
    }
}
