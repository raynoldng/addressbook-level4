package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.event.EpicEvent;
import seedu.address.model.person.Person;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Person> PREDICATE_MATCHING_NO_PERSONS = unused -> false;
    private static final Predicate<EpicEvent> PREDICATE_MATCHING_NO_EPIC_EVENT = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model, List<Person> toDisplay) {
        Optional<Predicate<Person>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredPersonList(predicate.orElse(PREDICATE_MATCHING_NO_PERSONS));
    }

    public static void setEpicEventFilteredList(Model model, List<EpicEvent> toDisplay) {
        Optional<Predicate<EpicEvent>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredEventList(predicate.orElse(PREDICATE_MATCHING_NO_EPIC_EVENT));
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Person... toDisplay) {
        setFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Person} equals to {@code other}.
     */
    private static Predicate<Person> getPredicateMatching(Person other) {
        return person -> person.equals(other);
    }

    /**
     * Returns a predicate that evaluates to true if this {@code EpicEvent} equals to {@code other}.
     */
    private static Predicate<EpicEvent> getPredicateMatching(EpicEvent other) {
        return event-> event.equals(other);
    }

}
