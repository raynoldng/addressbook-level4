package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_SEMINAR;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.EventPlanner;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

//@@author jiangyue12392

/**
 * A utility class containing a list of {@code EpicEvent} objects to be used in tests.
 */
public class TypicalEpicEvents {

    public static final EpicEvent GRADUATIONAY18 = new EpicEventBuilder().withName("AY201718 Graduation Ceremony")
            .withAttendees(Arrays.asList(ALICE, CARL, DANIEL, ELLE, FIONA, GEORGE)).withTags("graduation").build();
    public static final EpicEvent FOODSEMINAR = new EpicEventBuilder().withName("Food Seminar")
            .withTags("seminar", "food").build();
    public static final EpicEvent IOTSEMINAR = new EpicEventBuilder().withName("IoT Seminar")
            .withTags("seminar", "IoT").build();
    public static final EpicEvent MATHOLYMPIAD = new EpicEventBuilder().withName("Math Olympiad")
            .withTags("competition", "math").build();
    public static final EpicEvent PHYSICSOLYMPIAD = new EpicEventBuilder().withName("Physics Olympiad")
            .withTags("competition", "physics").build();
    public static final EpicEvent CAREERTALK = new EpicEventBuilder().withName("Career Talk")
            .withTags("talk", "career").build();
    public static final EpicEvent ORIENTATION = new EpicEventBuilder().withName("Orientation")
            .withAttendees(Arrays.asList(CARL)).build();

    public static final int GRADUATIONAY18_INDEX = 0;
    public static final int ORIENTATION_INDEX = 6;

    // Manually added - Event's details found in {@code CommandTestUtil}
    public static final EpicEvent GRADUATION = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
            .withTags(VALID_EVENT_TAG_GRADUATION).build();
    public static final EpicEvent SEMINAR = new EpicEventBuilder().withName(VALID_EVENT_NAME_SEMINAR)
            .withTags(VALID_EVENT_TAG_SEMINAR).build();

    public static final String KEYWORD_MATCHING_OLYMPIAD = "Olympiad"; // A keyword that matches OLYMPIAD

    private TypicalEpicEvents() {} // prevents instantiation

    /**
     * Returns an {@code EventPlanner} with all the typical events.
     */
    public static EventPlanner getTypicalEventPlanner() {
        EventPlanner ep = new EventPlanner();
        for (Person person : getTypicalPersons()) {
            try {
                ep.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        for (EpicEvent event : getTypicalEvents()) {
            try {
                ep.addEvent(event);
            } catch (DuplicateEventException e) {
                throw new AssertionError("not possible");
            }
        }

        return ep;
    }

    public static List<EpicEvent> getTypicalEvents() {
        return new ArrayList<>(Arrays.asList(GRADUATIONAY18, FOODSEMINAR, IOTSEMINAR, MATHOLYMPIAD, PHYSICSOLYMPIAD,
                CAREERTALK, ORIENTATION));

    }
}
