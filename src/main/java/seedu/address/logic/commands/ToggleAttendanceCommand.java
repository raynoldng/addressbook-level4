package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonNotFoundInEventException;

//@@author william6364
/**
 * Marks attendance of a participant for an event.
 */
public class ToggleAttendanceCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "toggle";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Toggles the attendance of the person identified by the index number used in the last attendee listing"
            + " for a particular event.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Toggled attendance of person %1$s for event %2$s";

    private Index targetIndex;
    private Attendance attendanceToToggle;

    public ToggleAttendanceCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    public ToggleAttendanceCommand(Attendance attendanceToToggle) {
        this.attendanceToToggle = attendanceToToggle;
    }

    public Attendance getAttendanceToToggle() {
        return attendanceToToggle;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(attendanceToToggle);

        try {
            model.toggleAttendance(attendanceToToggle.getPerson(), attendanceToToggle.getEvent());
        } catch (EventNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_EVENT_NOT_FOUND);
        } catch (PersonNotFoundInEventException e) {
            throw new CommandException(Messages.MESSAGE_PERSON_NOT_IN_EVENT);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, attendanceToToggle.getPerson().getFullName(),
                attendanceToToggle.getEvent().getName()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Attendance> lastShownList = model.getSelectedEpicEvent().getFilteredAttendees();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ATTENDANCE_DISPLAYED_INDEX);
        }

        attendanceToToggle = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new ToggleAttendanceCommand(attendanceToToggle);
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ToggleAttendanceCommand // instanceof handles nulls
                && this.targetIndex.equals(((ToggleAttendanceCommand) other).targetIndex) // state check
                && Objects.equals(this.attendanceToToggle, ((ToggleAttendanceCommand) other).attendanceToToggle));
    }
}
