package seedu.address.model.event;

import static java.util.Objects.requireNonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateEventException;

public class UniqueEventList {

    private final ObservableList<EpicEvent> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(EpicEvent toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an event to the list.
     *
     * @throws DuplicateEventException if the event to add is a duplicate of an existing event in the list.
     */
    public void add(EpicEvent toAdd) throws DuplicateEventException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(toAdd);
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEventList // instanceof handles nulls
                && this.internalList.equals(((UniqueEventList) other).internalList));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<EpicEvent> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }
    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
