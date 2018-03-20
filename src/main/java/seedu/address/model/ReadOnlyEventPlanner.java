package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyEventPlanner {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the events list.
     * This list will not contain any duplicate events.
     */
    ObservableList<EpicEvent> getEventList();

    /**
     * Returns an unmodifiable view of the person tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getPersonTagList();

    /**
     * Returns an unmodifiable view of the event tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getEventTagList();

}
