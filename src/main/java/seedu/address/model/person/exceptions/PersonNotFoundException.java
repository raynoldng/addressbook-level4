package seedu.address.model.person.exceptions;

/**
 * Signals that the operation is unable to find the specified person.
 */
public class PersonNotFoundException extends Exception {
    public PersonNotFoundException() {
        super("Operation requires person that do not exist");
    }
}
