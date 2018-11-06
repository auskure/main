package seedu.address.logic.commands;
//@@author BearPerson1

import java.io.File;
import java.nio.file.Paths;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class ShowNotesCommand extends Command {
    public static final String COMMAND_WORD = "showNotes";

    private static final String DIRECTORY_IDENTIFIER = "Directory: ";

    private static final String FILE_IDENTIFIER = "File: ";

    private static final String NEWLINE_SEPARATOR = "\r\n";

    private static final String LINE_SEPARATOR = "====================================================================";

    private static final String MESSAGE_FILE_LOCATION_ERROR = "The folder \"notes\" is not found!" + NEWLINE_SEPARATOR
            + "please download some notes to continue using showNotes";

    private String currentDirPath = Paths.get(".").toAbsolutePath().normalize().toString();

    public static String NOTES_PATH_EXTENSION = "/notes";

    private String notesPath = currentDirPath + NOTES_PATH_EXTENSION;

    private String notesResult = "";

    public String MESSAGE_SUCCESS = "Here are your Notes stored in:" + NEWLINE_SEPARATOR + notesPath + NEWLINE_SEPARATOR;

    private int DEFAULT_TAB_COUNT = 0;

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        try {
            getDirectoryFileValues(new File(notesPath), DEFAULT_TAB_COUNT);
        } catch (NullPointerException npe) {
            throw new CommandException(MESSAGE_FILE_LOCATION_ERROR);
        }
        return new CommandResult(MESSAGE_SUCCESS + notesResult);
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
        for (File file : files) {
            if (file.isDirectory()) {
                if (count == 0) {
                    notesResult += LINE_SEPARATOR + NEWLINE_SEPARATOR;
                }
                notesResult += tabPlaceholder + DIRECTORY_IDENTIFIER + file.getName() + NEWLINE_SEPARATOR;
                getDirectoryFileValues(file, count + 1);
            } else {
                notesResult += tabPlaceholder + FILE_IDENTIFIER + file.getName() + NEWLINE_SEPARATOR;
            }
        }
    }
}
