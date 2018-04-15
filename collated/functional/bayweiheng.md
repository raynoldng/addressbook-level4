# bayweiheng
###### \java\seedu\address\logic\commands\AddEventCommand.java
``` java

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new DeleteEventCommand(toAdd);
    }

```
###### \java\seedu\address\logic\commands\AddPersonCommand.java
``` java

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new DeletePersonCommand(toAdd);
    }

```
###### \java\seedu\address\logic\commands\ClearCommand.java
``` java

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new RestoreCommand(new EventPlanner(model.getEventPlanner()));
    }
```
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java
    /**
     * Used for generating the oppositeCommand of an AddEventCommand
     */
    public DeleteEventCommand(EpicEvent eventToDelete) {
        this.eventToDelete = eventToDelete;
        this.targetIndex = null;
    }
```
###### \java\seedu\address\logic\commands\DeleteEventCommand.java
``` java
    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new AddEventCommand(eventToDelete);
    }
```
###### \java\seedu\address\logic\commands\DeletePersonCommand.java
``` java
    public static final String MESSAGE_STILL_REGISTERED = "This person is still registered for an event!"
            + " Please deregister the person from all events first";
```
###### \java\seedu\address\logic\commands\DeletePersonCommand.java
``` java
    /**
     * Used for generating the oppositeCommand of an AddCommand
     */
    public DeletePersonCommand(Person personToDelete) {
        this.personToDelete = personToDelete;
        this.targetIndex = null;
    }
```
###### \java\seedu\address\logic\commands\DeletePersonCommand.java
``` java
    /**
     * Finds the person to delete from the supplied index.
     * If the person is still registered for an event, he/she is not allowed to be deleted,
     * and an exception will be thrown.
     */
    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToDelete = lastShownList.get(targetIndex.getZeroBased());
        if (personToDelete.getNumberOfEventsRegisteredFor() > 0) {
            throw new CommandException(MESSAGE_STILL_REGISTERED);
        }
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new AddPersonCommand(personToDelete);
    }
```
###### \java\seedu\address\logic\commands\DeregisterPersonCommand.java
``` java

/**
 * Registers a person to an event.
 */
public class DeregisterPersonCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deregister";

    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";
    public static final String MESSAGE_PERSON_NOT_IN_EVENT = "This person was not registered for the event";
    public static final String MESSAGE_SUCCESS = "Deregistered person %1$s from event %2$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deregisters the person identified by the index number used in the last person listing"
            + " from a particular event.\n"
            + "Parameters: INDEX (must be a positive integer), EVENT_NAME (must match an event's name"
            + " in EPIC exactly)\n"
            + "Example: " + COMMAND_WORD + " 1" + " AY201718 Graduation";

    private Index targetIndex;
    private String eventName;

    private Person personToDeregister;
    private EpicEvent eventToDeregisterFor;

    /**
     * Creates an RegisterPersonCommand to register the Person at targetIndex in the last person
     * listing for the EpicEvent with name eventName
     */
    public DeregisterPersonCommand(Index targetIndex, String eventName) {
        requireAllNonNull(targetIndex, eventName);
        this.targetIndex = targetIndex;
        this.eventName = eventName;
    }

    public DeregisterPersonCommand(Person personToDeregister, EpicEvent eventToDeregisterFor) {
        this.personToDeregister = personToDeregister;
        this.eventToDeregisterFor = eventToDeregisterFor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(personToDeregister, eventToDeregisterFor);
        try {
            model.deregisterPersonFromEvent(personToDeregister, eventToDeregisterFor);
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        } catch (PersonNotFoundInEventException dpe) {
            throw new CommandException(MESSAGE_PERSON_NOT_IN_EVENT);
        }

        model.visuallySelectEpicEvent(eventToDeregisterFor);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToDeregister, eventName));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToDeregister = lastShownPersonList.get(targetIndex.getZeroBased());

        List<EpicEvent> events = model.getEventList();

        List<EpicEvent> matchedEvents = events.stream()
                .filter(e -> e.getName().toString().equals(eventName))
                .collect(Collectors.toList());

        if (matchedEvents.isEmpty()) {
            throw new CommandException(MESSAGE_EVENT_NOT_FOUND);
        }

        eventToDeregisterFor = matchedEvents.get(0);
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new RegisterPersonCommand(personToDeregister, eventToDeregisterFor);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeregisterPersonCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeregisterPersonCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDeregister, ((DeregisterPersonCommand) other).personToDeregister)
                && this.eventName.equals(((DeregisterPersonCommand) other).eventName)
                && Objects.equals(this.eventToDeregisterFor, ((DeregisterPersonCommand) other).eventToDeregisterFor));
    }
}
```
###### \java\seedu\address\logic\commands\EditEventCommand.java
``` java
    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new EditEventCommand(eventToEdit, new EpicEvent(eventToEdit));
    }
