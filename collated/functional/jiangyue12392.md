# jiangyue12392
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java
/**
 * Deletes an event identified using it's last displayed index from the event planner.
 */
public class DeleteEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the last event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";

    private final Index targetIndex;

    private EpicEvent eventToDelete;

    public DeleteEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

```
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(eventToDelete);
        try {
            model.deleteEvent(eventToDelete);
            model.clearSelectedEpicEvent();
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<EpicEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        eventToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

```
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEventCommand) other).targetIndex) // state check
                && Objects.equals(this.eventToDelete, ((DeleteEventCommand) other).eventToDelete));
    }
}

```
###### \java\seedu\address\logic\commands\EditEventCommand.java
``` java
/**
 * Edits the details of an existing event in the address book.
 */
public class EditEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the event identified "
            + "by the index number used in the last event listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "School Event";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Event: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the address book.";

    private Index index;
    private EditEventDescriptor editEventDescriptor;

    private EpicEvent eventToEdit;
    private EpicEvent editedEvent;

    /**
     * @param index of the event in the filtered event list to edit
     * @param editEventDescriptor details to edit the event with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = new EditEventDescriptor(editEventDescriptor);
    }

    public EditEventCommand(EpicEvent eventToEdit, EpicEvent editedEvent) {
        this.eventToEdit = eventToEdit;
        this.editedEvent = editedEvent;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateEvent(eventToEdit, editedEvent);
        } catch (DuplicateEventException dee) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        }
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);

        model.visuallySelectEpicEvent(eventToEdit);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedEvent));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<EpicEvent> lastShownList = model.getFilteredEventList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        eventToEdit = lastShownList.get(index.getZeroBased());
        editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);
    }

```
###### \java\seedu\address\logic\commands\EditEventCommand.java
``` java

    /**
     * Creates and returns a {@code EpicEvent} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static EpicEvent createEditedEvent(EpicEvent eventToEdit, EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        Name updatedName = editEventDescriptor.getName().orElse(eventToEdit.getName());
        Set<Tag> updatedTags = editEventDescriptor.getTags().orElse(eventToEdit.getTags());

        return new EpicEvent(updatedName, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        // state check
        EditEventCommand e = (EditEventCommand) other;
        return index.equals(e.index)
                && editEventDescriptor.equals(e.editEventDescriptor)
                && Objects.equals(eventToEdit, e.eventToEdit);
    }

    /**
     * Stores the details to edit the event with. Each non-empty field value will replace the
     * corresponding field value of the event.
     */
    public static class EditEventDescriptor {
        private Name name;
        private Set<Tag> tags;

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditEventDescriptor(EditEventDescriptor toCopy) {
            setName(toCopy.name);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventDescriptor)) {
                return false;
            }

            // state check
            EditEventDescriptor e = (EditEventDescriptor) other;

            return getName().equals(e.getName())
                    && getTags().equals(e.getTags());
        }
    }
}
```
###### \java\seedu\address\logic\commands\FindEventCommand.java
``` java
/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindEventCommand extends Command {

    public static final String COMMAND_WORD = "find-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all events whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final EventNameContainsKeywordsPredicate predicate;

    public FindEventCommand(EventNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(predicate);
        model.clearSelectedEpicEvent();

        return new CommandResult(getMessageForEventListShownSummary(model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindEventCommand // instanceof handles nulls
                && this.predicate.equals(((FindEventCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\address\logic\parser\DeleteEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteEventCommand object
 */
public class DeleteEventCommandParser implements Parser<DeleteEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEventCommand
     * and returns an DeleteEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEventCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteEventCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\logic\parser\EditEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditEventCommand object
 */
public class EditEventCommandParser implements Parser<EditEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditEventCommand
     * and returns an EditEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE));
        }

        EditEventDescriptor editEventDescriptor = new EditEventDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editEventDescriptor::setName);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editEventDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editEventDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditEventCommand.MESSAGE_NOT_EDITED);
        }

        return new EditEventCommand(index, editEventDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
```
###### \java\seedu\address\logic\parser\FindEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindPersonCommand object
 */
public class FindEventCommandParser implements Parser<FindEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindPersonCommand
     * and returns an FindPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEventCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEventCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindEventCommand(new EventNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
```
###### \java\seedu\address\model\attendance\Attendance.java
``` java
    /**
     * Constructor for reconstruction of data from xmlfile
     * @param attendee
     * @param hasAttended
     */
    public Attendance(Person attendee, boolean hasAttended) {
        Objects.requireNonNull(attendee);
        this.attendee = attendee;
        this.event = null;
        this.hasAttendedEventProperty.set(hasAttended);
    }
```
###### \java\seedu\address\model\attendance\UniqueAttendanceList.java
``` java
    /**
     * Replaces the person in the attendance list with the given master person list if there is any person that
     * that is in both the master list and the attendance list
     */
    public void replace(Person toReplace, EpicEvent event) {
        requireAllNonNull(toReplace, event);

        for (int i = 0; i < internalList.size(); i++) {
            if (toReplace.equals(internalList.get(i).getPerson())) {
                Attendance currentAttendance = internalList.get(i);
                internalList.get(i).setAttendance(new Attendance(toReplace, event, currentAttendance.hasAttended()));
            }
        }

    }
```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    public EpicEvent(Name name, UniqueAttendanceList attendanceList, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.attendanceList = attendanceList;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    /** replace the person in the attendance list with the given person and the event in the attendance list*/
    public void replace(Person person) {
        attendanceList.replace(person, this);
    }

```
###### \java\seedu\address\model\event\EventNameContainsKeywordsPredicate.java
``` java

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class EventNameContainsKeywordsPredicate implements Predicate<EpicEvent> {
    private final List<String> keywords;

    public EventNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(EpicEvent event) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(event.getName().name, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventNameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((EventNameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\address\model\event\UniqueEpicEventList.java
``` java
    /**
     * Removes the equivalent event from the list.
     *
     * @throws EventNotFoundException if no such event could be found in the list.
     */
    public boolean remove(EpicEvent eventToRemove) throws EventNotFoundException {
        requireNonNull(eventToRemove);
        final boolean eventFoundAndDeleted = internalList.remove(eventToRemove);
        if (!eventFoundAndDeleted) {
            throw new EventNotFoundException();
        }
        return eventFoundAndDeleted;
    }

```
###### \java\seedu\address\model\event\UniqueEpicEventList.java
``` java
    /**
     * Replaces the event {@code targetEvent} in the list with {@code editedEvent}.
     *
     * @throws DuplicateEventException if the replacement is equivalent to another existing event in the list.
     * @throws EventNotFoundException if {@code targetEvent} could not be found in the list.
     */
    public void setEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedEvent);

        int index = internalList.indexOf(targetEvent);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        if (!targetEvent.equals(editedEvent) && internalList.contains(editedEvent)) {
            throw new DuplicateEventException();
        }

        internalList.get(index).setEvent(editedEvent);

        // Forces UI to refresh
        internalList.set(index, internalList.get(index));
    }

    public void setEvents(UniqueEpicEventList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setEvents(List<EpicEvent> events) throws DuplicateEventException {
        requireAllNonNull(events);
        final UniqueEpicEventList replacement = new UniqueEpicEventList();
        for (final EpicEvent event : events) {
            replacement.add(event);
        }
        setEvents(replacement);
    }

```
###### \java\seedu\address\model\EventPlanner.java
``` java
    /**
     * Removes {@code key} from this {@code EventPlanner}.
     * @throws EventNotFoundException if the {@code eventKey} is not in this {@code EventPlanner}.
     */
    public boolean removeEvent(EpicEvent eventKey) throws EventNotFoundException {
        if (events.remove(eventKey)) {
            eventKey.handleDeleteEvent();
            return true;
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Replaces the given event {@code targetEvent} in the list with {@code editedEvent}.
     * {@code EventPlanner}'s tag list will be updated with the tags of {@code editedEvent}.
     *
     * @throws DuplicateEventException if updating the event's details causes the event to be equivalent to
     *      another existing event in the list.
     * @throws EventNotFoundException if {@code targetEvent} could not be found in the list.
     *
     * @see #syncEventWithMasterTagList(EpicEvent)
     */
    public void updateEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedEvent);

        EpicEvent syncedEditedEvent = syncEventWithMasterTagList(editedEvent);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        events.setEvent(targetEvent, syncedEditedEvent);
    }


```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteEvent(EpicEvent targetEvent) throws EventNotFoundException {
        eventPlanner.removeEvent(targetEvent);
        indicateEventPlannerChanged();
    }

```
###### \java\seedu\address\model\person\exceptions\PersonNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified person.
 */
public class PersonNotFoundException extends DataNotFoundException {
    public PersonNotFoundException() {
        super("Operation requires person that do not exist");
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedAttendance.java
``` java
/**
 * JAXB-friendly version of the Attendance.
 */
public class XmlAdaptedAttendance {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Attendance's %s field is missing!";

    @XmlElement(required = true)
    private XmlAdaptedPerson attendee;

    @XmlElement
    private Boolean hasAttended;

    /**
     * Constructs an XmlAdaptedAttendance.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAttendance() {}

    /**
     * Constructs an {@code XmlAdaptedAttendance} with the given Attendance details.
     */
    public XmlAdaptedAttendance(XmlAdaptedPerson attendee, Boolean hasAttended) {
        this.attendee = attendee;
        this.hasAttended = hasAttended;
    }

    /**
     * Converts a given Attendance into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAttendance
     */
    public XmlAdaptedAttendance(Attendance source) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(source.getPerson());
        attendee = new XmlAdaptedPerson(source.getPerson());
        hasAttended = source.hasAttended();
    }

    /**
     * Converts this jaxb-friendly adapted Attendance object into the model's Attendance object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Attendance
     */
    public Attendance toModelType() throws IllegalValueException {
        if (this.attendee == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Person.class.getSimpleName()));
        }
        if (this.hasAttended == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Attendance Status"));
        }
        final Person attendee = this.attendee.toModelType();

        final boolean hasAttended = this.hasAttended;

        return new Attendance(attendee, hasAttended);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAttendance)) {
            return false;
        }

        XmlAdaptedAttendance otherAttendance = (XmlAdaptedAttendance) other;
        return Objects.equals(attendee, otherAttendance.attendee)
                && hasAttended == otherAttendance.hasAttended;
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedEpicEvent.java
``` java
/**
 * JAXB-friendly version of the EpicEvent.
 */
public class XmlAdaptedEpicEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "EpicEvent's %s field is missing!";

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private List<XmlAdaptedAttendance> attendanceList = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedEpicEvent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEpicEvent() {}

    /**
     * Constructs an {@code XmlAdaptedEpicEvent} with the given epicEvent details.
     */
    public XmlAdaptedEpicEvent(String name, List<XmlAdaptedAttendance> attendanceList, List<XmlAdaptedTag> tagged) {
        this.name = name;

        if (attendanceList != null) {
            this.attendanceList = new ArrayList<>(attendanceList);
        }

        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given EpicEvent into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEpicEvent
     */
    public XmlAdaptedEpicEvent(EpicEvent source) {
        name = source.getName().name;

        attendanceList = new ArrayList<>();
        for (Attendance attendance : source.getAttendanceList()) {
            attendanceList.add(new XmlAdaptedAttendance(attendance));
        }

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted EpicEvent object into the model's EpicEvent object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted epicEvent
     */
    public EpicEvent toModelType() throws IllegalValueException {
        final List<Tag> eventTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            eventTags.add(tag.toModelType());
        }

        final UniqueAttendanceList attendances = new UniqueAttendanceList();
        for (XmlAdaptedAttendance attendance : attendanceList) {
            attendances.add(attendance.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        final Set<Tag> tags = new HashSet<>(eventTags);
        return new EpicEvent(name, attendances, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedEpicEvent)) {
            return false;
        }

        XmlAdaptedEpicEvent otherEpicEvent = (XmlAdaptedEpicEvent) other;
        return Objects.equals(name, otherEpicEvent.name)
                && attendanceList.equals(otherEpicEvent.attendanceList)
                && tagged.equals(otherEpicEvent.tagged);
    }
}
```
###### \java\seedu\address\storage\XmlSerializableEventPlanner.java
``` java
    /**
     * Finds and replaces the dummy person in attendance list with the person object in the master list
     */
    void setPersonForAttendance(UniquePersonList persons, EpicEvent event) {
        for (Person p : persons) {
            event.replace(p);
        }
    }
```
