package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;


/**
 * Deletes all downloaded notes from the application.
 */
public class ClearNotesCommand extends Command {

    public static final String COMMAND_WORD = "clearNotes";

    public static final String MESSAGE_USAGE = COMMAND_WORD;

    public static final String MESSAGE_DELETE_ALL_NOTES_SUCCESS = "All notes have been cleared!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {

        model.indicateNotesChanged(COMMAND_WORD);

        return new CommandResult(MESSAGE_DELETE_ALL_NOTES_SUCCESS);
    }

}
