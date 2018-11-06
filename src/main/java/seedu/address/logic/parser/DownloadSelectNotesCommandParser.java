package seedu.address.logic.parser;
//@@author BearPerson1
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELECT_FILE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERNAME;

import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.DownloadSelectNotesCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DownloadSelectNotesCommandParser implements Parser {

    @Override
    public DownloadSelectNotesCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_USERNAME, PREFIX_PASSWORD,
            PREFIX_MODULECODE, PREFIX_SELECT_FILE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD, PREFIX_USERNAME, PREFIX_MODULECODE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DownloadSelectNotesCommand.MESSAGE_USAGE));
        }

        String username = argMultimap.getValue(PREFIX_USERNAME).get();
        String password = argMultimap.getValue(PREFIX_PASSWORD).get();
        String moduleCode = argMultimap.getValue(PREFIX_MODULECODE).get();

        if (argMultimap.getValue(PREFIX_SELECT_FILE).isPresent()) {
            String fileSelect = argMultimap.getValue(PREFIX_SELECT_FILE).get();
            if (fileSelect.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    Messages.MESSAGE_DOWNLOAD_SELECT_NO_FILES_SELECTED));

            }
            return new DownloadSelectNotesCommand(username, password, moduleCode, fileSelect);
        } else {
            return new DownloadSelectNotesCommand(username, password, moduleCode);
        }

    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
