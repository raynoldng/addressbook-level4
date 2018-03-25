package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.EventPlanner;
import seedu.address.model.ReadOnlyEventPlanner;

/**
 * Represents a storage for {@link EventPlanner}.
 */
public interface EventPlannerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getEventPlannerFilePath();

    /**
     * Returns EventPlanner data as a {@link ReadOnlyEventPlanner}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEventPlanner> readEventPlanner() throws DataConversionException, IOException;

    /**
     * @see #getEventPlannerFilePath()
     */
    Optional<ReadOnlyEventPlanner> readEventPlanner(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyEventPlanner} to the storage.
     * @param eventPlanner cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveEventPlanner(ReadOnlyEventPlanner eventPlanner) throws IOException;

    /**
     * @see #saveEventPlanner(ReadOnlyEventPlanner)
     */
    void saveEventPlanner(ReadOnlyEventPlanner eventPlanner, String filePath) throws IOException;

}
