package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.commons.core.Messages;
import seedu.address.model.person.*;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import java.io.ByteArrayOutputStream;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import static java.util.Objects.requireNonNull;


/**
 * Export a person into string
 */

public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Export the person so as "
            + "to import it into another system.\n"
            + "Parameters: INDEX (must be positive integer )\n"
            + "Example: " + COMMAND_WORD + " " + "1";

    private final Index index;

    public ExportCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    /**
     * Calls getSerializedString to get the Base64 String for the person selected through the index
     * The generated string is copied to the user's clipboard for easy copy and pasting
     *
     * @throws CommandException if the index given is invalid
     */

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        requireNonNull(model);
        List<Person> filteredPersonList = model.getFilteredPersonList();
        filteredPersonList = ((ObservableList<Person>) filteredPersonList).filtered(new IsNotSelfOrMergedPredicate());

        if (index.getZeroBased() >= filteredPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person myPerson = filteredPersonList.get(index.getZeroBased());
        String theString = getSerializedString(myPerson);
        StringSelection ss = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(ss, null);

        String output = theString;
        output += "\n";
        output += "The string has been copied onto the clipboard.";
        return new CommandResult(output);

    }

    /**
     * Generates the Base64 String for the Person object
     */
    private String getSerializedString(Serializable o) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (Exception e) {
            return "Error getting string, please try again";
        }




    }



}



