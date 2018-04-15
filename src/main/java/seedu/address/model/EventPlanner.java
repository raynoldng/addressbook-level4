package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.attendance.exceptions.DuplicateAttendanceException;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.event.UniqueEpicEventList;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the event planner level
 * Duplicates are not allowed (by .equals comparison)
 */
public class EventPlanner implements ReadOnlyEventPlanner {

    private final UniquePersonList persons;
    private final UniqueEpicEventList events;
    private final UniqueTagList personTags;
    private final UniqueTagList eventTags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        events = new UniqueEpicEventList();
        personTags = new UniqueTagList();
        eventTags = new UniqueTagList();
    }

    public EventPlanner() {}

    /**
     * Creates an EventPlanner using the Persons and Tags in the {@code toBeCopied}
     */
    public EventPlanner(ReadOnlyEventPlanner toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setEvents(List<EpicEvent> events) throws DuplicateEventException {
        this.events.setEvents(events);
    }
    public void setPersonTags(Set<Tag> tags) {
        this.personTags.setTags(tags);
    }

    public void setEventTags(Set<Tag> tags) {
        this.eventTags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code EventPlanner} with {@code newData}.
     */
    public void resetData(ReadOnlyEventPlanner newData) {
        requireNonNull(newData);
        setPersonTags(new HashSet<>(newData.getPersonTagList()));
        setEventTags(new HashSet<>(newData.getEventTagList()));
        List<Person> syncedPersonList = newData.getPersonList().stream()
                .collect(Collectors.toList());
        List<EpicEvent> syncedEventList = newData.getEventList().stream()
                .collect(Collectors.toList());

        try {
            setPersons(syncedPersonList);
        } catch (DuplicatePersonException e) {
            throw new AssertionError("EventPlanners should not have duplicate persons");
        }
        try {
            setEvents(syncedEventList);
        } catch (DuplicateEventException e) {
            throw new AssertionError("EventPlanners should not have duplicate events");
        }
    }

    //// person-level operations

    /**
     * Adds a person to the event planner
     * Also checks the new person's tags and updates {@link #personTags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #personTags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person p) throws DuplicatePersonException {
        Person person = syncPersonWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.add(person);
    }

    /**
     * Replaces the given person {@code targetPerson} in the list with {@code editedPerson}.
     * {@code EventPlanner}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code targetPerson} could not be found in the list.
     *
     * @see #syncPersonWithMasterTagList(Person)
     */
    public void updatePerson(Person targetPerson, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        Person syncedEditedPerson = syncPersonWithMasterTagList(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.setPerson(targetPerson, syncedEditedPerson);
    }

    /**
     *  Updates the master tag list to include tags in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     *  list.
     */
    private Person syncPersonWithMasterTagList(Person person) {
        final UniqueTagList newPersonTags = new UniqueTagList(person.getTags());
        final Map<Tag, Tag> masterTagObjects = updateMasterPersonTagList(newPersonTags);

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        newPersonTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Person(
                person.getFullName(), person.getPhone(), person.getEmail(), person.getAddress(), correctTagReferences);
    }

    /**
     * Removes {@code personKey} from this {@code EventPlanner}.
     * @throws PersonNotFoundException if the {@code personKey} is not in this {@code EventPlanner}.
     */
    public boolean removePerson(Person personKey) throws PersonNotFoundException {
        if (persons.remove(personKey)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //@@author william6364

    //// event-level operation

    /**
     * Adds an event to the event planner
     * Also checks the new event's tags and updates {@link #eventTags} with any new tags found,
     * and updates the Tag objects in the event to point to those in {@link #eventTags}.
     *
     * @throws DuplicateEventException if an equivalent event already exists.
     */
    public void addEvent(EpicEvent e) throws DuplicateEventException {
        EpicEvent event = syncEventWithMasterTagList(e);
        events.add(event);
        event.setAttendanceList(e.getAttendanceList());
        event.handleAddEvent();
    }

    /**
     *  Updates the master tag list to include tags in {@code event} that are not in the list.
     *  @return a copy of this {@code event} such that every tag in this event points to a Tag object in the master
     *  list.
     */
    private EpicEvent syncEventWithMasterTagList(EpicEvent event) {
        final UniqueTagList newEventTags = new UniqueTagList(event.getTags());
        final Map<Tag, Tag> masterTagObjects = updateMasterEventTagList(newEventTags);

        // Rebuild the list of event tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        newEventTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new EpicEvent(event.getName(), event.getUniqueAttendanceList(), correctTagReferences);
    }

    //@@author jiangyue12392
    /**
     * Removes {@code key} from this {@code EventPlanner}.
     * @throws EventNotFoundException if the {@code eventKey} is not in this {@code EventPlanner}.
     */
    public boolean removeEvent(EpicEvent eventKey) throws EventNotFoundException {
        if (events.remove(eventKey)) {
            eventKey.handleDeleteEvent();
            return true;
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Replaces the given event {@code targetEvent} in the list with {@code editedEvent}.
     * {@code EventPlanner}'s tag list will be updated with the tags of {@code editedEvent}.
     *
     * @throws DuplicateEventException if updating the event's details causes the event to be equivalent to
     *      another existing event in the list.
     * @throws EventNotFoundException if {@code targetEvent} could not be found in the list.
     *
     * @see #syncEventWithMasterTagList(EpicEvent)
     */
    public void updateEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedEvent);

        EpicEvent syncedEditedEvent = syncEventWithMasterTagList(editedEvent);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        events.setEvent(targetEvent, syncedEditedEvent);
    }


    //@@author bayweiheng
    /**
     * Registers a particular person to a particular event
     */
    public void registerPersonForEvent(Person person, EpicEvent event)
            throws EventNotFoundException, DuplicateAttendanceException {
        events.registerPersonForEvent(person, event);
    }

    /**
     * Deregisters a particular person from a particular event
     */
    public void deregisterPersonFromEvent(Person person, EpicEvent event)
            throws EventNotFoundException, PersonNotFoundInEventException {
        events.deregisterPersonFromEvent(person, event);
    }

    //@@author william6364
    /**
     * Toggles the attendance of a particular person in a particular event
     */
    public void toggleAttendance(Person person, EpicEvent event)
        throws EventNotFoundException, PersonNotFoundInEventException {
        events.toggleAttendance(person, event);
    }

    //// tag-level operations

    /**
     *  Updates the master person tag list to include tags in {@code objectTags} that are not in the list.
     *  @return a mapping of the Tags in the list Tag object in the master list.
     */
    private Map<Tag, Tag> updateMasterPersonTagList(UniqueTagList objectTags) {
        personTags.mergeFrom(objectTags);

        // Create map with values = tag object references in the master list
        // used for checking tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        personTags.forEach(tag -> masterTagObjects.put(tag, tag));

        return masterTagObjects;
    }


    /**
     *  Updates the master event tag list to include tags in {@code objectTags} that are not in the list.
     *  @return a mapping of the Tags in the list Tag object in the master list.
     */
    private Map<Tag, Tag> updateMasterEventTagList(UniqueTagList objectTags) {
        eventTags.mergeFrom(objectTags);

        // Create map with values = tag object references in the master list
        // used for checking tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        eventTags.forEach(tag -> masterTagObjects.put(tag, tag));

        return masterTagObjects;
    }

    public void addPersonTag(Tag t) throws UniqueTagList.DuplicateTagException {
        personTags.add(t);
    }

    public void addEventTag(Tag t) throws UniqueTagList.DuplicateTagException {
        eventTags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + personTags.asObservableList().size()
                +  " person tags, " + eventTags.asObservableList().size() + " event tags.";
    }

    //@@author

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asObservableList();
    }

    public UniquePersonList getPersonMaseterList() {
        return persons;
    }

    @Override
    public ObservableList<EpicEvent> getEventList() {
        return events.asObservableList();
    }

    @Override
    public ObservableList<Tag> getPersonTagList() {
        return personTags.asObservableList();
    }

    @Override
    public ObservableList<Tag> getEventTagList() {
        return eventTags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventPlanner // instanceof handles nulls
                && this.persons.equals(((EventPlanner) other).persons)
                && this.events.equals(((EventPlanner) other).events)
                && this.personTags.equalsOrderInsensitive(((EventPlanner) other).personTags)
                && this.eventTags.equalsOrderInsensitive(((EventPlanner) other).eventTags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, events, personTags, eventTags);
    }
}