```
###### \java\seedu\address\logic\commands\EditPersonCommand.java
``` java
    /**
     * Used for generating the oppositeCommand of an EditPersonCommand
     */
    public EditPersonCommand(Person personToEdit, Person editedPerson) {
        this.personToEdit = personToEdit;
        this.editedPerson = editedPerson;
    }
```
###### \java\seedu\address\logic\commands\EditPersonCommand.java
``` java
    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new EditPersonCommand(editedPerson, new Person(personToEdit));
    }
```
###### \java\seedu\address\logic\commands\ListRegisteredPersonsCommand.java
``` java

/** Lists all registered persons in a given event, regardless of whether their attendance is marked */
public class ListRegisteredPersonsCommand extends Command {

    public static final String COMMAND_WORD = "list-registered";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the persons registered for the specified event on the Persons Pane.\n"
            + "Parameters: EVENT_NAME (must match an event's name in EPIC exactly)\n"
            + "Example: " + COMMAND_WORD + " IoT Seminar";

    public static final String MESSAGE_SUCCESS = "Listed all persons in %1$s";
    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";

    private final String eventName;
    private EpicEvent eventToListRegisteredPersonsFor;

    public ListRegisteredPersonsCommand(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<EpicEvent> events = model.getEventList();

        List<EpicEvent> matchedEvents = events.stream()
                .filter(e -> e.getName().toString().equals(eventName))
                .collect(Collectors.toList());

        if (matchedEvents.isEmpty()) {
            throw new CommandException(MESSAGE_EVENT_NOT_FOUND);
        }

        eventToListRegisteredPersonsFor = matchedEvents.get(0);

        Predicate<Person> isInEvent = person ->
            eventToListRegisteredPersonsFor.hasPerson(person);

        model.updateFilteredPersonList(isInEvent);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }
}
```
###### \java\seedu\address\logic\commands\RegisterPersonCommand.java
``` java

/**
 * Registers a person to an event.
 */
