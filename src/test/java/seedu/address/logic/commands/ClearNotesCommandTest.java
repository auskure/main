package seedu.address.logic.commands;
//@@author auskure
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalModuleCodes.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearNotesCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    /**
     * checks ClearNotes executes correctly on an empty notesDownloaded, within an empty model.
     */
    @Test
    public void execute_emptyNotesDownloaded_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.clearNotesData(ClearNotesCommand.COMMAND_WORD);

        assertCommandSuccess(new ClearNotesCommand(), model, commandHistory,
                                ClearNotesCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * checks ClearNotes executes correctly on an non-empty notesDownloaded, within a non-empty model.
     */
    @Test
    public void execute_nonEmptyNotesDownloaded_success() {
        Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
        expectedModel.clearNotesData(ClearNotesCommand.COMMAND_WORD);

        assertCommandSuccess(new ClearNotesCommand(), model, commandHistory,
                                ClearNotesCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
