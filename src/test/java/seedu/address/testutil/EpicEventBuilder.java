package seedu.address.testutil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.Name;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

//@@author william6364

/**
 * A utility class to help with building EpicEvent objects.
 */
public class EpicEventBuilder {

    public static final String DEFAULT_NAME = "MOCK AY201718 Graduation Ceremony";
    public static final String DEFAULT_TAGS = "graduation";

    private Name name;
    private Set<Tag> tags;
    private List<Person> attendees;

    public EpicEventBuilder() {
        name = new Name(DEFAULT_NAME);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
    }

    /**
     * Initializes the EpicEventBuilder with the data of {@code eventToCopy}.
     */
    public EpicEventBuilder(EpicEvent eventToCopy) {
        name = eventToCopy.getName();
        tags = new HashSet<>(eventToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code attendees} of the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withAttendees(List<Person> attendees) {
        this.attendees = attendees;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Builds the {@code EpicEvent} with the {@code Name}, {@code tags}, {@code attendees} set
     */
    public EpicEvent build() {
        EpicEvent event = new EpicEvent(name, tags);
        if (attendees != null) {
            for (Person person : attendees) {
                try {
                    event.registerPerson(person);
                } catch (DuplicateAttendanceException e) {
                    throw new AssertionError("Same person registered multiple twice");
                }
            }
        }
        return event;
    }

}
