package seedu.address.logic.commands;

//@@author BearPerson1

import static junit.framework.TestCase.assertTrue;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ShowNotesCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * test if properly detects file location
     */

    @Test

    public void execute_showNotes_properlyFindNotesFolderSuccess() {
        String intendedFileLocation = System.getProperty("user.dir") + ShowNotesCommand.getNotesPathExtension();
        File notesFolder = new File(intendedFileLocation);
        assertTrue(notesFolder.exists());
    }

}
