package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UniqueEventList {

    private final ObservableList<EpicEvent> internalList = FXCollections.observableArrayList();

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
