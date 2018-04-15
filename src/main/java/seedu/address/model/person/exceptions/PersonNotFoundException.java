package seedu.address.model.person.exceptions;

import seedu.address.commons.exceptions.DataNotFoundException;

//@@author jiangyue12392
/**
 * Signals that the operation is unable to find the specified person.
 */
public class PersonNotFoundException extends DataNotFoundException {
    public PersonNotFoundException() {
        super("Operation requires person that do not exist");
    }
}
