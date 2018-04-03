package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Name;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.UniqueAttendanceList;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.tag.Tag;

//@@author jiangyue12392
/**
 * JAXB-friendly version of the EpicEvent.
 */
public class XmlAdaptedEpicEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "EpicEvent's %s field is missing!";

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private List<XmlAdaptedAttendance> attendanceList = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedEpicEvent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEpicEvent() {}

    /**
     * Constructs an {@code XmlAdaptedEpicEvent} with the given epicEvent details.
     */
    public XmlAdaptedEpicEvent(String name, List<XmlAdaptedAttendance> attendanceList, List<XmlAdaptedTag> tagged) {
        this.name = name;

        if (attendanceList != null) {
            this.attendanceList = new ArrayList<>(attendanceList);
        }

        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given EpicEvent into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEpicEvent
     */
    public XmlAdaptedEpicEvent(EpicEvent source) {
        name = source.getName().name;

        attendanceList = new ArrayList<>();
        for (Attendance attendance : source.getAttendanceList()) {
            attendanceList.add(new XmlAdaptedAttendance(attendance));
        }

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted EpicEvent object into the model's EpicEvent object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted epicEvent
     */
    public EpicEvent toModelType() throws IllegalValueException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        final UniqueAttendanceList attendances = new UniqueAttendanceList();
        for (XmlAdaptedAttendance attendance : attendanceList) {
            attendances.add(attendance.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        final Set<Tag> tags = new HashSet<>(eventTags);
        return new EpicEvent(name, attendances, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedEpicEvent)) {
            return false;
        }

        XmlAdaptedEpicEvent otherEpicEvent = (XmlAdaptedEpicEvent) other;
        return Objects.equals(name, otherEpicEvent.name)
                && attendanceList.equals(otherEpicEvent.attendanceList)
                && tagged.equals(otherEpicEvent.tagged);
    }
}
