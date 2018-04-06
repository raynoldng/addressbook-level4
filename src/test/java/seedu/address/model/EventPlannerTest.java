package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalEpicEvents.GRADUATION;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class EventPlannerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EventPlanner eventPlanner = new EventPlanner();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), eventPlanner.getPersonList());
        assertEquals(Collections.emptyList(), eventPlanner.getPersonTagList());
        assertEquals(Collections.emptyList(), eventPlanner.getEventTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        eventPlanner.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        EventPlanner newData = getTypicalAddressBook();
        eventPlanner.resetData(newData);
        assertEquals(newData, eventPlanner);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ALICE twice
        List<Person> newPersons = Arrays.asList(ALICE, ALICE);
        List<EpicEvent> newEvents = Arrays.asList();
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        EventPlannerStub newData = new EventPlannerStub(newPersons, newEvents, newTags, newTags);

        thrown.expect(AssertionError.class);
        eventPlanner.resetData(newData);
    }

    //@@author william6364
    @Test
    public void resetData_withDuplicateEvents_throwsAssertionError() {
        // Repeat GRADUATION twice
        List<Person> newPersons = Arrays.asList();
        List<EpicEvent> newEvents = Arrays.asList(GRADUATION, GRADUATION);
        List<Tag> newTags = new ArrayList<>(GRADUATION.getTags());
        EventPlannerStub newData = new EventPlannerStub(newPersons, newEvents, newTags, newTags);

        thrown.expect(AssertionError.class);
        eventPlanner.resetData(newData);
    }

    //@@author

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getPersonList().remove(0);
    }

    //@@author william6364

    @Test
    public void getEventList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getEventList().remove(0);
    }

    @Test
    public void getPersonTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getPersonTagList().remove(0);
    }

    @Test
    public void getEventTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        eventPlanner.getEventTagList().remove(0);
    }

    /**
     * A stub ReadOnlyEventPlanner whose persons and tags lists can violate interface constraints.
     */
    private static class EventPlannerStub implements ReadOnlyEventPlanner {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<EpicEvent> events = FXCollections.observableArrayList();
        private final ObservableList<Tag> personTags = FXCollections.observableArrayList();
        private final ObservableList<Tag> eventTags = FXCollections.observableArrayList();

        EventPlannerStub(Collection<Person> persons, Collection<EpicEvent> events, Collection<? extends Tag> personTags,
                         Collection<? extends Tag> eventTags) {
            this.persons.setAll(persons);
            this.events.setAll(events);
            this.personTags.setAll(personTags);
            this.eventTags.setAll(eventTags);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<EpicEvent> getEventList() {
            return events;
        }

        @Override
        public ObservableList<Tag> getPersonTagList() {
            return personTags;
        }

        @Override
        public ObservableList<Tag> getEventTagList() {
            return eventTags;
        }
    }

}
