package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.*;
import seedu.address.testutil.MergedBuilder;

import java.util.*;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

public class UpdateMergedCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private String GROUP_NAME = "test";

    @Before
    public void setUp(){
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList =
                ((ObservableList<Person>) filteredPersonList).filtered(new IsNotSelfOrMergedPredicate());
        List<Integer> indices = new ArrayList<>();
        List<Person> personsToMerge = new ArrayList<>();

        indices.add(INDEX_FIRST_PERSON.getZeroBased());
        indices.add(mainList.size()-1);

        personsToMerge.add(mainList.get(0));
        personsToMerge.add(mainList.get(mainList.size()-1));

        MergedBuilder mergedBuilder = new MergedBuilder(personsToMerge, GROUP_NAME);
        Person newGroup = mergedBuilder.getMergedPerson();

        expectedModel.addPerson(newGroup);
        model.addPerson(newGroup);
    }
    @Test
    public void execute_updateAfterTimetableChange_success(){
        List<Person> filteredPersonList = expectedModel.getFilteredPersonList();
        List<Person> mainList =
                ((ObservableList<Person>) filteredPersonList).filtered(new IsNotSelfOrMergedPredicate());
        List<Person> mergedList = ((ObservableList<Person>) filteredPersonList).filtered(new IsMergedPredicate());
        Person personToChange = mainList.get(INDEX_FIRST_PERSON.getZeroBased());

        Map<String, List<TimeSlots>> timetableToChange = personToChange.getTimeSlots();
        List<TimeSlots> dayToChange = timetableToChange.get("mon");
        dayToChange.set(1, new TimeSlots("test"));
        personToChange = mergedList.get(0);
        timetableToChange = personToChange.getTimeSlots();
        dayToChange = timetableToChange.get("mon");
        TimeSlots changedTimeSlot = dayToChange.get(1);
        String newBusyNum = changedTimeSlot.toString();
        int newBusyNumInt = Integer.parseInt(newBusyNum);

        changedTimeSlot = new TimeSlots(Integer.toString(newBusyNumInt));
        dayToChange.set(1, changedTimeSlot);

        expectedModel.commitAddressBook();
        assertUpdateTimeSlotSuccess();
    }

    @Test
    public void execute_updateAfterDeletionChange_success() {
        List<Person> filteredPersonList = expectedModel.getFilteredPersonList();
        List<Person> mainList =
                ((ObservableList<Person>) filteredPersonList).filtered(new IsNotSelfOrMergedPredicate());
        List<Person> mergedList = ((ObservableList<Person>) filteredPersonList).filtered(new IsMergedPredicate());
        List<Person> personsToMerge = new ArrayList<>();
        Map<String, List<String>> removedPersons = new HashMap<>();

        Person personToDelete = mainList.get(mainList.size()-1);
        Person groupToUpdate = mergedList.get(0);

        personsToMerge.add(mainList.get(INDEX_FIRST_PERSON.getZeroBased()));
        new MergedBuilder(personsToMerge, GROUP_NAME);

        model.deletePerson(personToDelete);
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();

        List<String> affectedModules = new ArrayList<>();
        Name deletedPersonName = personToDelete.getName();
        Name affectedModuleName = groupToUpdate.getName();
        affectedModules.add(affectedModuleName.toString());
        removedPersons.put(deletedPersonName.toString(), affectedModules);
        String output = createCorrectOutput(removedPersons);

        assertUpdateDeletionSuccess(output);
    }
    private void assertUpdateTimeSlotSuccess() {
        UpdateMergedCommand updateMergedCommand = new UpdateMergedCommand();
        String expectedMessage = String.format(updateMergedCommand.MESSAGE_UPDATE_SUCCESS);

        assertCommandSuccess(updateMergedCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    private void assertUpdateDeletionSuccess(String affectedGroupsOutput){
        UpdateMergedCommand updateMergedCommand = new UpdateMergedCommand();
        String expectedMessage = UpdateMergedCommand.MESSAGE_UPDATE_SUCCESS_WITH_REMOVED_PERSONS + affectedGroupsOutput;

        assertCommandSuccess(updateMergedCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    String createCorrectOutput( Map<String, List<String>> removedPersons){
        String output = "";
        Iterator<Map.Entry<String, List<String>>> it = removedPersons.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> removedName = it.next();
            output = output + removedName.getKey() + ":" + " ";
            List<String> removedModules = removedName.getValue();
            for (String affectedGroup : removedModules) {
                if (affectedGroup.equalsIgnoreCase(removedModules.get(removedModules.size() - 1))) {
                    output = output + affectedGroup + "\n";
                } else {
                    output = output + affectedGroup + ", ";
                }
            }

        }
        return output;
    }
}
