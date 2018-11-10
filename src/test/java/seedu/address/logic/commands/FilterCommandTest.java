package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.IsNotSelfOrMergedPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TimetableContainsModulePredicate;

public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(),
                                                    getTypicalNotesDownloaded(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validTimeToFilter_success() {
        List<String> keywords = new ArrayList<>();
        keywords.add("tue");
        keywords.add("2");
        TimetableContainsModulePredicate predicate = new TimetableContainsModulePredicate(keywords);

        expectedModel.updateFilteredPersonList(predicate);
        assertFilterSuccess(predicate);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Test
    public void execute_validActivityToFilter_success() {
        List<String> keywords = new ArrayList<>();
        keywords.add("CS2040c");

        TimetableContainsModulePredicate predicate = new TimetableContainsModulePredicate(keywords);

        expectedModel.updateFilteredPersonList(predicate);
        assertFilterSuccess(predicate);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Test
    public void execute_validActivityAndTimeToFilter_success() {
        List<String> keywords = new ArrayList<>();
        keywords.add("CS2040c");
        keywords.add("mon");
        keywords.add("3");

        TimetableContainsModulePredicate predicate = new TimetableContainsModulePredicate(keywords);

        expectedModel.updateFilteredPersonList(predicate);
        assertFilterSuccess(predicate);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Test
    public void execute_validMultipleActivitiesAndTimesToFilter_success() {
        List<String> keywords = new ArrayList<>();
        keywords.add("CS2040c");
        keywords.add("mon");
        keywords.add("3");
        keywords.add("GER1000");
        keywords.add("mon");
        keywords.add("4");

        TimetableContainsModulePredicate predicate = new TimetableContainsModulePredicate(keywords);

        expectedModel.updateFilteredPersonList(predicate);
        assertFilterSuccess(predicate);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Executes a {@code FilterCommand} with the given {@code predicate}, and checks that
     * the address book is filtered correctly.
     */
    private void assertFilterSuccess(TimetableContainsModulePredicate predicate) {
        FilterCommand filterCommand = new FilterCommand(predicate);
        List<Person> filteredPersonList = model.getFilteredPersonList();
        List<Person> mainList = ((ObservableList<Person>) filteredPersonList)
                .filtered(new IsNotSelfOrMergedPredicate());
        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, ((FilteredList<Person>)
                mainList).filtered(predicate).size());

        assertCommandSuccess(filterCommand, model, commandHistory, expectedMessage, expectedModel);
    }
}
