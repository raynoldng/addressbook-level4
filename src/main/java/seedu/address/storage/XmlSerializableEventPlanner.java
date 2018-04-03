package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EventPlanner;
import seedu.address.model.ReadOnlyEventPlanner;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * An Immutable EventPlanner that is serializable to XML format
 */
@XmlRootElement(name = "eventplanner")
public class XmlSerializableEventPlanner {

    @XmlElement
    private List<XmlAdaptedPerson> persons;
    @XmlElement
    private List<XmlAdaptedEpicEvent> epicEvents;
    @XmlElement
    private List<XmlAdaptedTag> personTags;
    @XmlElement
    private List<XmlAdaptedTag> eventTags;

    /**
     * Creates an empty XmlSerializableEventPlanner.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableEventPlanner() {
        persons = new ArrayList<>();
        epicEvents = new ArrayList<>();
        personTags = new ArrayList<>();
        eventTags = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableEventPlanner(ReadOnlyEventPlanner src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
        epicEvents.addAll(src.getEventList().stream().map(XmlAdaptedEpicEvent::new).collect(Collectors.toList()));
        personTags.addAll(src.getPersonTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        eventTags.addAll(src.getEventTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
    }

    //@@author jiangyue12392
    /**
     * Finds and replaces the dummy person in attendance list with the person object in the master list
     */
    void setPersonForAttendance(UniquePersonList persons, EpicEvent event) {
        for (Person p : persons) {
            event.replace(p);
        }
    }
    //@@author

    /**
     * Converts this eventplanner into the model's {@code EventPlanner} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson}, {@code XmlAdaptedEpicEvent} or {@code XmlAdaptedTag}.
     */
    public EventPlanner toModelType() throws IllegalValueException {
        EventPlanner eventPlanner = new EventPlanner();
        for (XmlAdaptedTag t : personTags) {
            eventPlanner.addPersonTag(t.toModelType());
        }
        for (XmlAdaptedTag t : eventTags) {
            eventPlanner.addEventTag(t.toModelType());
        }
        for (XmlAdaptedPerson p : persons) {
            eventPlanner.addPerson(p.toModelType());
        }
        for (XmlAdaptedEpicEvent e: epicEvents) {
            EpicEvent newEpicEvent = e.toModelType();
            setPersonForAttendance(eventPlanner.getPersonMaseterList(), newEpicEvent);
            eventPlanner.addEvent(newEpicEvent);
        }
        return eventPlanner;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableEventPlanner)) {
            return false;
        }

        XmlSerializableEventPlanner otherEp = (XmlSerializableEventPlanner) other;
        return persons.equals(otherEp.persons) && epicEvents.equals(otherEp.epicEvents)
                && eventTags.equals(otherEp.eventTags)
                && personTags.equals(otherEp.personTags);
    }
}
