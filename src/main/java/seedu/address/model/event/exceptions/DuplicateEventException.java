package seedu.address.model.event.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

//@@author william6364
/**
 * Signals that the operation will result in duplicate EpicEvent objects.
 */
public class DuplicateEventException extends DuplicateDataException {
    public DuplicateEventException() {
        super("Operation would result in duplicate events");
    }
}
