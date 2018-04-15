package seedu.address.model.event.exceptions;
import seedu.address.commons.exceptions.DataNotFoundException;

//@@author bayweiheng
/**
 * Signals that the operation is unable to find the specified event.
 */
public class PersonNotFoundInEventException extends DataNotFoundException {
    public PersonNotFoundInEventException() {
        super("Operation requires person that does not exist in event");
    }
}
