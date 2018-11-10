package seedu.address.logic.commands;

import static junit.framework.TestCase.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ShowNotesCommandTest {
    private static final String CURRENT_NOTES_DIR_RESULT = "Here are your Notes stored in:\r\n"
            + System.getProperty("user.dir")
            + "/notes\r\n"
            + "====================================================================\r\n"
            + "Directory: CS2105_NOTES - Introduction to Computer Networks\r\n"
            + "====================================================================\r\n"
            + "\tDirectory: introduction to computer networks\r\n"
            + "\t\tDirectory: Assignments\r\n"
            + "\t\t\tFile: ass00.pdf\r\n"
            + "\t\t\tFile: ass01.pdf\r\n"
            + "\t\t\tFile: ass01_files.zip\r\n"
            + "\t\t\tFile: ass02.pdf\r\n"
            + "\t\t\tFile: ass02_files.zip\r\n"
            + "\t\t\tFile: ass03.pdf\r\n"
            + "\t\t\tFile: ass03_files.zip\r\n"
            + "\t\t\tFile: ass03_supplement.pdf\r\n"
            + "\t\t\tFile: UnreliNET.zip\r\n"
            + "\t\t\tFile: WebServer.py\r\n"
            + "\t\tDirectory: Lecture Slides\r\n"
            + "\t\t\tFile: Lecture 0 - Welcome to CS2105_NOTES.pdf\r\n"
            + "\t\t\tFile: Lecture 1 - Introduction.pdf\r\n"
            + "\t\t\tFile: Lecture 10 - Local Area Network.pdf\r\n"
            + "\t\t\tFile: Lecture 2 - Application Layer.pdf\r\n"
            + "\t\t\tFile: Lecture 3 - Socket Programming.pdf\r\n"
            + "\t\t\tFile: Lecture 4 - Reliable Protocols.pdf\r\n"
            + "\t\t\tFile: Lecture 5 - UDP and TCP.pdf\r\n"
            + "\t\t\tFile: Lecture 6 - Network Layer.pdf\r\n"
            + "\t\t\tFile: Lecture 7 - Routing.pdf\r\n"
            + "\t\t\tFile: Lecture 8 - Network Security.pdf\r\n"
            + "\t\t\tFile: Lecture 9 - Link Layer.pdf\r\n"
            + "\t\tDirectory: Misc\r\n"
            + "\t\t\tFile: midterm answers.txt\r\n"
            + "\t\t\tFile: midterm-seating.pdf\r\n"
            + "\t\t\tFile: sample questions 1.pdf\r\n"
            + "\t\t\tFile: sample questions 2.pdf\r\n"
            + "\t\t\tFile: sample questions 3.pdf\r\n"
            + "\t\tDirectory: Tutorials\r\n"
            + "\t\t\tFile: T3Q3programs.zip\r\n"
            + "\t\t\tFile: tut01-ans.pdf\r\n"
            + "\t\t\tFile: tut01.pdf\r\n"
            + "\t\t\tFile: tut02-ans.pdf\r\n"
            + "\t\t\tFile: tut02.pdf\r\n"
            + "\t\t\tFile: tut03-ans.pdf\r\n"
            + "\t\t\tFile: tut03.pdf\r\n"
            + "\t\t\tFile: tut04-ans.pdf\r\n"
            + "\t\t\tFile: tut04.pdf\r\n"
            + "\t\t\tFile: tut05.pdf\r\n"
            + "\t\t\tFile: tut06.pdf\r\n"
            + "\t\t\tFile: tut07.pdf\r\n"
            + "\t\t\tFile: tut08.pdf\r\n"
            + "\t\t\tFile: tut9.pdf\r\n";

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * test if properly detects file location
     */

    public void execute_showNotes_properlyFindNotesFolderSuccess() {
        String intendedFileLocation = System.getProperty("user.dir") + ShowNotesCommand.getNotesPathExtension();
        File notesFolder = new File(intendedFileLocation);
        assertTrue(notesFolder.exists());
    }

}
