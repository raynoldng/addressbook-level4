# raynoldng
###### /java/seedu/address/ui/EpicEventCardTest.java
``` java
public class EpicEventCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        EpicEvent eventWithoutTags = new EpicEventBuilder().withTags(new String[0]).build();
        EpicEventCard eventCard = new EpicEventCard(eventWithoutTags, 1);
        uiPartRule.setUiPart(eventCard);
        assertEpicEventCardDisplay(eventCard, eventWithoutTags, 1);

        // with tags
        EpicEvent eventWithTags = new EpicEventBuilder().build();
        eventCard = new EpicEventCard(eventWithTags, 2);
        uiPartRule.setUiPart(eventCard);
        assertEpicEventCardDisplay(eventCard, eventWithTags, 2);
    }

    @Test
    public void equals() {
        EpicEvent event = new EpicEventBuilder().build();
        EpicEventCard eventCard = new EpicEventCard(event, 0);

        // same event, same index -> return true
        EpicEventCard copy = new EpicEventCard(event, 0);
        assertTrue(eventCard.equals(eventCard));

        // same object -> returns true
        assertTrue(eventCard.equals(eventCard));

        // null -> returns false
        assertFalse(eventCard.equals(null));

        // different types -> return false
        assertFalse(eventCard.equals(0));

        // different card, same index -> returns false
        EpicEvent differentEvent = new EpicEventBuilder().withName("differentEvent").build();
        assertFalse(eventCard.equals(new EpicEventCard(differentEvent, 0)));

        // same event, different index -> returns false
        assertFalse(eventCard.equals(new EpicEventCard(event, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertEpicEventCardDisplay(EpicEventCard eventCard, EpicEvent expectedEvent, int expectedId) {
        guiRobot.pauseForHuman();

        EpicEventCardHandle epicCardHandle = new EpicEventCardHandle(eventCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", epicCardHandle.getId());

        // verify person details are displayed correctly
        assertEpicEventCardDisplaysEpicEvent(expectedEvent, epicCardHandle);
    }
}
```
###### /java/seedu/address/logic/parser/SelectEventCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;

import org.junit.Test;

import seedu.address.logic.commands.SelectEventCommand;

/**
 * Test scope: similar to {@code DeletePersonCommandParserTest}.
 * @see DeletePersonCommandParserTest
 */
public class SelectEventCommandParserTest {

    private SelectEventCommandParser parser = new SelectEventCommandParser();

    @Test
    public void parse_validArgs_returnsSelectEventCommand() {
        assertParseSuccess(parser, "1", new SelectEventCommand(INDEX_FIRST_EVENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectEventCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/commands/SelectEventCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static seedu.address.logic.commands.CommandTestUtil.showEventAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_EVENT;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToEventListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;


/**
 * Contains integration tests (interaction with the Model) for {@code SelectEventCommand}.
 */
public class SelectEventCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastEpicEventIndex = Index.fromOneBased(model.getFilteredEventList().size());
        assertExecutionSuccess(INDEX_FIRST_EVENT);
        assertExecutionSuccess(INDEX_THIRD_EVENT);

        assertExecutionSuccess(lastEpicEventIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        assertExecutionSuccess(INDEX_FIRST_EVENT);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        Index outOfBoundsIndex = INDEX_SECOND_EVENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getEventPlanner().getEventList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectEventCommand selectFirstEventCommand = new SelectEventCommand(INDEX_FIRST_EVENT);
        SelectEventCommand selectSecondEventCommand = new SelectEventCommand(INDEX_SECOND_EVENT);

        // same object -> returns true
        assertTrue(selectFirstEventCommand.equals(selectFirstEventCommand));

        // same values -> returns true
        SelectEventCommand selectFirstEventCommandCopy = new SelectEventCommand(INDEX_FIRST_EVENT);
        assertTrue(selectFirstEventCommand.equals(selectFirstEventCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstEventCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstEventCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstEventCommand.equals(selectSecondEventCommand));
    }

    /**
     * Executes a {@code SelectEventCommand} with the given {@code index}, and checks that
     * {@code JumpToListRequestEvent} is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectEventCommand selectEventCommand = prepareCommand(index);

        try {
            CommandResult commandResult = selectEventCommand.execute();
            assertEquals(String.format(SelectEventCommand.MESSAGE_SELECT_EVENT_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToEventListRequestEvent lastEvent = (JumpToEventListRequestEvent)
                eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }
    /**
     * Executes a {@code SelectEventCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectEventCommand selectEventCommand = prepareCommand(index);

        try {
            selectEventCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectEventCommand} with parameters {@code index}.
     */
    private SelectEventCommand prepareCommand(Index index) {
        SelectEventCommand selectEventCommand = new SelectEventCommand(index);
        selectEventCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectEventCommand;
    }
}
```
###### /java/seedu/address/testutil/TypicalPersons.java
``` java
    /**
     * Returns an {@code EventPlanner} with all the typical persons.
     */
    public static EventPlanner getTypicalAddressBookWithoutEvents() {
        EventPlanner ab = new EventPlanner();
        for (Person person : getTypicalPersons()) {
            try {
                ab.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }
    // @@ author

    /**
     * Returns an {@code EventPlanner} with all the typical persons.
     */
    public static EventPlanner getTypicalAddressBook() {
        EventPlanner ab = new EventPlanner();
        for (Person person : getTypicalPersons()) {
            try {
                ab.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        for (EpicEvent epicEvent : getTypicalEvents()) {
            try {
                ab.addEvent(epicEvent);
            } catch (DuplicateEventException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
```
###### /java/guitests/guihandles/EpicEventCardHandle.java
``` java
/**
 * Provides a handle to a person card in the person list panel.
 */
public class EpicEventCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final List<Label> tagLabels;

    public EpicEventCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.nameLabel = getChildNode(NAME_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
```
