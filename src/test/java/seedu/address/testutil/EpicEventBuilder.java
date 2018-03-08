package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.event.EpicEvent;
import seedu.address.model.Name;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building EpicEvent objects.
 */
public class EpicEventBuilder {

    public static final String DEFAULT_NAME = "AY2017/18 Graduation Ceremony";
    public static final String DEFAULT_TAGS = "graduation";

    private Name name;
    private Set<Tag> tags;

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
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EpicEvent} that we are building.
     */
    public EpicEventBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public EpicEvent build() {
        return new EpicEvent(name, tags);
    }

}
