package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import seedu.address.logic.commands.MergeCommand;

public class MergeCommandParserTest {
    private MergeCommandParser parser = new MergeCommandParser();

    @Test
    public void parse_validArgs_returnsMergeCommand() {
        List<Integer> indices = new ArrayList<>();
        String groupName = "test";
        indices.add(INDEX_FIRST_PERSON.getZeroBased());
        indices.add(INDEX_SECOND_PERSON.getZeroBased());

        MergeCommand expectedMergeCommand = new MergeCommand(indices, groupName);

        assertParseSuccess(parser, " m/1 m/2 n/test", expectedMergeCommand);
    }

    @Test
    public void parse_noGroupName_throwsParseException() {
        assertParseFailure(parser, " m/1 m/2", MergeCommand.MESSAGE_NO_GROUP_NAME + " "
                + MergeCommand.MESSAGE_USAGE);

    }

    @Test
    public void parse_indexNotANumber_throwsParseException() {
        assertParseFailure(parser, " m/1 m/a n/test",
                MergeCommand.MESSAGE_INDEX_NEEDS_TO_BE_NUMBER + MergeCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "hi", String.format(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MergeCommand.MESSAGE_USAGE)));

    }

}
