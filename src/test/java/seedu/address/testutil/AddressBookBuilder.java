package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EventPlanner;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code EventPlanner ab = new AddressBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class AddressBookBuilder {

    private EventPlanner eventPlanner;

    public AddressBookBuilder() {
        eventPlanner = new EventPlanner();
    }

    public AddressBookBuilder(EventPlanner eventPlanner) {
        this.eventPlanner = eventPlanner;
    }

    /**
     * Adds a new {@code Person} to the {@code EventPlanner} that we are building.
     */
    public AddressBookBuilder withPerson(Person person) {
        try {
            eventPlanner.addPerson(person);
        } catch (DuplicatePersonException dpe) {
            throw new IllegalArgumentException("person is expected to be unique.");
        }
        return this;
    }

    /**
     * Parses {@code tagName} into a {@code Tag} and adds it to the {@code EventPlanner} that we are building.
     */
    public AddressBookBuilder withTag(String tagName) {
        try {
            eventPlanner.addPersonTag(new Tag(tagName));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("tagName is expected to be valid.");
        }
        return this;
    }

    public EventPlanner build() {
        return eventPlanner;
    }
}
