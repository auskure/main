package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.Base64;

import static java.util.Objects.requireNonNull;


/**
 * Import a person from a string
 */

public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": import the person into the system.\n"
        + "Parameters: STRING (the Base64 string)\n"
        + "Example: " + COMMAND_WORD + " " + "rO0ABXNyACFzZWVkdS5hZGRyZXNzLm1vZGVsLnB...";

    private final String personString;

    private static final String MESSAGE_SUCCESS = "Import successful";
    private static final String MESSAGE_FAILED = "Failed to import";
    private static final String MESSAGE_DUPLICATE = "Failed to import, duplicate person exist";

    public ImportCommand(String input) {
        requireNonNull(input);
        this.personString = input.trim();
    }

    /**
     * Reads the input Base64 String and serialize a person object and add it into the addressbook
     *
     * @throws DuplicatePersonException if the person that the user is trying to add already exists
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        requireNonNull(model);
        Person p = getSerializedPerson(personString);

        try {
            model.addPerson(p);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE);
        }

        model.commitAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);

    }

    /**
     * Serialize a Person object from the given Base64 String
     *
     * @throws CommandException if the given Base64 string is bad and is unable to serialize an object
     */
    private Person getSerializedPerson(String s) throws CommandException {
        try {
            byte[] data = Base64.getDecoder().decode(s);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Person p = (Person) ois.readObject();
            return p;
        } catch (Exception e) {
            throw new CommandException(MESSAGE_FAILED);
        }


    }


}



