package seedu.address.storage;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Person;

//@@author jiangyue12392
/**
 * JAXB-friendly version of the Attendance.
 */
public class XmlAdaptedAttendance {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Attendance's %s field is missing!";

    @XmlElement(required = true)
    private XmlAdaptedPerson attendee;

    @XmlElement
    private Boolean hasAttended;

    /**
     * Constructs an XmlAdaptedAttendance.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAttendance() {}

    /**
     * Constructs an {@code XmlAdaptedAttendance} with the given Attendance details.
     */
    public XmlAdaptedAttendance(XmlAdaptedPerson attendee, Boolean hasAttended) {
        this.attendee = attendee;
        this.hasAttended = hasAttended;
    }

    /**
     * Converts a given Attendance into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAttendance
     */
    public XmlAdaptedAttendance(Attendance source) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(source.getPerson());
        attendee = new XmlAdaptedPerson(source.getPerson());
        hasAttended = source.hasAttended();
    }

    /**
     * Converts this jaxb-friendly adapted Attendance object into the model's Attendance object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Attendance
     */
    public Attendance toModelType() throws IllegalValueException {
        if (this.attendee == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Person.class.getSimpleName()));
        }
        if (this.hasAttended == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Attendance Status"));
        }
        final Person attendee = this.attendee.toModelType();

        final boolean hasAttended = this.hasAttended;

        return new Attendance(attendee, hasAttended);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAttendance)) {
            return false;
        }

        XmlAdaptedAttendance otherAttendance = (XmlAdaptedAttendance) other;
        return Objects.equals(attendee, otherAttendance.attendee)
                && hasAttended == otherAttendance.hasAttended;
    }
}
