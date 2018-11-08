package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.logic.commands.ChangeTimeSlotCommand;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

public class ChangeTimeSlotCommandParserTest {
    private ChangeTimeSlotCommandParser parser = new ChangeTimeSlotCommandParser();

    @Test
    public void parse_validArgs_returnsChangeCommand(){
        String index = "1";
        String[] actions = {"1", "mon", "10am", "GER1000"};

        ChangeTimeSlotCommand expectedChangeCommand = new ChangeTimeSlotCommand(index, actions);

        assertParseSuccess(parser, "1 mon 10am GER1000", expectedChangeCommand);
    }

    @Test
    public void parse_invalidDay_throwsParseException(){
        assertParseFailure(parser, "1 a 10am GER1000",
                ChangeTimeSlotCommand.MESSAGE_INVALID_DAY + ChangeTimeSlotCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "7 1 10am GER1000",
                ChangeTimeSlotCommand.MESSAGE_INVALID_DAY + ChangeTimeSlotCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "1 sat 10am GER1000",
                ChangeTimeSlotCommand.MESSAGE_INVALID_DAY + ChangeTimeSlotCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidTime_throwsParseException(){
        assertParseFailure(parser, "1 mon 7am GER1000",
                ChangeTimeSlotCommand.MESSAGE_INVALID_TIME + ChangeTimeSlotCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "1 mon 8pm GER1000",
                ChangeTimeSlotCommand.MESSAGE_INVALID_TIME + ChangeTimeSlotCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidArgs_throwsParseException(){
        assertParseFailure(parser, "1 mon 10am", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangeTimeSlotCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 mon GER1000", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeTimeSlotCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "1 10am GER1000", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeTimeSlotCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "mon 10am GER100", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeTimeSlotCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeTimeSlotCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "test", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeTimeSlotCommand.MESSAGE_USAGE));
    }
}

