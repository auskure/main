package seedu.address.logic.commands;
//@@author auskure
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyNotesDownloaded;

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

    public static final String MESSAGE_DELETE_ALL_NOTES_SUCCESS = "The following notes have been deleted: ";
    public static final String MESSAGE_DELETE_ALL_NOTES_CAUTION = "\nThe following entries are invalid: ";

    public static final String MESSAGE_UNAVAILABLE_NOTES = "Please enter names of modules you have already downloaded\n"
                                                        + "Run `showNotes` if you do not know which notes you have ";

    private final Set<String> requestModuleCodes;
    private final Set<String> invalidModuleCodes;
    private final Set<String> finalModuleCodes;

    public DeleteSelectNotesCommand(Set requestModuleCodes) {
        this.requestModuleCodes = requestModuleCodes;
        this.invalidModuleCodes = new TreeSet<>();
        this.finalModuleCodes = new TreeSet<>();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        removeInvalidModuleCodes(model);
        if (requestModuleCodes.isEmpty()) {
            throw new CommandException(MESSAGE_UNAVAILABLE_NOTES);
        }
        getFullModuleNames(model);
        model.deleteSelectedNotes(COMMAND_WORD, finalModuleCodes);
        return new CommandResult(MESSAGE_DELETE_ALL_NOTES_SUCCESS + finalModuleCodes.toString()
                                        + MESSAGE_DELETE_ALL_NOTES_CAUTION + invalidModuleCodes.toString());
    }

    /**
     * Removes module names that are not available in storage
     */
    private void removeInvalidModuleCodes(Model model) {
        ReadOnlyNotesDownloaded notesDownloaded = model.getNotesList();
        Set<String> notesList = notesDownloaded.getNotesList();
        boolean isValid;
        for (Iterator<String> iterator = requestModuleCodes.iterator(); iterator.hasNext();) {
            String request = iterator.next();
            isValid = false;
            for (String tempNotes : notesList) {
                if (tempNotes.contains(request)) {
                    isValid = true;
                }
            }
            if (!isValid) {
                invalidModuleCodes.add(request);
                iterator.remove();
            }
        }

    }

    /**
     * Replaces any partial module names with full module names.
     */
    private void getFullModuleNames(Model model) {
        ReadOnlyNotesDownloaded notesDownloaded = model.getNotesList();
        Set<String> notesList = notesDownloaded.getNotesList();
        for (Iterator<String> iterator = requestModuleCodes.iterator(); iterator.hasNext();) {
            String request = iterator.next();
            for (String tempNotes : notesList) {
                if (tempNotes.contains(request)) {
                    finalModuleCodes.add(tempNotes);
                }
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteSelectNotesCommand // instanceof handles nulls
                && requestModuleCodes.equals(((DeleteSelectNotesCommand) other).requestModuleCodes)); // state check
    }

}
