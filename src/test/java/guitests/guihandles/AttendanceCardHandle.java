package guitests.guihandles;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

// @@author raynoldng
/**
 * Provides a handle to an attendance card in the attendance list panel.
 */
public class AttendanceCardHandle extends PersonCardHandle {

    private static final String IMAGEVIEW_FIELD_ID = "#attendanceToggleImage";

    private final ImageView attendanceToggleImage;

    public AttendanceCardHandle(Node cardNode) {
        super(cardNode);
        attendanceToggleImage = getChildNode(IMAGEVIEW_FIELD_ID);
    }

    public ImageView getAttendanceToggleImage() {
        return attendanceToggleImage;
    }
}
