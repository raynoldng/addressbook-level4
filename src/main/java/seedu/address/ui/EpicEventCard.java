
package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.event.EpicEvent;
// @@author raynoldng
/**
 * An UI component that displays information of an {@code EpicEvent}.
 */
public class EpicEventCard extends UiPart<Region> {

    private static final String FXML = "EpicEventListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on EventPlanner level 4</a>
     */

    public final EpicEvent epicEvent;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;

    public EpicEventCard(EpicEvent epicEvent, int displayedIndex) {
        super(FXML);
        this.epicEvent = epicEvent;
        id.setText(displayedIndex + ". ");
        name.setText(epicEvent.getName().name);
        epicEvent.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EpicEventCard)) {
            return false;
        }

        // state check
        EpicEventCard card = (EpicEventCard) other;
        return id.getText().equals(card.id.getText())
                && epicEvent.equals(card.epicEvent);
    }
}
