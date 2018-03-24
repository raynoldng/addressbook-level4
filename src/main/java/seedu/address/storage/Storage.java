package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.events.model.EventPlannerChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEventPlanner;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends EventPlannerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getEventPlannerFilePath();

    @Override
    Optional<ReadOnlyEventPlanner> readEventPlanner() throws DataConversionException, IOException;

    @Override
    void saveEventPlanner(ReadOnlyEventPlanner eventPlanner) throws IOException;

    /**
     * Saves the current version of the Event Planner to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleEventPlannerChangedEvent(EventPlannerChangedEvent abce);
}
