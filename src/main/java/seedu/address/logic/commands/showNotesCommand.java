package seedu.address.logic.commands;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;

import java.io.*;

import static seedu.address.logic.commands.DownloadAbstract.PARAM_CURRENT_DIRECTORY;

public class showNotesCommand extends Command{

    public String currentDirPath = System.getProperty(PARAM_CURRENT_DIRECTORY);

    public String notesPathExtension = "/notes";

    public String notesPath = currentDirPath + notesPathExtension;

    public static final String COMMAND_WORD = "showNotes";

    public static final String DIRECTORY_IDENTIFIER = "Directory: ";

    public static final String FILE_IDENTIFIER = "File: ";

    public static final String NEWLINE_SEPERATOR = "\r\n";

    public static final String LINE_SEPERATOR = "====================================================================";

    public String MESSAGE_STORED_NOTES = "";

    public String MESSAGE_SUCCESS = "Here are your Notes stored in: \r\n" + notesPath + "\r\n";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {

        getDirectoryFileValues(new File(notesPath),1);
        return new CommandResult(MESSAGE_SUCCESS+ MESSAGE_STORED_NOTES);

    }

    /**
     * recursively searches for all the files and parses it into MESSAGE_STORED_NOTES
     * @param dir the current starting directory.
     * @param count used to keeptrack of the number of tabs.
     */
    public void getDirectoryFileValues(File dir, int count) {
        File[] files = dir.listFiles();
        if(count==1){
            MESSAGE_STORED_NOTES += LINE_SEPERATOR + NEWLINE_SEPERATOR;
        }
        /**
         *  tabPlaceholder is used to insert tabs to make it look more visually appealing
         */
        String tabPlaceholder=new String();
        for(int i=0;i<count;i++) {
            tabPlaceholder+="\t";
        }
        for (File file : files) {
            if (file.isDirectory()) {
                MESSAGE_STORED_NOTES += tabPlaceholder + DIRECTORY_IDENTIFIER + file.getName() + NEWLINE_SEPERATOR;
                getDirectoryFileValues(file,count+1);
            } else {
                MESSAGE_STORED_NOTES += tabPlaceholder + FILE_IDENTIFIER + file.getName() + NEWLINE_SEPERATOR;
            }
        }
    }
}