public class RegisterPersonCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "register";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Registers the person identified by the index number used in the last person listing"
            + " to a particular event.\n"
            + "Parameters: INDEX (must be a positive integer), EVENT_NAME (must match an event's name"
            + " in EPIC exactly)\n"
            + "Example: " + COMMAND_WORD + " 1" + " AY201718 Graduation";

    public static final String MESSAGE_SUCCESS = "Registered person %1$s for event %2$s";
    public static final String MESSAGE_EVENT_NOT_FOUND = "The event specified cannot be found";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person is already registered for the event";

    private Index targetIndex;
    private String eventName;

    private Person personToRegister;
    private EpicEvent eventToRegisterFor;

    /**
     * Creates an RegisterPersonCommand to register the Person at targetIndex in the last person
     * listing for the EpicEvent with name eventName
     */
    public RegisterPersonCommand(Index targetIndex, String eventName) {
        requireAllNonNull(targetIndex, eventName);
        this.targetIndex = targetIndex;
        this.eventName = eventName;
    }

    public RegisterPersonCommand(Person personToRegister, EpicEvent eventToRegisterFor) {
        this.personToRegister = personToRegister;
        this.eventToRegisterFor = eventToRegisterFor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(personToRegister, eventToRegisterFor);
        try {
            model.registerPersonForEvent(personToRegister, eventToRegisterFor);
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        } catch (DuplicateAttendanceException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.visuallySelectEpicEvent(eventToRegisterFor);

        return new CommandResult(String.format(MESSAGE_SUCCESS, personToRegister, eventName));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToRegister = lastShownPersonList.get(targetIndex.getZeroBased());

        List<EpicEvent> events = model.getEventList();

        List<EpicEvent> matchedEvents = events.stream()
                .filter(e -> e.getName().toString().equals(eventName))
                .collect(Collectors.toList());

        if (matchedEvents.isEmpty()) {
            throw new CommandException(MESSAGE_EVENT_NOT_FOUND);
        }

        eventToRegisterFor = matchedEvents.get(0);
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new DeregisterPersonCommand(personToRegister, eventToRegisterFor);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RegisterPersonCommand // instanceof handles nulls
                && this.targetIndex.equals(((RegisterPersonCommand) other).targetIndex) // state check
                && Objects.equals(this.personToRegister, ((RegisterPersonCommand) other).personToRegister)
                && this.eventName.equals(((RegisterPersonCommand) other).eventName)
                && Objects.equals(this.eventToRegisterFor, ((RegisterPersonCommand) other).eventToRegisterFor));
    }
}
```
###### \java\seedu\address\logic\commands\RestoreCommand.java
``` java

/** Restores the event planner to its previous state.
 * Used only to undo a ClearCommand.
 */
public class RestoreCommand extends UndoableCommand {

    public static final String MESSAGE_SUCCESS = "Event planner has been restored!";

    private ReadOnlyEventPlanner previousEventPlanner;

