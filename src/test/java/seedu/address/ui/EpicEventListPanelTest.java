package seedu.address.ui;

import static org.junit.Assert.assertEquals;

import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalEpicEvents.getTypicalEvents;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.ui.testutil.GuiTestAssert.assertEpicEventCardDisplaysEpicEvent;
import static seedu.address.ui.testutil.GuiTestAssert.assertEpicEventCardEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.EpicEventCardHandle;
import guitests.guihandles.EpicEventListPanelHandle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.model.event.EpicEvent;


//@@author raynoldng
/**
 * Panel containing the list of events.
 */
public class EpicEventListPanelTest extends GuiUnitTest {
    private static final ObservableList<EpicEvent> TYPICAL_EVENTS =
            FXCollections.observableList(getTypicalEvents());

    private static final JumpToEventListRequestEvent JUMP_TO_SECOND_EVENT =
            new JumpToEventListRequestEvent(INDEX_SECOND_EVENT);

    private EpicEventListPanelHandle epicEventListPanelHandle;

    @Before
    public void setUp() {
        EpicEventListPanel epicEventListPanel = new EpicEventListPanel(TYPICAL_EVENTS);
        uiPartRule.setUiPart(epicEventListPanel);

        epicEventListPanelHandle = new EpicEventListPanelHandle(getChildNode(epicEventListPanel.getRoot(),
                EpicEventListPanelHandle.EPIC_EVENT_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_EVENTS.size(); i++) {
            epicEventListPanelHandle.navigateToCard(TYPICAL_EVENTS.get(i));
            EpicEvent expectedEpicEvent = TYPICAL_EVENTS.get(i);
            EpicEventCardHandle actualCard = epicEventListPanelHandle.getEpicEventCardHandle(i);

            assertEpicEventCardDisplaysEpicEvent(expectedEpicEvent, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        EpicEventCardHandle expectedCard =
                epicEventListPanelHandle.getEpicEventCardHandle(INDEX_SECOND_EVENT.getZeroBased());
        EpicEventCardHandle selectedCard = epicEventListPanelHandle.getHandleToSelectedCard();
        assertEpicEventCardEquals(expectedCard, selectedCard);
    }
}
