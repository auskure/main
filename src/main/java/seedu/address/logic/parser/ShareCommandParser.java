package seedu.address.logic.parser;

import seedu.address.logic.commands.ShareCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;


/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ShareCommandParser implements Parser<ShareCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ShareCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShareCommand.MESSAGE_USAGE));
        }
        String[] keywords = trimmedArgs.split("\\s+");
        if (!keywords[0].equalsIgnoreCase("private") && !keywords[0].equalsIgnoreCase("public")) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShareCommand.MESSAGE_USAGE));
        }
        if (keywords.length > 2) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShareCommand.MESSAGE_USAGE));
        }
        return new ShareCommand(keywords[0], keywords[1]);
    }
}
