package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;

import seedu.address.model.event.EpicEvent;
import seedu.address.ui.EpicEventCard;

/**
 * Provides a handle for {@code EpicEventListPanel} containing the list of {@code EpicEventCard}.
 */
public class EpicEventListPanelHandle extends NodeHandle<ListView<EpicEventCard>> {
    public static final String EPIC_EVENT_LIST_VIEW_ID = "#epicEventListView";

    private Optional<EpicEventCard> lastRememberedSelectedEpicEventCard;

    public EpicEventListPanelHandle(ListView<EpicEventCard> epicEventListPanelNode) {
        super(epicEventListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code EpicEventCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public EpicEventCardHandle getHandleToSelectedCard() {
        List<EpicEventCard> epicEventList = getRootNode().getSelectionModel().getSelectedItems();

        if (epicEventList.size() != 1) {
            throw new AssertionError("Epic Event list size expected 1.");
        }

        return new EpicEventCardHandle(epicEventList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<EpicEventCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the epicEvent.
     */
    public void navigateToCard(EpicEvent epicEvent) {
        List<EpicEventCard> cards = getRootNode().getItems();
        Optional<EpicEventCard> matchingCard = cards.stream().filter(card ->
                card.epicEvent.equals(epicEvent)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("Event does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the EpicEvent card handle of a EpicEvent associated with the {@code index} in the list.
     */
    public EpicEventCardHandle getEpicEventCardHandle(int index) {
        return getEpicEventCardHandle(getRootNode().getItems().get(index).epicEvent);
    }

    /**
     * Returns the {@code EpicEventCardHandle} of the specified {@code epicEvent} in the list.
     */
    public EpicEventCardHandle getEpicEventCardHandle(EpicEvent epicEvent) {
        Optional<EpicEventCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.epicEvent.equals(epicEvent))
                .map(card -> new EpicEventCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Epic Event does not exist."));
    }

    /**
     * Selects the {@code EpicEventCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code EpicEventCard} in the list.
     */
    public void rememberSelectedEpicEventCard() {
        List<EpicEventCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedEpicEventCard = Optional.empty();
        } else {
            lastRememberedSelectedEpicEventCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code EpicEventCard} is different from the value remembered by the most recent
     * {@code rememberSelectedEpicEventCard()} call.
     */
    public boolean isSelectedEpicEventCardChanged() {
        List<EpicEventCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedEpicEventCard.isPresent();
        } else {
            return !lastRememberedSelectedEpicEventCard.isPresent()
                    || !lastRememberedSelectedEpicEventCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
