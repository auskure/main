package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.IsNotSelfOrMergedPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.MergedBuilder;

public class MergeCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(),
                                                    getTypicalNotesDownloaded(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    }

    @Test
    public void execute_validIndexForMerge_success() {
        String groupName = "Merge";
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        List<Integer> indices = new ArrayList<>();
        List<Person> personsToMerge = new ArrayList<>();

        indices.add(INDEX_FIRST_PERSON.getZeroBased());
        indices.add(mainList.size() - 1);

        personsToMerge.add(mainList.get(0));
        personsToMerge.add(mainList.get(mainList.size() - 1));

        MergedBuilder mergedBuilder = new MergedBuilder(personsToMerge, groupName);
        Person newGroup = mergedBuilder.getMergedPerson();

        expectedModel.addPerson(newGroup);
        expectedModel.commitAddressBook();

        assertMergeSuccess(indices, groupName);
    }

    @Test
    public void execute_invalidIndexForMerge_failure() {
        String groupName = "invalidIndex";
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        List<Integer> indexOverLimit = new ArrayList<>();
        List<Integer> indexUnderLimit = new ArrayList<>();

        int overLimit = mainList.size();
        indexOverLimit.add(overLimit);

        assertIndexSelectionFailure(indexOverLimit, groupName, MergeCommand.MESSAGE_INVALID_INDEX);

        int underLimit = -1;
        indexUnderLimit.add(underLimit);

        assertIndexSelectionFailure(indexUnderLimit, groupName, MergeCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void execute_editExistingGroup_success() {
        String groupName = "Edit";
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        List<Person> personsToMerge = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        personsToMerge.add(mainList.get(INDEX_FIRST_PERSON.getOneBased()));
        personsToMerge.add(mainList.get(INDEX_SECOND_PERSON.getOneBased()));

        MergedBuilder mergedBuilder = new MergedBuilder(personsToMerge, groupName);
        Person newGroup = mergedBuilder.getMergedPerson();

        model.addPerson(newGroup);
        expectedModel.addPerson(newGroup);
        expectedModel.commitAddressBook();

        indices.add(INDEX_FIRST_PERSON.getZeroBased());
        indices.add(INDEX_FIRST_PERSON.getZeroBased());

        assertGroupEditSuccess(indices, groupName);
    }


    /**
     * Executes a {@code MergeCommand} with the given {@code index}, and checks that correct contacts are merged
     * correctly.
     */
    private void assertMergeSuccess(List<Integer> indices, String groupName) {
        MergeCommand mergeCommand = new MergeCommand(indices, groupName);
        String expectedMessage = String.format(MergeCommand.MESSAGE_MERGE_TIMETABLE_SUCCESS);

        assertCommandSuccess(mergeCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code MergeCommand} with the given {@code index}, and checks that correct {@code CommandException}
     * is thrown
     */
    private void assertIndexSelectionFailure(List<Integer> indices, String groupName, String expectedMessage) {
        MergeCommand mergeCommand = new MergeCommand(indices, groupName);

        assertCommandFailure(mergeCommand, model, commandHistory, expectedMessage);
    }

    /**
     * Executes a {@code MergeCommand} with the given {@code index}, and checks that correct group is updated correctly.
     */
    private void assertGroupEditSuccess(List<Integer> indices, String groupName) {
        MergeCommand mergeCommand = new MergeCommand(indices, groupName);
        String expectedMessage = String.format(MergeCommand.MESSAGE_UPDATE_GROUP_SUCCESS, groupName);

        assertCommandSuccess(mergeCommand, model, commandHistory, expectedMessage, expectedModel);
    }

}
