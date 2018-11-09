package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.IsNotSelfOrMergedPredicate;
import seedu.address.model.person.IsSelfPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TimeSlots;

public class ChangeTimeSlotCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validContactChange_success() {
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        String[] days = {"mon", "tue", "wed", "thu", "fri"};
        Map<String, List<TimeSlots>> timeSlots;
        Map<String, List<TimeSlots>> changedTimeSlots = new HashMap<>();
        List<TimeSlots> monday;


        String[] actions = {"1", "mon", "10am", "GER1000"};
        String index = "1";

        Person personToChange = mainList.get(0);
        timeSlots = personToChange.getTimeSlots();
        monday = timeSlots.get("mon");
        monday.set(3, new TimeSlots("GER1000"));

        for (String day : days) {
            changedTimeSlots.put(day, timeSlots.get(day));
        }

        Person changedPerson = new Person(personToChange.getName(), personToChange.getPhone(),
                personToChange.getEmail(), personToChange.getAddress(), personToChange.getTags(),
                personToChange.getEnrolledModules(), changedTimeSlots);

        expectedModel.updatePerson(personToChange, changedPerson);
        expectedModel.commitAddressBook();

        assertContactChangeSuccess(index, actions, personToChange);

    }

    @Test
    public void execute_validSelfChange_success() {
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> selfList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsSelfPredicate());
        String[] days = {"mon", "tue", "wed", "thu", "fri"};
        Map<String, List<TimeSlots>> timeSlots;
        Map<String, List<TimeSlots>> changedTimeSlots = new HashMap<>();
        List<TimeSlots> monday;


        String[] actions = {"1", "mon", "10am", "GER1000"};
        String index = "self";

        Person personToChange = selfList.get(0);
        timeSlots = personToChange.getTimeSlots();
        monday = timeSlots.get("mon");
        monday.set(3, new TimeSlots("GER1000"));

        for (String day : days) {
            changedTimeSlots.put(day, timeSlots.get(day));
        }

        Person changedPerson = new Person(personToChange.getName(), personToChange.getPhone(),
                personToChange.getEmail(), personToChange.getAddress(), personToChange.getTags(),
                personToChange.getEnrolledModules(), changedTimeSlots);

        expectedModel.updatePerson(personToChange, changedPerson);
        expectedModel.commitAddressBook();

        assertSelfChangeSuccess(index, actions);
    }

    @Test
    public void execute_noTimeSlotChanged_success() {
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        Map<String, List<TimeSlots>> timeSlots;
        List<TimeSlots> monday;


        String[] actions = {"1", "mon", "10am", "free"};
        String index = "1";

        Person personToChange = mainList.get(0);
        timeSlots = personToChange.getTimeSlots();
        monday = timeSlots.get("mon");
        TimeSlots mon10amSlot = monday.get(3);
        actions[3] = mon10amSlot.toString();

        assertNothingChangedFailure(index, actions);

    }

    @Test
    public void execute_invalidIndex_failure() {
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
        String[] actions = {"0", "mon", "10am"};

        String index = "0";
        assertIndexSelectionFailure(index, actions, expectedMessage);

        int overLimit = mainList.size() + 1;
        index = Integer.toString(overLimit);
        assertIndexSelectionFailure(index, actions, expectedMessage);

        index = "hi";
        assertIndexSelectionFailure(index, actions, expectedMessage);

        index = " ";
        assertIndexSelectionFailure(index, actions, expectedMessage);
    }

    /**
     * Executes a {@code ChangeTimeSlotCommand} with the given {@code index} and {@code actions}, and checks that the
     * correct person has the correct time slot changed in the correct way.
     */
    private void assertContactChangeSuccess(String index, String[] actions, Person personToChange) {
        ChangeTimeSlotCommand changeCommand = new ChangeTimeSlotCommand(index, actions);
        String expectedMessage = String.format(ChangeTimeSlotCommand.MESSAGE_EDIT_PERSON_SUCCESS, personToChange);

        assertCommandSuccess(changeCommand, model, commandHistory, expectedMessage, expectedModel);
    }
    /**
     * Executes a {@code ChangeTimeSlotCommand} with the given {@code index} and {@code actions}, and checks that the
     * self contact has the correct time slot changed in the correct way.
     */
    public void assertSelfChangeSuccess(String index, String[] actions) {
        ChangeTimeSlotCommand changeCommand = new ChangeTimeSlotCommand(index, actions);
        String expectedMessage = ChangeTimeSlotCommand.MESSAGE_EDIT_SELF_SUCCESS;

        assertCommandSuccess(changeCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code ChangeTimeSlotCommand} with the given {@code index}, {@code actions}, and checks that a
     * {@code CommandException}is thrown with the {@code expectedMessage}.
     */
    public void assertNothingChangedFailure(String index, String[] actions) {
        ChangeTimeSlotCommand changeCommand = new ChangeTimeSlotCommand(index, actions);
        String expectedMessage = ChangeTimeSlotCommand.MESSAGE_NOTHING_CHANGED;

        assertCommandFailure(changeCommand, model, commandHistory, expectedMessage);
    }

    /**
     * Executes a {@code ChangeTimeSlotCommand} with the given {@code index}, {@code actions}, and checks that a
     * {@code CommandException}is thrown with the {@code expectedMessage}.
     */
    private void assertIndexSelectionFailure(String index, String[] actions, String expectedMessage) {
        ChangeTimeSlotCommand changeCommand = new ChangeTimeSlotCommand(index, actions);

        assertCommandFailure(changeCommand, model, commandHistory, expectedMessage);
    }
}
