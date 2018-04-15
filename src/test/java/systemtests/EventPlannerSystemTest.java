package systemtests;

import static guitests.guihandles.AttendanceListPanelHeaderHandle.ATTENDANCE_STATUS_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ATTENDEES;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.AttendanceListPanelHandle;
import guitests.guihandles.AttendanceListPanelHeaderHandle;
import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.EpicEventListPanelHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.PersonListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;

import seedu.address.TestApp;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FindEventCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.commands.ListPersonCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SelectEventCommand;
import seedu.address.model.EventPlanner;
import seedu.address.model.Model;
import seedu.address.testutil.TypicalEpicEvents;
import seedu.address.ui.CommandBox;

/**
 * A system test class for EventPlanner, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class EventPlannerSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() throws Exception {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected EventPlanner getInitialData() {
        return TypicalEpicEvents.getTypicalEventPlanner();
    }

    /**
     * Returns the directory of the data file.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public PersonListPanelHandle getPersonListPanel() {
        return mainWindowHandle.getPersonListPanel();
    }

    public EpicEventListPanelHandle getEventListPanel() {
        return mainWindowHandle.getEventListPanel();
    }

    public AttendanceListPanelHandle getAttendanceListPanel() {
        return mainWindowHandle.getAttendanceListPanel();
    }

    public AttendanceListPanelHeaderHandle getAttendanceListPanelHeader() {
        return mainWindowHandle.getAttendanceListPanelHeader();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }


    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {

        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

        //    waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Displays all persons in the event planner.
     */
    protected void showAllPersons() {
        executeCommand(ListPersonCommand.COMMAND_WORD);
        assertEquals(getModel().getEventPlanner().getPersonList().size(), getModel().getFilteredPersonList().size());
    }

    /**
     * Displays all persons with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showPersonsWithName(String keyword) {
        executeCommand(FindPersonCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredPersonList().size()
                < getModel().getEventPlanner().getPersonList().size());
    }


    /**
     * Selects the person at {@code index} of the displayed list.
     */
    protected void selectPerson(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getPersonListPanel().getSelectedCardIndex());
    }

    /**
     * Clear the event planner.
     */
    protected void clearEventPlanner() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getEventPlanner().getPersonList().size());
    }

    //@@author william6364
    /**
     * Displays all events with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showEventsWithName(String keyword) {
        executeCommand(FindEventCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredEventList().size()
                < getModel().getEventPlanner().getEventList().size());
    }

    /**
     * Selects the event at {@code index} of the displayed list.
     */
    protected void selectEvent(Index index) {
        executeCommand(SelectEventCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getEventListPanel().getSelectedCardIndex());
    }

    //@@author

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the model and storage contains the same person objects as {@code expectedModel}
     * and the person list panel displays the persons in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(expectedModel, getModel());
        assertEquals(expectedModel.getEventPlanner(), testApp.readStorageAddressBook());
        assertListMatching(getPersonListPanel(), expectedModel.getFilteredPersonList());
    }

    //@@author raynoldng
    /**
     * Asserts that the {@code AttendanceListPanel} displays the expected message, i.e.: filtered if so and attendance
     * count is displayed correctly
     */
    protected void assertAttendanceListHeaderDisplaysExpected(Model expectedModel) {
        boolean isFiltered = expectedModel.getSelectedEpicEvent().getFilteredAttendees().getPredicate()
                != PREDICATE_SHOW_ALL_ATTENDEES;

        if (isFiltered) {
            assert(getAttendanceListPanelHeader().getText().contains("(filtered)"));
        }

        int numAttended = (int) expectedModel.getSelectedEpicEvent().getFilteredAttendees().stream()
                .filter(attendance -> attendance.hasAttended())
                .count();
        int total = expectedModel.getSelectedEpicEvent().getFilteredAttendees().size();
        String expected = String.format(ATTENDANCE_STATUS_FORMAT, isFiltered ? "(filtered)" : "", numAttended,
                total);
        assertEquals(expected, getAttendanceListPanelHeader().getText());

    }
    //@@author

    /**
     * Calls {@code PersonListPanelHandle}, {@code EventListPanelHandle}
     * and {@code StatusBarFooterHandle} to remember their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getPersonListPanel().rememberSelectedPersonCard();
        getEventListPanel().rememberSelectedEpicEventCard();
    }

    /**
     * Asserts that the previously selected card is now deselected
     * @see BrowserPanelHandle#isUrlChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getPersonListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the person in the person list panel selected is changed
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see PersonListPanelHandle#isSelectedPersonCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        assertEquals(expectedSelectedCardIndex.getZeroBased(), getPersonListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the selected card in the person list panel remain unchanged.
     * @see PersonListPanelHandle#isSelectedPersonCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getPersonListPanel().isSelectedPersonCardChanged());
    }

    /**
     * Asserts that the event in the event list panel selected is changed
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see PersonListPanelHandle#isSelectedPersonCardChanged()
     */
    protected void assertSelectedEpicEventCardChanged(Index expectedSelectedCardIndex) {
        assertEquals(expectedSelectedCardIndex.getZeroBased(), getEventListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the selected card in the events list panel remain unchanged.
     * @see PersonListPanelHandle#isSelectedPersonCardChanged()
     */
    protected void assertSelectedEpicEventCardUnchanged() {
        assertFalse(getEventListPanel().isSelectedEpicEventCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        try {
            assertEquals("", getCommandBox().getInput());
            assertEquals("", getResultDisplay().getText());
            assertListMatching(getPersonListPanel(), getModel().getFilteredPersonList());
            //        assertEquals(MainApp.class.getResource(FXML_FILE_FOLDER
            //        + DEFAULT_PAGE), getBrowserPanel().getLoadedUrl());
            assertEquals("./" + testApp.getStorageSaveLocation(), getStatusBarFooter().getSaveLocation());
            assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
        } catch (Exception e) {
            throw new AssertionError("Starting state is wrong.", e);
        }
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
