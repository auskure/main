package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Set;

import seedu.address.logic.commands.DeleteSelectNotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteSelectNotesCommand object
 */
public class DeleteSelectNotesCommandParser implements Parser<DeleteSelectNotesCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteSelectNotesCommand parse(String args) throws ParseException {
        try {
            Set<String> moduleNames = ParserUtil.parseSelectModuleNames(args);
            return new DeleteSelectNotesCommand(moduleNames);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSelectNotesCommand.MESSAGE_USAGE), pe);
        }
    }

}
