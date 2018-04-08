package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javafx.beans.property.SimpleObjectProperty;
import seedu.address.model.Name;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the event planner.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person extends SimpleObjectProperty {

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private int numberOfEventsRegisteredFor;

    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.numberOfEventsRegisteredFor = 0;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    public Person(Name name, Phone phone, Email email, Address address, int numberOfEvents, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.numberOfEventsRegisteredFor = numberOfEvents;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    public Person(Person toBeCopied) {
        requireNonNull(toBeCopied);
        this.name = new Name(toBeCopied.getFullName().toString());
        this.phone = new Phone(toBeCopied.getPhone().toString());
        this.email = new Email(toBeCopied.getEmail().toString());
        this.address = new Address(toBeCopied.getAddress().toString());
        this.numberOfEventsRegisteredFor = toBeCopied.getNumberOfEventsRegisteredFor();
        this.tags = new UniqueTagList(toBeCopied.getTags());
    }

    public Name getFullName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public int getNumberOfEventsRegisteredFor() {
        return numberOfEventsRegisteredFor;
    }

    public void decrementNumberOfEventsRegisteredFor() {
        numberOfEventsRegisteredFor--;
    }

    public void incrementNumberOfEventsRegisteredFor() {
        numberOfEventsRegisteredFor++;
    }

    //@@author bayweiheng
    /**
     * Edits this person by transferring the fields of dummyPerson over.
     * Used for mutable edit command
     */
    public void setPerson(Person dummyPerson) {
        this.name = dummyPerson.getFullName();
        this.phone = dummyPerson.getPhone();
        this.email = dummyPerson.getEmail();
        this.address = dummyPerson.getAddress();
        this.tags = new UniqueTagList(dummyPerson.getTags());
        fireValueChangedEvent();
    }
    //@@author

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getFullName().equals(this.getFullName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getFullName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
