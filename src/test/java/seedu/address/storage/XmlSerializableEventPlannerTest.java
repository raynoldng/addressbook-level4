package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.EventPlanner;
import seedu.address.testutil.TypicalPersons;

public class XmlSerializableEventPlannerTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableEventPlannerTest/");
    private static final File TYPICAL_PERSONS_FILE = new File(TEST_DATA_FOLDER + "typicalPersonsAddressBook.xml");
    private static final File INVALID_PERSON_FILE = new File(TEST_DATA_FOLDER + "invalidPersonAddressBook.xml");
    private static final File INVALID_TAG_FILE = new File(TEST_DATA_FOLDER + "invalidTagAddressBook.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        XmlSerializableEventPlanner dataFromFile = XmlUtil.getDataFromFile(TYPICAL_PERSONS_FILE,
                XmlSerializableEventPlanner.class);
        EventPlanner eventPlannerFromFile = dataFromFile.toModelType();
        EventPlanner typicalPersonsEventPlanner = TypicalPersons.getTypicalAddressBookWithoutEvents();
        assertEquals(eventPlannerFromFile, typicalPersonsEventPlanner);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        XmlSerializableEventPlanner dataFromFile = XmlUtil.getDataFromFile(INVALID_PERSON_FILE,
                XmlSerializableEventPlanner.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_invalidTagFile_throwsIllegalValueException() throws Exception {
        XmlSerializableEventPlanner dataFromFile = XmlUtil.getDataFromFile(INVALID_TAG_FILE,
                XmlSerializableEventPlanner.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
}
