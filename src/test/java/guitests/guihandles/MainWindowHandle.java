package guitests.guihandles;

import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final PersonListPanelHandle personListPanel;
    private final EpicEventListPanelHandle eventListPanel;
    private final AttendanceListPanelHandle attendanceListPanel;
    private final AttendanceListPanelHeaderHandle attendanceListPanelHeader;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    //  private final BrowserPanelHandle browserPanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        personListPanel = new PersonListPanelHandle(getChildNode(PersonListPanelHandle.PERSON_LIST_VIEW_ID));
        eventListPanel = new EpicEventListPanelHandle(getChildNode(EpicEventListPanelHandle.EPIC_EVENT_LIST_VIEW_ID));
        attendanceListPanel = new AttendanceListPanelHandle(getChildNode(
                AttendanceListPanelHandle.ATTENDANCE_LIST_VIEW_ID));
        attendanceListPanelHeader = new AttendanceListPanelHeaderHandle(getChildNode(
                AttendanceListPanelHeaderHandle.ATTENDANCE_STATUS_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
    }

    public PersonListPanelHandle getPersonListPanel() {
        return personListPanel;
    }

    public EpicEventListPanelHandle getEventListPanel() {
        return eventListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    public AttendanceListPanelHandle getAttendanceListPanel() {
        return attendanceListPanel;
    }

    public AttendanceListPanelHeaderHandle getAttendanceListPanelHeader() {
        return attendanceListPanelHeader;
    }
}
