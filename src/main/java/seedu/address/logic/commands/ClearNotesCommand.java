package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

import java.io.File;

/**
 * Deletes all downloaded notes from the application.
 */
public class ClearNotesCommand extends Command {

    public static final String COMMAND_WORD = "clearNotes";

    public static final String MESSAGE_USAGE = COMMAND_WORD;

    public static final String MESSAGE_DELETE_ALL_NOTES_SUCCESS = "All notes have been cleared!";

    private static final String PARAM_CURRENT_DIRECTORY = "user.dir";

    private static final String NOTES_PATH = "/notes";

    private static final String GITHUB_PLACEHOLDER_FILE_NAME = "placeholder.txt";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        deleteAllNotes();
        return new CommandResult(MESSAGE_DELETE_ALL_NOTES_SUCCESS);
    }

    /**
     * Deletes all notes in the notes folder
     */
    private static void deleteAllNotes() {
        String currentDirectoryPath = System.getProperty(PARAM_CURRENT_DIRECTORY);
        String notesDirectory = currentDirectoryPath + NOTES_PATH;

        File curDir = new File(notesDirectory);
        String currentName;
        File[] filesList = curDir.listFiles();
        for(File file : filesList) {
            currentName = file.getName();
            //No bookkeeping files stored by the Operating System should be deleted
            if(file.isHidden()){
                continue;
            }

            //The placeholding file for Github should not be deleted
            if(currentName.equals(GITHUB_PLACEHOLDER_FILE_NAME)) {
                continue;
            }

            String[] directoryContents = file.list();
            if(file.isDirectory() && directoryContents != null){
                recursiveDelete(file);
                continue;
            }

            file.delete();
        }
    }

    /**
     * Helps to delete notes in an occupied folder
     */
    private static void recursiveDelete(File file){
        if(file.isDirectory()){
            File[] directoryContents = file.listFiles();
            if(directoryContents != null){
                for(File content : directoryContents){
                    recursiveDelete(content);
                }
            }
        }
        file.delete();
    }

}
