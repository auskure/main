package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.IsNotSelfOrMergedPredicate;
import seedu.address.model.person.IsSelfPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TimeSlots;

/**
 * Edits an existing person's timetable in the address book.
 */
public class ChangeTimeSlotCommand extends Command {

    public static final String COMMAND_WORD = "change";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the selected time slot "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX "
            + "DAY(mon, tue, wed, thu, fri) "
            + "TIME(8am, 9am, 10am, 11am, 12pm, 1pm, 2pm, 3pm, 4pm, 5pm, 6pm, 7pm) "
            + "Activity "
            + "Example: " + COMMAND_WORD + " 1 " + "mon "
            + "8am "
            + "CS2107";


    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Time slot changed: %1$s";
    public static final String MESSAGE_EDIT_SELF_SUCCESS = "Time slot changed: Self";
    public static final String MESSAGE_NOTHING_CHANGED = "No time slot was changed";
    public static final String MESSAGE_INVALID_DAY = "Invalid Day. ";
    public static final String MESSAGE_INVALID_TIME = "Invalid Time. ";

    private final String reference;
    private final String[] actions;

    public ChangeTimeSlotCommand(String index, String[] actions) {
        requireNonNull(actions);
        requireNonNull(index);

        this.reference = index;
        this.actions = actions;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToChange;
        try {
            int index = Integer.parseInt(reference);
            lastShownList = ((ObservableList<Person>) lastShownList).filtered(new IsNotSelfOrMergedPredicate());

            if (index - 1 >= lastShownList.size() || index < 1) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            personToChange = lastShownList.get(index - 1);
        } catch (NumberFormatException nfe) {
            if (!reference.equalsIgnoreCase("self")) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            int index = 0;
            lastShownList = ((ObservableList<Person>) lastShownList).filtered(new IsSelfPredicate());
            personToChange = lastShownList.get(index);
        }

        Map<String, List<TimeSlots>> timeSlots = personToChange.getTimeSlots();
        Map<String, List<TimeSlots>> changedTimeSlots = new HashMap<>();
        if (createNewTimetable(timeSlots) != null) {
            changedTimeSlots = createNewTimetable(timeSlots);
        } else {
            throw new CommandException(MESSAGE_NOTHING_CHANGED);
        }
        Person newPerson = new Person(personToChange.getName(), personToChange.getPhone(), personToChange.getEmail(),
                personToChange.getAddress(), personToChange.getTags(), personToChange.getEnrolledModules(),
                changedTimeSlots);


        model.updatePerson(personToChange, newPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();

        if (!reference.equalsIgnoreCase("self")) {
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, newPerson));
        } else {
            return new CommandResult(String.format(MESSAGE_EDIT_SELF_SUCCESS));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeTimeSlotCommand // instanceof handles nulls
                && reference.equals(((ChangeTimeSlotCommand) other).reference)
                && Arrays.equals(actions, (((ChangeTimeSlotCommand) other).actions)));
    }

    /**
     * Copies a person's timetable.
     */
    private Map<String, List<TimeSlots>> createNewTimetable(Map<String, List<TimeSlots>> timeSlots) {
        Map<String, List<TimeSlots>> changedTimeSlots = new HashMap<>();
        String[] days = {"mon", "tue", "wed", "thu", "fri"};
        String day = null;
        String time = null;
        Boolean didTimetableChange = false;
        for (String d : days) {
            List<TimeSlots> toAdd;
            toAdd = copyDay(timeSlots.get(d));
            changedTimeSlots.put(d, toAdd);
        }

        for (int i = 1; i < actions.length; i++) {

            String activity;
            if (i % 3 == 1) {
                day = actions[i];
            }
            if (i % 3 == 2) {
                time = actions[i];
            }
            if (i % 3 == 0) {
                activity = actions[i];

                if (!changedTimeSlots.get(day).get(changeTimeToIndex(time)).toString().equalsIgnoreCase(activity)) {
                    changedTimeSlots.get(day).set(changeTimeToIndex(time), new TimeSlots(activity));
                    didTimetableChange = true;
                }

            }
        }
        if (didTimetableChange) {
            return changedTimeSlots;
        } else {
            return null;
        }
    }

    /**
     * Copies the timeslots in a day of a timetable.
     */
    private List<TimeSlots> copyDay(List<TimeSlots> toCopy) {
        List<TimeSlots> finalSlots = new ArrayList<>();
        for (TimeSlots toAdd : toCopy) {
            finalSlots.add(toAdd);
        }
        return finalSlots;
    }

    /**
     * Converts from a time to its corresponding index.
     */
    private int changeTimeToIndex(String time) {
        int index;
        if (time.equalsIgnoreCase("8am")) {
            index = 0;
        } else if (time.equalsIgnoreCase("9am")) {
            index = 1;
        } else if (time.equalsIgnoreCase("10am")) {
            index = 2;
        } else if (time.equalsIgnoreCase("11am")) {
            index = 3;
        } else if (time.equalsIgnoreCase("12pm")) {
            index = 4;
        } else if (time.equalsIgnoreCase("1pm")) {
            index = 5;
        } else if (time.equalsIgnoreCase("2pm")) {
            index = 6;
        } else if (time.equalsIgnoreCase("3pm")) {
            index = 7;
        } else if (time.equalsIgnoreCase("4pm")) {
            index = 8;
        } else if (time.equalsIgnoreCase("5pm")) {
            index = 9;
        } else if (time.equalsIgnoreCase("6pm")) {
            index = 10;
        } else if (time.equalsIgnoreCase("7pm")) {
            index = 11;
        } else {
            return 11;
        }
        return index;
    }
}
