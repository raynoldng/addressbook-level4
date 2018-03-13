package seedu.address.model.event;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.Name;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Event in the event planner.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class EpicEvent {

    private Name name;

    private UniqueTagList tags;
    private final UniquePersonList attendees;

    /**
     * Every field must be present and not null.
     */
    public EpicEvent(Name name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.attendees = new UniquePersonList();
    }

    public Name getName() {
        return name;
    }

    /**
     * Edits this person by transferring the fields of dummyPerson over
     */
    public void setEvent(EpicEvent dummyEvent) {
        this.name = dummyEvent.getName();
        this.tags = new UniqueTagList(dummyEvent.getTags());
    }

    /** registers person for this event */
    public void registerPerson(Person person) throws DuplicatePersonException {
        attendees.add(person);
    }

    /** deregisters person from this event */
    public void deregisterPerson(Person person) throws PersonNotFoundInEventException {
        try {
            attendees.remove(person);
        } catch (PersonNotFoundException e) {
            throw new PersonNotFoundInEventException();
        }
    }

    /** returns true if person is in this event */
    public boolean hasPerson(Person person) {
        return attendees.contains(person);
    }

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

        if (!(other instanceof EpicEvent)) {
            return false;
        }

        EpicEvent otherEpicEvent = (EpicEvent) other;
        return otherEpicEvent.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
