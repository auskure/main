package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

public class FreeCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory;

    @Before
    public void setUp() throws CommandException {
        model = new ModelManager(TypicalPersons.getFreeAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
        expectedModel = new ModelManager(TypicalPersons.getFreeAddressBook(), getTypicalNotesDownloaded(),
            new UserPrefs());
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_freeWholeDay() {

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        FreeCommand fc = new FreeCommand(list);
        // thursday
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 10, 8);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        fc.setCurrentTime(cal);



        CommandTestUtil.assertCommandSuccess(fc, model, commandHistory,
            "The next available timeslot for John Doe is : thu 8:00 AM - 8:00 PM", expectedModel);
    }

    @Test
    public void execute_freeMiddleOfDay() {

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        FreeCommand fc = new FreeCommand(list);
        //thursday
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 10, 8);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 34);

        fc.setCurrentTime(cal);



        CommandTestUtil.assertCommandSuccess(fc, model, commandHistory,
            "The next available timeslot for John Doe is : Thu 12:34 PM - 8:00 PM", expectedModel);
    }

    @Test
    public void execute_freeMiddleOfDay_self() {

        ArrayList<String> list = new ArrayList<>();
        list.add("self");
        FreeCommand fc = new FreeCommand(list);
        //thursday
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 10, 8);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 34);

        fc.setCurrentTime(cal);

        CommandTestUtil.assertCommandSuccess(fc, model, commandHistory,
            "The next available timeslot for John Doe is: Thu 12:34 PM - 8:00 PM", expectedModel);
    }

    @Test
    public void execute_freeWrapToMon() throws CommandException {

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        FreeCommand fc = new FreeCommand(list);
        Calendar cal = Calendar.getInstance();
        //saturday
        cal.set(2018, 10, 10);
        fc.setCurrentTime(cal);

        CommandResult result = fc.execute(model, commandHistory);
        String theString = result.feedbackToUser;
        if (!theString.contains(": mon")) {
            fail("did not wrap to monday");
        }

    }

    @Test
    public void execute_busyMiddleOfDay() throws CommandException {

        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        FreeCommand fc = new FreeCommand(list);

        //thursday
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 10, 8);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 34);

        fc.setCurrentTime(cal);

        String[] action = new String[4];
        action[0] = "1";
        action[1] = "thu";
        action[2] = "12pm";
        action[3] = "CS1234";

        //using changetimeslotcommand to change time slot
        ChangeTimeSlotCommand change = new ChangeTimeSlotCommand("1", action);
        change.execute(model, commandHistory);



        CommandResult result = fc.execute(model, commandHistory);
        String theString = result.feedbackToUser;
        //12.34 pm is busy, jump to 1pm
        assertEquals("The next available timeslot for John Doe is : thu 1:00 PM - 8:00 PM", theString);

    }

}
