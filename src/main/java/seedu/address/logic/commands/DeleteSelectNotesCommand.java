package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import java.util.Iterator;
import java.util.Set;

/**
 * Deletes all downloaded notes from the application.
 */
public class DeleteSelectNotesCommand extends Command {

    public static final String COMMAND_WORD = "deleteSelectNotes";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Deletes selected notes. "
            + "Parameters: "
            + "MODULE NAME...\n"
            + "Example: " + COMMAND_WORD + " "
            + "CS2101 "
            + "CS2113";

    public static final String MESSAGE_DELETE_ALL_NOTES_SUCCESS = "Selected notes have been deleted!\n";

    public static final String MESSAGE_UNAVAILABLE_NOTES = "Please enter names of modules you have already downloaded\n"
                                                        + "Run `showNotes` if you do not know which notes you have ";

    private final Set<String> moduleCodes;

    public DeleteSelectNotesCommand(Set moduleCodes) {
        this.moduleCodes = moduleCodes;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        removeInvalidNotes(model);
        if (moduleCodes.isEmpty()) {
            throw new CommandException(MESSAGE_UNAVAILABLE_NOTES);
        }
        model.deleteSelectedNotes(COMMAND_WORD, moduleCodes);
        return new CommandResult(MESSAGE_DELETE_ALL_NOTES_SUCCESS + moduleCodes.toString());
    }

    /**
     * Removes module names that are not available in storage
     */
    private void removeInvalidNotes(Model model) {
        String currentNotesList = model.getNotesList().toString();
        for (Iterator<String> iterator = moduleCodes.iterator(); iterator.hasNext();) {
            String request = iterator.next();
            if (!currentNotesList.contains(request)) {
                iterator.remove();
            }
        }

    }

}
