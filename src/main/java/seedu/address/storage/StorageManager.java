package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.EventPlannerChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEventPlanner;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of EventPlanner data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private EventPlannerStorage eventPlannerStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(EventPlannerStorage eventPlannerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.eventPlannerStorage = eventPlannerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ EventPlanner methods ==============================

    @Override
    public String getEventPlannerFilePath() {
        return eventPlannerStorage.getEventPlannerFilePath();
    }

    @Override
    public Optional<ReadOnlyEventPlanner> readEventPlanner() throws DataConversionException, IOException {
        return readEventPlanner(eventPlannerStorage.getEventPlannerFilePath());
    }

    @Override
    public Optional<ReadOnlyEventPlanner> readEventPlanner(String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return eventPlannerStorage.readEventPlanner(filePath);
    }

    @Override
    public void saveEventPlanner(ReadOnlyEventPlanner eventPlanner) throws IOException {
        saveEventPlanner(eventPlanner, eventPlannerStorage.getEventPlannerFilePath());
    }

    @Override
    public void saveEventPlanner(ReadOnlyEventPlanner eventPlanner, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        eventPlannerStorage.saveEventPlanner(eventPlanner, filePath);
    }


    @Override
    @Subscribe
    public void handleEventPlannerChangedEvent(EventPlannerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveEventPlanner(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
