package seedu.address.logic.commands;
//@@author BearPerson1

import static seedu.address.commons.util.FileUtil.currentDirectory;

import java.io.File;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * ShowNotesCommand locates the "notes" folder as specified by the DownloadAbstract class, it then recursively
 * searches for the files and sorts them based on the file type: Directory or file. And appends tabs and spaces
 * according to the depth of the file.
 * <p>
 * ShowNotesCommand extends on the Command class.
 */


public class ShowNotesCommand extends Command {
    public static final String COMMAND_WORD = "showNotes";

    private static final String DIRECTORY_IDENTIFIER = "Directory: ";

    private static final String FILE_IDENTIFIER = "File: ";

    private static final String NEWLINE_SEPARATOR = "\r\n";

    private static final String LINE_SEPARATOR =
            "====================================================================";

    private static final String NOTES_PATH_EXTENSION = DownloadAbstract.DOWNLOAD_FILE_PATH;

    private String currentDirPath = currentDirectory();

    private String notesPath = currentDirPath + NOTES_PATH_EXTENSION;

    private String notesResult = "";

    private String messageShownotesSuccess = "Here are your Notes stored in:"
            + NEWLINE_SEPARATOR + notesPath + NEWLINE_SEPARATOR;

    private int defaultTabCount = 0;

    public static String getNotesPathExtension() {
        return NOTES_PATH_EXTENSION;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        try {
            getDirectoryFileValues(new File(notesPath), defaultTabCount);
        } catch (NullPointerException npe) {
            throw new CommandException(Messages.MESSAGE_FILE_LOCATION_ERROR);
        }
        return new CommandResult(messageShownotesSuccess + notesResult);
    }

    /**
     * recursively searches for all the files and parses it into notesResult
     *
     * @param dir   the current starting directory.
     * @param count used to keeptrack of the number of tabs.
     */
    public void getDirectoryFileValues(File dir, int count) {
        File[] files = dir.listFiles();
        if (count == 1) {
            notesResult += LINE_SEPARATOR + NEWLINE_SEPARATOR;
        }
        /**
         *  tabPlaceholder is used to insert tabs to make it look more visually appealing
         *  Count is recursively increased, ie: the deeper the directory, the more tabs the files would have.
         */
        String tabPlaceholder = "";
        for (int i = 0; i < count; i++) {
            tabPlaceholder += "\t";
        }
        for (File currentFile : files) {
            if (currentFile.isDirectory()) {
                if (count == 0) {
                    notesResult += LINE_SEPARATOR + NEWLINE_SEPARATOR;
                }
                notesResult += tabPlaceholder + DIRECTORY_IDENTIFIER + currentFile.getName() + NEWLINE_SEPARATOR;
                getDirectoryFileValues(currentFile, count + 1);
            } else {
                notesResult += tabPlaceholder + FILE_IDENTIFIER + currentFile.getName() + NEWLINE_SEPARATOR;
            }
        }
    }
}
