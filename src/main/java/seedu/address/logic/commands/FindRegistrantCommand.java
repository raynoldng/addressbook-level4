package seedu.address.logic.commands;

import seedu.address.model.attendance.AttendanceNameContainsKeywordsPredicate;

//@@author raynoldng
/**
 * Finds and lists all persons in event planner whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindRegistrantCommand extends Command {

    public static final String COMMAND_WORD = "find-registrant";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) in the selected event and displays them as a list with index"
            + " numbers.\nParameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final AttendanceNameContainsKeywordsPredicate predicate;

    public FindRegistrantCommand(AttendanceNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredAttendanceList(predicate);
        int numFound = model.getSelectedEpicEvent().getFilteredAttendees().size();
        return new CommandResult(getMessageForPersonListShownSummary(numFound));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindRegistrantCommand // instanceof handles nulls
                && this.predicate.equals(((FindRegistrantCommand) other).predicate)); // state check
    }
}
