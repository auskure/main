package seedu.address.logic.commands;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
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

public class ShareCommand extends Command {

    public static final String COMMAND_WORD = "share";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Export a contact's timetable "
        + "to import it into another address book.\n"
        + "Parameters: PRIVACY(public, private) INDEX (must be positive integer )\n"
        + "Example: " + COMMAND_WORD + "public " + " " + "1";

    private final String index;
    private final String privacy;

    public ShareCommand(String privacy, String index) {
        requireNonNull(index);
        this.index = index;
        this.privacy = privacy;
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
        Person myPerson;
        if (index.equalsIgnoreCase("self")) {
            filteredPersonList = ((ObservableList<Person>) filteredPersonList).filtered(new IsSelfPredicate
                ());
            myPerson = filteredPersonList.get(0);
        } else {
            int num;
            try {
                num = Integer.parseInt(index) - 1;
            } catch (NumberFormatException nfe) {
                throw new CommandException(String.format(MESSAGE_USAGE));
            }
            filteredPersonList = ((ObservableList<Person>) filteredPersonList).filtered(new IsNotSelfOrMergedPredicate());

            if (num >= filteredPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            myPerson = filteredPersonList.get(num);
        }

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
    private String getSerializedString(Person source) {
        Map<String, List<TimeSlots>> timeSlots = source.getTimeSlots();
        String[] days = {"mon", "tue", "wed", "thu", "fri"};
        List<String> exportStrings = new ArrayList<>();
        String finalExport = " ";
        for (String day : days) {
            List<TimeSlots> daySlots = timeSlots.get(day);
            for (int i = 0; i < 12; i++) {
                TimeSlots activity;
                if (privacy.equalsIgnoreCase("public")) {
                    activity = daySlots.get(i);
                } else {
                    activity = daySlots.get(i);
                    if (!activity.toString().equalsIgnoreCase("free")) {
                        activity = new TimeSlots("busy");
                    }
                }
                String stringToInput = day + " " + indexToTime(i) + " " + activity.toString() + " ";
                exportStrings.add(stringToInput);
            }
        }
        for (int i = 0; i < exportStrings.size(); i++) {
            finalExport = finalExport + exportStrings.get(i);
        }
        return finalExport;
    }

    private String indexToTime(int index) {
        String[] timings = {"8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm"};
        return timings[index];
    }

}
