package seedu.address.logic.commands;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

public class DownloadAllNotesCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();


    @Test
    public void dummy() {
        DownloadAllNotesCommand cmd = new DownloadAllNotesCommand("dummy", "dummy", "CS1010");
        assertCommandFailure(cmd, model, commandHistory, Messages.MESSAGE_USERNAME_PASSWORD_ERROR);
    }
}
