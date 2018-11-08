package seedu.address.logic.parser;
//@@author BearPerson1

import seedu.address.logic.commands.DownloadAllNotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.stream.Stream;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

/**
 * DownloadAllNotesCommandParser parses the input that follows after the
 * DownloadAllNotesCommand.COMMAND_WORD that follows the prefix. and creates a new DownloadAllNotesCommand
 * object
 *
 * DownloadAllNotesCommandParser implements the interface parser
 */

public class DownloadAllNotesCommandParser implements Parser {

    @Override
    public DownloadAllNotesCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_USERNAME, PREFIX_PASSWORD,
                PREFIX_MODULECODE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD, PREFIX_USERNAME, PREFIX_MODULECODE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DownloadAllNotesCommand.MESSAGE_USAGE));
        }

        String username = argMultimap.getValue(PREFIX_USERNAME).get();
        String password = argMultimap.getValue(PREFIX_PASSWORD).get();
        String moduleCode = argMultimap.getValue(PREFIX_MODULECODE).get();

        return new DownloadAllNotesCommand(username, password, moduleCode);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
