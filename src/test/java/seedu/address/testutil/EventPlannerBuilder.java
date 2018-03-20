package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EventPlanner;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Eventplanner objects.
 * Example usage: <br>
 *     {@code EventPlanner ab = new EventPlannerBuilder().withPerson("John", "Doe").withPersonTag("Friend").build();}
 */
public class EventPlannerBuilder {

    private EventPlanner eventPlanner;

    public EventPlannerBuilder() {
        eventPlanner = new EventPlanner();
    }

    public EventPlannerBuilder(EventPlanner eventPlanner) {
        this.eventPlanner = eventPlanner;
    }

    /**
     * Adds a new {@code Person} to the {@code EventPlanner} that we are building.
     */
    public EventPlannerBuilder withPerson(Person person) {
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
    public EventPlannerBuilder withPersonTag(String tagName) {
        try {
            eventPlanner.addPersonTag(new Tag(tagName));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("tagName is expected to be valid.");
        }
        return this;
    }

    //TODO: Allow adding of events and event tags

    public EventPlanner build() {
        return eventPlanner;
    }
}
