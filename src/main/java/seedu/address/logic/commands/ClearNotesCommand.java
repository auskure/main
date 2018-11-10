package seedu.address.logic.commands;
//@@author auskure
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;


/**
 * Deletes all downloaded notes from the application.
 */
public class ClearNotesCommand extends Command {

    public static final String COMMAND_WORD = "clearNotes";

    public static final String MESSAGE_USAGE = COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "All notes have been cleared!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {

        model.clearNotesData(COMMAND_WORD);
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
