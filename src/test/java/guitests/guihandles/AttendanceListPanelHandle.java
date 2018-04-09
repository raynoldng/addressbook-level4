package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;

import seedu.address.model.attendance.Attendance;
import seedu.address.ui.AttendanceCard;


//@@author raynoldng
/**
 * Provides a handle for {@code AttendanceListPanel} containing the list of {@code AttendanceCard}.
 */
public class AttendanceListPanelHandle extends NodeHandle<ListView<AttendanceCard>> {
    public static final String ATTENDANCE_LIST_VIEW_ID = "#attendanceListView";

    private Optional<AttendanceCard> lastRememberedSelectedAttedanceCard;

    public AttendanceListPanelHandle(ListView<AttendanceCard> attendanceListPanelNode) {
        super(attendanceListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code AttendanceCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public AttendanceCardHandle getHandleToSelectedCard() {
        List<AttendanceCard> attendanceList = getRootNode().getSelectionModel().getSelectedItems();

        if (attendanceList.size() != 1) {
            throw new AssertionError("Attendance list size expected 1.");
        }

        return new AttendanceCardHandle(attendanceList.get(0).getRoot());
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
        List<AttendanceCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the epicEvent.
     */
    public void navigateToCard(Attendance attendance) {
        List<AttendanceCard> cards = getRootNode().getItems();
        Optional<AttendanceCard> matchingCard = cards.stream().filter(card ->
                card.getAttendance().equals(attendance)).findFirst();

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
     * Returns the Attendace card handle of a Attendance associated with the {@code index} in the list.
     */
    public AttendanceCardHandle getAttendanceCardHandle(int index) {
        return getAttendanceCardHandle(getRootNode().getItems().get(index).getAttendance());
    }

    /**
     * Returns the {@code AttendanceCardHandle} of the specified {@code attendance} in the list.
     */
    public AttendanceCardHandle getAttendanceCardHandle(Attendance attendance) {
        Optional<AttendanceCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.getAttendance().equals(attendance))
                .map(card -> new AttendanceCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Attendee does not exist."));
    }

    /**
     * Selects the {@code AttendanceCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code AttendanceCard} in the list.
     */
    public void rememberSelectedAttendanceCard() {
        List<AttendanceCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedAttedanceCard = Optional.empty();
        } else {
            lastRememberedSelectedAttedanceCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code AttendanceCard} is different from the value remembered by the most recent
     * {@code rememberSelectedAttendanceCard()} call.
     */
    public boolean isSelectedAttendanceCardChanged() {
        List<AttendanceCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedAttedanceCard.isPresent();
        } else {
            return !lastRememberedSelectedAttedanceCard.isPresent()
                    || !lastRememberedSelectedAttedanceCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
