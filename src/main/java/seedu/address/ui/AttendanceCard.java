package seedu.address.ui;

import java.util.Arrays;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import seedu.address.model.attendance.Attendance;

// @@author raynoldng
/**
 * An UI component that displays information of a {@code Person}.
 */
public class AttendanceCard extends PersonCard {

    protected static final String FXML = "AttendanceListCard.fxml";

    private static final String ICON_ATTENDED_URL = "/images/green_tick.png";
    private static final String ICON_NOT_ATTENDED_URL = "/images/red_cross.png";

    private static final Image ICON_ATTENDED = new Image(ICON_ATTENDED_URL);
    private static final Image ICON_NOT_ATTENDED = new Image(ICON_NOT_ATTENDED_URL);
    private static final List<Image> images = Arrays.asList(ICON_ATTENDED, ICON_NOT_ATTENDED);

    @FXML
    private ImageView attendanceToggleImage;

    private Attendance attendance;
    private IntegerProperty intValue;

    public AttendanceCard(Attendance attendee, int displayedIndex) {
        super(attendee.getPerson(), displayedIndex, FXML);
        this.attendance = attendee;
        intValue = new SimpleIntegerProperty();

        intValue.set(attendee.hasAttended() ? 0 : 1);
        attendanceToggleImage.imageProperty().bind(Bindings.createObjectBinding(() -> images.get(intValue.getValue())));
    }

    public Attendance getAttendance() {
        return attendance;
    }

}
