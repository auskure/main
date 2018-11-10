package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.TimetableContainsModulePredicate;

public class FilterCommandParserTest {
    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        List<String> keywords = new ArrayList<>();
        keywords.add("mon");
        keywords.add("2");
        TimetableContainsModulePredicate predicate = new TimetableContainsModulePredicate(keywords);

        FilterCommand expectedFilterCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, "mon 10am", expectedFilterCommand);

        keywords.add("CS2040c");

        expectedFilterCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, "mon 10am CS2040c", expectedFilterCommand);

        keywords.set(0, "CS2040c");
        keywords.remove(1);
        keywords.remove(1);

        expectedFilterCommand = new FilterCommand(predicate);

        assertParseSuccess(parser, "CS2040c", expectedFilterCommand);
    }

    @Test
    public void parse_invalidTime_throwsParseException() {
        assertParseFailure(parser, "mon 10", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "mon am", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "mon 8pm", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "mon 7am", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "mon", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }
}
