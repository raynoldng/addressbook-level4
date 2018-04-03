package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seedu.address.MainApp;
import seedu.address.model.attendance.Attendance;

/**
 * An UI component that displays information of a {@code Person}.
 */
// @@author raynoldng
public class AttendanceCard extends PersonCard {

    protected static final String FXML = "AttendanceListCard.fxml";

    private static final String ICON_ATTENDED_URL = "/images/green_tick.png";
    private static final String ICON_NOT_ATTENDED_URL = "/images/red_cross.png";

    public static final Image ICON_ATTENDED = new Image(ICON_ATTENDED_URL);
    public static final Image ICON_NOT_ATTENDED = new Image(ICON_NOT_ATTENDED_URL);

    @FXML
    private ImageView attendanceToggleImage;

    private Attendance attendance;
    private Image currentIcon;

    public AttendanceCard(Attendance attendee, int displayedIndex) {
        super(attendee.getPerson(), displayedIndex, FXML);
        this.attendance = attendee;
        if (attendee.hasAttended()) {
            attendanceToggleImage.setImage(ICON_ATTENDED);
            currentIcon = ICON_ATTENDED;
        } else {
            attendanceToggleImage.setImage(ICON_NOT_ATTENDED);
            currentIcon = ICON_NOT_ATTENDED;
        }
    }

    private Image getImage(String imagePath) {
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    private void updateImage() {
        Image nextIcon = attendance.hasAttended() ? ICON_ATTENDED : ICON_NOT_ATTENDED;
        if (nextIcon != currentIcon) {
            currentIcon = nextIcon;
            attendanceToggleImage.setImage(currentIcon);
        }
    }
}