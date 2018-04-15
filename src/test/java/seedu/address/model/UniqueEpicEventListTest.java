package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.event.UniqueEpicEventList;

//@@author william6364

public class UniqueEpicEventListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueEpicEventList uniqueEpicEventList = new UniqueEpicEventList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueEpicEventList.asObservableList().remove(0);
    }
}
