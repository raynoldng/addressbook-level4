package guitests.guihandles;

import javafx.scene.control.Label;

//@@author raynoldng
public class AttendanceListPanelHeaderHandle extends NodeHandle<Label> {

    public static final String ATTENDANCE_STATUS_ID = "#attendanceStatus";
    public static final String ATTENDANCE_STATUS_FORMAT = "Attendees%s: (%d/%d)";

    public AttendanceListPanelHeaderHandle(Label rootNode) {
        super(rootNode);
    }

    /**
     * Returns the text in the header.
     */
    public String getText() {
        return getRootNode().getText();
    }
}
