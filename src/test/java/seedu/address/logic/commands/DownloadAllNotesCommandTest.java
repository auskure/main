package seedu.address.logic.commands;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

import java.io.File;

import static junit.framework.TestCase.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.CORRECT_MODULE_CODE;
import static seedu.address.logic.commands.CommandTestUtil.INCORRECT_MODULE_CODE;
import static seedu.address.logic.commands.CommandTestUtil.INCORRECT_PASSWORD;
import static seedu.address.logic.commands.CommandTestUtil.INCORRECT_USERNAME;


public class DownloadAllNotesCommandTest {

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * checks if incorrect username and password fails correctly
     */
    @Test
    public void execute_DownloadAllNotesCommand_WrongUserNameAndPass() {
        DownloadAllNotesCommand command = new DownloadAllNotesCommand(INCORRECT_USERNAME, INCORRECT_PASSWORD,
                CORRECT_MODULE_CODE);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
    }

    /**
     * checks if incorrect module code will fail correctly
     */

    @Test
    public void execute_DownloadAllNotesCommand_InvalidModuleCode() {
        DownloadAllNotesCommand command = new DownloadAllNotesCommand(INCORRECT_USERNAME, INCORRECT_PASSWORD,
                INCORRECT_MODULE_CODE);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
    }

    /**
     * checks if notes file is correctly created after execution of downloadAllNotesCommand.
     */

    @Test
    public void execute_notesFilesCreated() {
        DownloadAllNotesCommand command = new DownloadAllNotesCommand(INCORRECT_USERNAME, INCORRECT_PASSWORD,
                CORRECT_MODULE_CODE);
        String intendedFileLocation = System.getProperty("user.dir") + DownloadAllNotesCommand.DOWNLOAD_FILE_PATH;
        File notesFile = new File(intendedFileLocation);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
        assertTrue(notesFile.exists());
    }

    /**
     * check if windows chrome driver is properly extracted
     */

    @Test
    public void execute_WindowsDriverExtracted() {
        DownloadAllNotesCommand command = new DownloadAllNotesCommand(INCORRECT_USERNAME, INCORRECT_PASSWORD,
                CORRECT_MODULE_CODE);
        String intendedFileLocation = System.getProperty("user.dir") +
                "/" + DownloadAllNotesCommand.WINDOWS_CHROME_DRIVER_DIRECTORY;
        File windowsDriverDir = new File(intendedFileLocation);
        intendedFileLocation += "/" + DownloadAllNotesCommand.WINDOWS_CHROME_DRIVER_NAME;
        File windowsChromeDriver = new File(intendedFileLocation);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
        try {
            assertTrue(windowsDriverDir.exists());
        } catch (NullPointerException npe) {
            throw new AssertionError("MacDirectory was not created");
        }
        try {
            assertTrue(windowsChromeDriver.exists());
        } catch (NullPointerException npe) {
            throw new AssertionError("MacDirectory was not created");
        }
    }

    /**
     * check if mac chrome driver is properly extracted.
     */

    @Test
    public void execute_MacDriverExtracted() {
        DownloadAllNotesCommand command = new DownloadAllNotesCommand(INCORRECT_USERNAME, INCORRECT_PASSWORD,
                CORRECT_MODULE_CODE);
        String intendedFileLocation = System.getProperty("user.dir") +
                "/" + DownloadAllNotesCommand.MAC_CHROME_DRIVER_DIRECTORY;
        File macDriverDir = new File(intendedFileLocation);
        intendedFileLocation += "/" + DownloadAllNotesCommand.MAC_CHROME_DRIVER_NAME;
        File macChromeDriver = new File(intendedFileLocation);
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
        try {
            assertTrue(macDriverDir.exists());
        } catch (NullPointerException npe) {
            throw new AssertionError("MacDirectory was not created");
        }
        try {
            assertTrue(macChromeDriver.exists());
        } catch (NullPointerException npe) {
            throw new AssertionError("MacDirectory was not created");
        }
    }

    /**
     * check if notes download is clear of files of the "crdownload" file type.
     */

    @Test
    public void execute_checkNotesFolderClearForDownload() {
        DownloadAllNotesCommand command = new DownloadAllNotesCommand(INCORRECT_USERNAME, INCORRECT_PASSWORD,
                CORRECT_MODULE_CODE);
        String intendedFileLocation = System.getProperty("user.dir") +
                DownloadAllNotesCommand.DOWNLOAD_FILE_PATH;
        assertCommandFailure(command, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
        File notesFile = new File(intendedFileLocation);
        String[] filesInNotesFile = notesFile.list();
        try {
            for (String files : filesInNotesFile) {
                assertTrue(files.contains(DownloadAllNotesCommand.DOWNLOAD_FILE_ONGOING_EXTENSION));
            }
        } catch (Exception e) {
            throw new AssertionError("A crdownload file exist");
        }
    }

}
