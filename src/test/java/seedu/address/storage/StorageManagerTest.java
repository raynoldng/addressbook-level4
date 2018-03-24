package seedu.address.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.EventPlannerChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.EventPlanner;
import seedu.address.model.ReadOnlyEventPlanner;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlEventPlannerStorage addressBookStorage = new XmlEventPlannerStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlEventPlannerStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlEventPlannerStorageTest} class.
         */
        EventPlanner original = getTypicalAddressBook();
        storageManager.saveEventPlanner(original);
        ReadOnlyEventPlanner retrieved = storageManager.readEventPlanner().get();
        assertEquals(original, new EventPlanner(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getEventPlannerFilePath());
    }

    @Test
    public void handleAddressBookChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlEventPlannerStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"));
        storage.handleEventPlannerChangedEvent(new EventPlannerChangedEvent(new EventPlanner()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlEventPlannerStorageExceptionThrowingStub extends XmlEventPlannerStorage {

        public XmlEventPlannerStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveEventPlanner(ReadOnlyEventPlanner eventPlanner, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
