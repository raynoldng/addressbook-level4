package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_NAME_SEMINAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_GRADUATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TAG_SEMINAR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.EventPlanner;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.exceptions.DuplicateEventException;

/**
 * A utility class containing a list of {@code EpicEvent} objects to be used in tests.
 */
public class TypicalEpicEvents {

    public static final EpicEvent GRDUATION = new EpicEventBuilder().withName("AY201718 Graduation Ceremony")
            .withTags("graduation").build();
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
    public static final EpicEvent ORIENTATION = new EpicEventBuilder().withName("Orientation").build();

    // Manually added
    public static final EpicEvent SOCORIENTATION = new EpicEventBuilder().withName("SOC Orientation")
            .withTags("orientation", "SOC").build();
    public static final EpicEvent IOTTALK = new EpicEventBuilder().withName("IoT Talk").build();

    // Manually added - Event's details found in {@code CommandTestUtil}
    public static final EpicEvent AMY = new EpicEventBuilder().withName(VALID_EVENT_NAME_GRADUATION)
            .withTags(VALID_EVENT_TAG_GRADUATION).build();
    public static final EpicEvent BOB = new EpicEventBuilder().withName(VALID_EVENT_NAME_SEMINAR)
            .withTags(VALID_EVENT_TAG_SEMINAR).build();

    public static final String KEYWORD_MATCHING_OLYMPIAD = "Olympiad"; // A keyword that matches MEIER

    private TypicalEpicEvents() {} // prevents instantiation

    /**
     * Returns an {@code EventPlanner} with all the typical events.
     */
    public static EventPlanner getTypicalAddressBook() {
        EventPlanner ab = new EventPlanner();
        for (EpicEvent event : getTypicalEvents()) {
            try {
                ab.addEvent(event);
            } catch (DuplicateEventException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    public static List<EpicEvent> getTypicalEvents() {
        return new ArrayList<>(Arrays.asList(GRDUATION, FOODSEMINAR, IOTSEMINAR, MATHOLYMPIAD, PHYSICSOLYMPIAD,
                CAREERTALK, ORIENTATION));
    }
}
