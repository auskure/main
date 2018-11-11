package seedu.address.logic.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.ui.testutil.EventsCollectorRule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

import static org.junit.Assert.*;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains integration tests (interaction with the Model) for {@code ExportCommand}.
 */
public class ExportCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    private String theString = "";

    @Before
    public void setUp() throws CommandException {
        ExportCommand ec = new ExportCommand("public", "1");

        CommandResult cr = ec.execute(model,commandHistory);
        String exportString = cr.feedbackToUser;
        //remove string that informs user that the string is copied
        exportString = exportString.replaceAll("The string has been copied onto the clipboard.", "");
        exportString = exportString.trim();
        theString = exportString;
        System.out.println(theString);
    }

    @Test
    public void execute_exportedStringIsPersonClass() throws CommandException {

        byte[] data = Base64.getDecoder().decode(theString);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Person p = (Person) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            fail("Unable to find Person Class");
        }

    }

    @Test
    public void execute_exportedStringIsBase64() throws CommandException {

        //corrupting the string
        theString+= "a";
        try {
            byte[] data = Base64.getDecoder().decode(theString);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("did not throw exception");


    }


}
