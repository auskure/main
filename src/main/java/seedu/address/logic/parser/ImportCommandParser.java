package seedu.address.logic.parser;

import seedu.address.logic.commands.ImportCommand;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {
    private ArgumentMultimap argMultimap;

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     *
     */
    public ImportCommand parse(String args) {

        return new ImportCommand(args.trim());
    }
}
