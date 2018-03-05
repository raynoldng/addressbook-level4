package seedu.address.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_NOTUSED;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;


public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ALICE twice
        List<Person> newPersons = Arrays.asList(ALICE, ALICE);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        AddressBookStub newData = new AddressBookStub(newPersons, newTags);

        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getPersonList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getTagList().remove(0);
    }

    @Test
    public void updatePerson_personAndTagsListUpdated() throws Exception {
        AddressBook addressBookChangedtoAmy = new AddressBookBuilder().withPerson(BOB).build();
        addressBookChangedtoAmy.updatePerson(BOB, AMY);

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(AMY).build();

        assertEquals(expectedAddressBook, addressBookChangedtoAmy);
    }

    @Test
    public void removeTag_nonExistentTag_AddressBookUnchanged() throws Exception {
        AddressBook addressBookWithBob = new AddressBookBuilder().withPerson(BOB).build();
        addressBookWithBob.removeTag(new Tag(VALID_TAG_NOTUSED));

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(BOB).build();

        assertEquals(expectedAddressBook, addressBookWithBob);
    }

    @Test
    public void removeTag_severalContactsWithTag_tagRemoved() throws Exception {
        AddressBook addressBookWithAmyAndBob = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();
        addressBookWithAmyAndBob.removeTag(new Tag(VALID_TAG_FRIEND));

        Person amyNoFriendTag = new PersonBuilder(AMY).withTags().build();
        Person bobNoFriendTag = new PersonBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(amyNoFriendTag)
                .withPerson(bobNoFriendTag).build();

        assertEquals(expectedAddressBook, addressBookWithAmyAndBob);
    }


    /**
     * A stub ReadOnlyAddressBook whose persons and tags lists can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<? extends Tag> tags) {
            this.persons.setAll(persons);
            this.tags.setAll(tags);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }
    }

}