    public RestoreCommand(ReadOnlyEventPlanner previousEventPlanner) {
        this.previousEventPlanner = previousEventPlanner;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.resetData(previousEventPlanner);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    protected void generateOppositeCommand() {
        oppositeCommand = new ClearCommand();
    }

}
```
###### \java\seedu\address\logic\commands\UndoableCommand.java
``` java
    /**
     * Represents the Command to be executed to revert the changes made by the UndoableCommand.
     * We mandate that the oppositeCommand of an UndoableCommand be undoable as well.
     */
    protected UndoableCommand oppositeCommand;
```
###### \java\seedu\address\logic\commands\UndoableCommand.java
``` java
    /**
     * Undoes the intention of the previous command, and updates the person panel
     * to show all persons. The relative ordering of Persons/EpicEvents may be altered after undoing of
     * a delete command.
     */
    protected final void undo() {
        requireAllNonNull(model);
        try {
            oppositeCommand.executeUndoableCommand();
        } catch (CommandException ce) {
            throw new AssertionError("This command should not fail.");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    protected abstract void generateOppositeCommand();

    /**
     * Executes the command and updates person panel to show all persons.
     */
    protected final void redo() {
        requireNonNull(model);
        try {
            executeUndoableCommand();
        } catch (CommandException ce) {
            throw new AssertionError("The command has been successfully executed previously; "
                    + "it should not fail now.");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public final CommandResult execute() throws CommandException {
        preprocessUndoableCommand();
        generateOppositeCommand();
        oppositeCommand.setData(model, history, undoRedoStack);
        return executeUndoableCommand();
    }
```
###### \java\seedu\address\logic\parser\DeregisterPersonCommandParser.java
``` java

/**
 * Parses input arguments and creates a new DeregisterPersonCommand object
 */
public class DeregisterPersonCommandParser implements Parser<DeregisterPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeregisterPersonCommand
     * and returns an DeregisterPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeregisterPersonCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] indexAndEventName = trimmedArgs.split("\\s+", 2);

        if (indexAndEventName.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeregisterPersonCommand.MESSAGE_USAGE));
        }
        String eventName = indexAndEventName[1];

        try {
            Index index = ParserUtil.parseIndex(indexAndEventName[0]);
            return new DeregisterPersonCommand(index, eventName);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeregisterPersonCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\logic\parser\ListRegisteredPersonsCommandParser.java
``` java

/**
 * Parses input arguments and creates a new ListRegisteredPersonsCommand object
 */
public class ListRegisteredPersonsCommandParser implements Parser<ListRegisteredPersonsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListRegisteredPersonsCommand
     * and returns an ListRegisteredPersonsCommand object for execution.
     */
    public ListRegisteredPersonsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListRegisteredPersonsCommand.MESSAGE_USAGE));
        }

        return new ListRegisteredPersonsCommand(trimmedArgs);
    }

}
```
###### \java\seedu\address\logic\parser\RegisterPersonCommandParser.java
``` java

/**
 * Parses input arguments and creates a new RegisterPersonCommand object
 */
public class RegisterPersonCommandParser implements Parser<RegisterPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RegisterPersonCommand
     * and returns an RegisterPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RegisterPersonCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] indexAndEventName = trimmedArgs.split("\\s+", 2);

        if (indexAndEventName.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RegisterPersonCommand.MESSAGE_USAGE));
        }
        String eventName = indexAndEventName[1];

        try {
            Index index = ParserUtil.parseIndex(indexAndEventName[0]);
            return new RegisterPersonCommand(index, eventName);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RegisterPersonCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    public EpicEvent(EpicEvent toBeCopied) {
        requireNonNull(toBeCopied);
        this.name = new Name(toBeCopied.getName().toString());
        this.tags = new UniqueTagList(toBeCopied.getTags());
        this.attendanceList = new UniqueAttendanceList();
    }

```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    /**
     * Edits this event by transferring the name and tags of the dummyEvent over
     */
    public void setEvent(EpicEvent dummyEvent) {
        this.name = dummyEvent.getName();
        this.tags = new UniqueTagList(dummyEvent.getTags());
        for (Attendance attendance : this.attendanceList.asObservableList()) {
            attendance.setAttendance(new Attendance(attendance.getPerson(), this, attendance.hasAttended()));
        }
    }

    /** registers person for this event */
    public void registerPerson(Person person) throws DuplicateAttendanceException {
        attendanceList.add(person, this);
    }

    /** deregisters person from this event */
    public void deregisterPerson(Person person) throws PersonNotFoundInEventException {
        try {
            attendanceList.remove(person, this);
        } catch (PersonNotFoundInEventException e) {
            throw new PersonNotFoundInEventException();
        }
    }

```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    /**
     * Decrements all this event's attendanceList' numberOfEventsRegisteredFor.
     * Called only when this event is being deleted
     */
    public void handleDeleteEvent() {
        attendanceList.handleDeleteEvent();
    }


    /**
     * Increments all this event's attendanceList' numberOfEventsRegisteredFor.
     * Called only when this event is being added.
     * Required to properly maintain numberOfPersonsRegisteredFor for these persons
     * when an undo of a delete operation is called
     */
    public void handleAddEvent() {
        attendanceList.handleAddEvent();
    }

    /**
     * Sets the attendance list using another event's. Used for undoing deleteEvent
     */
    public void setAttendanceList(List<Attendance> dummyRegisteredPersons) {
        try {
            attendanceList.setAttendanceList(dummyRegisteredPersons);
        } catch (DuplicateAttendanceException e) {
            throw new AssertionError("this should not happen, dummyRegisteredPersons"
                    + "is a valid Attendance List from another event");
        }
    }

```
###### \java\seedu\address\model\event\EpicEvent.java
``` java
    /** returns true if person is in this event, regardless of whether
     * his attendance has been marked
     */
    public boolean hasPerson(Person person) {
        return attendanceList.contains(person, this);
    }

```
###### \java\seedu\address\model\event\exceptions\EventNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified event.
 */
public class EventNotFoundException extends DataNotFoundException {
    public EventNotFoundException() {
        super("Operation requires event that do not exist");
    }
}
```
###### \java\seedu\address\model\event\exceptions\PersonNotFoundInEventException.java
``` java
/**
 * Signals that the operation is unable to find the specified event.
 */
public class PersonNotFoundInEventException extends DataNotFoundException {
    public PersonNotFoundInEventException() {
        super("Operation requires person that does not exist in event");
    }
}
```
###### \java\seedu\address\model\event\UniqueEpicEventList.java
``` java
    /**
     * Registers the person to the event. Updates the person's numberOfEventsRegisteredFor
     * upon success.
     *
     * @throws DuplicateAttendanceException if the person is already registered
     * @throws EventNotFoundException if no such event could be found in the list
     */
    public void registerPersonForEvent(Person person, EpicEvent eventToRegisterFor)
            throws DuplicateAttendanceException, EventNotFoundException {
        requireAllNonNull(person, eventToRegisterFor);

        int index = internalList.indexOf(eventToRegisterFor);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        eventToRegisterFor.registerPerson(person);
        person.incrementNumberOfEventsRegisteredFor();
    }

    /**
     * Deregisters the person to the event. Updates the person's numberOfEventsRegisteredFor
     * upon success.
     *
     * @throws PersonNotFoundInEventException if person could not be found in event
     * @throws EventNotFoundException if no such event could be found in the list
     */
    public void deregisterPersonFromEvent(Person person, EpicEvent eventToRegisterFor)
            throws PersonNotFoundInEventException, EventNotFoundException {
        requireAllNonNull(person, eventToRegisterFor);

        int index = internalList.indexOf(eventToRegisterFor);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        eventToRegisterFor.deregisterPerson(person);
        person.decrementNumberOfEventsRegisteredFor();
    }

```
###### \java\seedu\address\model\EventPlanner.java
``` java
    /**
     * Registers a particular person to a particular event
     */
    public void registerPersonForEvent(Person person, EpicEvent event)
            throws EventNotFoundException, DuplicateAttendanceException {
        events.registerPersonForEvent(person, event);
    }

    /**
     * Deregisters a particular person from a particular event
     */
    public void deregisterPersonFromEvent(Person person, EpicEvent event)
            throws EventNotFoundException, PersonNotFoundInEventException {
        events.deregisterPersonFromEvent(person, event);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void updateEvent(EpicEvent targetEvent, EpicEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireAllNonNull(targetEvent, editedEvent);

        eventPlanner.updateEvent(targetEvent, editedEvent);
        indicateEventPlannerChanged();
    }

    //=========== Event-Person Interactions ==================================================================

    @Override
    public void registerPersonForEvent(Person person, EpicEvent event)
            throws EventNotFoundException, DuplicateAttendanceException {
        requireAllNonNull(person, event);

        eventPlanner.registerPersonForEvent(person, event);
        indicateEventPlannerChanged();
    }

    @Override
    public void deregisterPersonFromEvent(Person person, EpicEvent event)
            throws EventNotFoundException, PersonNotFoundInEventException {
        requireAllNonNull(person, event);

        eventPlanner.deregisterPersonFromEvent(person, event);
        indicateEventPlannerChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * Returns an unmodifiable view of the list of {@code EpicEvent} backed by the internal list of
     * {@code eventPlanner}
     */
    @Override
    public ObservableList<EpicEvent> getEventList() {
        return FXCollections.unmodifiableObservableList(eventPlanner.getEventList());
    }

```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Edits this person by transferring the fields of dummyPerson over.
     * Used for mutable edit command
     */
    public void setPerson(Person dummyPerson) {
        this.name = dummyPerson.getFullName();
        this.phone = dummyPerson.getPhone();
        this.email = dummyPerson.getEmail();
        this.address = dummyPerson.getAddress();
        this.tags = new UniqueTagList(dummyPerson.getTags());
        fireValueChangedEvent();
    }
```
