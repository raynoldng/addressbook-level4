package seedu.address.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores eventplanner data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given eventplanner data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableEventPlanner eventPlanner)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, eventPlanner);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns event planner in the file or an empty event planner
     */
    public static XmlSerializableEventPlanner loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableEventPlanner.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
