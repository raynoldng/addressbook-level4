package seedu.address.model.event.exceptions;
import seedu.address.commons.exceptions.DataNotFoundException;

//@@author bayweiheng
/**
 * Signals that the operation is unable to find the specified event.
 */
public class EventNotFoundException extends DataNotFoundException {
    public EventNotFoundException() {
        super("Operation requires event that do not exist");
    }
}
