package seedu.address.commons.exceptions;

/**
 * Signals an error caused by accessing data that do not exist.
 */
public abstract class DataNotFoundException extends IllegalValueException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
