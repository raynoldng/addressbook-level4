package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyEventPlanner;

/**
 * A class to access EventPlanner data stored as an xml file on the hard disk.
 */
public class XmlEventPlannerStorage implements EventPlannerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlEventPlannerStorage.class);

    private String filePath;

    public XmlEventPlannerStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getEventPlannerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyEventPlanner> readEventPlanner() throws DataConversionException, IOException {
        return readEventPlanner(filePath);
    }

    /**
     * Similar to {@link #readEventPlanner()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyEventPlanner> readEventPlanner(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        File eventPlannerFile = new File(filePath);

        if (!eventPlannerFile.exists()) {
            logger.info("EventPlanner file "  + eventPlannerFile + " not found");
            return Optional.empty();
        }

        XmlSerializableEventPlanner xmlEventPlanner = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlEventPlanner.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + eventPlannerFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveEventPlanner(ReadOnlyEventPlanner eventPlanner) throws IOException {
        saveEventPlanner(eventPlanner, filePath);
    }

    /**
     * Similar to {@link #saveEventPlanner(ReadOnlyEventPlanner)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveEventPlanner(ReadOnlyEventPlanner eventPlanner, String filePath) throws IOException {
        requireNonNull(eventPlanner);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableEventPlanner(eventPlanner));
    }

}
