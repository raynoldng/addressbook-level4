package seedu.address.model.attendance;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

//@@author raynoldng
/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class AttendanceNameContainsKeywordsPredicate implements Predicate<Attendance> {
    private final List<String> keywords;

    public AttendanceNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Attendance attendee) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(attendee.getPerson().getFullName().name,
                        keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AttendanceNameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((AttendanceNameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
