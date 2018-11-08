package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FREE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.enrolledmodule.EnrolledModule;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.IsNotSelfOrMergedPredicate;
import seedu.address.model.person.IsSelfPredicate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TimeSlots;
import seedu.address.model.tag.Tag;

/**
 * Checks for common free time slot for 1 or multiple people
 */

public class FreeCommand extends Command {

    public static final String COMMAND_WORD = "free";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays the next available time slot for persons "
        + "listed by their index number.\n"
        + "Parameter: " + PREFIX_FREE + "[INDEX]"
        + " for all contacts you would like to compare.\n"
        + "Example: " + COMMAND_WORD + " " + PREFIX_FREE + "1 " + PREFIX_FREE + "2 ";

    private static final String MESSAGE_NOT_FREED = "There are no common free time slots found.";
    private final List<String> indices;
    private final String[] days = {"mon", "tue", "wed", "thu", "fri"};

    public FreeCommand(List<String> indices) {
        requireNonNull(indices);
        this.indices = indices;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        lastShownList = ((ObservableList<Person>) lastShownList).filtered(new IsNotSelfOrMergedPredicate());

        // Check if inputs are valid
        // Checking could not be done in parser as Model is only available here
        for (String index : indices) {
            if (index.equalsIgnoreCase("self")) {
                continue;
            }

            int currentIndex = 0;
            try {
                currentIndex = Integer.parseInt(index);
            } catch (NumberFormatException e) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            if (currentIndex > lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // remove the first person from the list as we have reference to him in personFirst
        Person personFirst;
        if (indices.get(0).equalsIgnoreCase("self")) {
            personFirst = model.getFilteredPersonList().filtered(new IsSelfPredicate()).get(0);
        } else {
            personFirst = lastShownList.get(Integer.parseInt(indices.get(0)) - 1);
        }
        indices.remove(0);

        // start generation of output string to user
        String outputToUser = "The next available timeslot for";
        outputToUser += " " + personFirst.getName() + ",";

        // if trying to find free slots for more than 1 person, use the merge algorithm to create a merged timetable to
        // find a common free slot(s)
        if (indices.size() > 0) {
            for (String x : indices) {
                Person personTemp;
                if (x.equalsIgnoreCase("self")) {
                    personTemp = model.getFilteredPersonList().filtered(new IsSelfPredicate()).get(0);
                } else {
                    personTemp = lastShownList.get(Integer.parseInt(x) - 1);
                }
                personFirst = mergeTimetables(personFirst, personTemp, 0);
                outputToUser += " " + personTemp.getName() + ",";
            }
        } else {
            // only finding 1 person free slot, so use the merge algorithm to compare both the same person
            // to change the time slots to free or busy tag
            personFirst = mergeTimetables(personFirst, personFirst, 0);
        }

        outputToUser = outputToUser.substring(0, outputToUser.length() - 1);
        outputToUser = outputToUser + " is: ";


        // if today is saturday or sunday, loop back to monday
        int dayToCheck;
        boolean isToday = true;
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY || LocalDate.now().getDayOfWeek()
            == DayOfWeek.SUNDAY) {
            dayToCheck = 0;
            isToday = false;
        } else if (LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY) {
            dayToCheck = 0;
        } else if (LocalDate.now().getDayOfWeek() == DayOfWeek.TUESDAY) {
            dayToCheck = 1;
        } else if (LocalDate.now().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
            dayToCheck = 2;
        } else if (LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY) {
            dayToCheck = 3;
        } else {
            dayToCheck = 4;
        }


        Calendar rightNow = Calendar.getInstance();
        int hourNow = rightNow.get(Calendar.HOUR_OF_DAY);
        if (hourNow >= 20) {
            // after 8pm, loop to next day
            dayToCheck++;
            dayToCheck %= 5;
            hourNow = 8;
            isToday = false;
        }

        List<Integer> listFoundSlot = new ArrayList<>();

        // loop for 6 days, the 6th day is to look for time that is on this day but hours before current hour
        for (int i = 0; i < 6; i++) {

            int timeSlotIndex = 0;
            int currentHourIndex = hourNow - 8;
            listFoundSlot.clear();
            boolean found = false;
            int prevIndex = -1;
            List<TimeSlots> timeslotToCheck = personFirst.getTimeSlots().get(days[dayToCheck]);

            for (TimeSlots x : timeslotToCheck) {
                if (x.toString().equalsIgnoreCase("0")) {

                    // do not add hours that are before current time if it is today
                    if ((isToday && (!(timeSlotIndex < currentHourIndex))) || (!isToday)) {

                        // finding consecutive slots
                        if (found == false) {
                            found = true;
                            prevIndex = timeSlotIndex;
                        } else {
                            if (timeSlotIndex - prevIndex != 1) {
                                break;
                            } else {
                                prevIndex = timeSlotIndex;
                            }
                        }
                        listFoundSlot.add(timeSlotIndex);

                    }

                }
                timeSlotIndex++;
            }


            String timeFrom;
            String timeTo;
            DateFormat sdf = new SimpleDateFormat("EEE hh:mm aa");
            if (listFoundSlot.size() > 0) {
                // found a free slot
                int foundHour = listFoundSlot.get(0) + 8;
                int endHour = (listFoundSlot.get(listFoundSlot.size() - 1) + 9);

                if (!(isToday && (foundHour == hourNow))) {
                    isToday = false;
                }
                if (isToday) {
                    timeFrom = sdf.format(rightNow.getTime());
                    timeTo = getTimeFormatted(endHour);
                    outputToUser += timeFrom + " - " + timeTo;
                    return new CommandResult(outputToUser);
                } else {
                    timeFrom = getTimeFormatted(foundHour);
                    timeTo = getTimeFormatted(endHour);
                    outputToUser += days[dayToCheck] + " " + timeFrom + " - " + timeTo;
                }
                return new CommandResult(outputToUser);
            }

            dayToCheck++;
            dayToCheck %= 5;
            hourNow = 8;
            isToday = false;
        }


        // if after 6 loops and we are not able to find a common timeslot, returns message
        // to inform user that there is no common free timeslot
        return new CommandResult(MESSAGE_NOT_FREED);

    }

    /*
     * Formats the time into readable String
     *
     * @param Hour
     */

    private String getTimeFormatted(int hours) {
        String amPm = "AM";
        if (hours > 12) {
            hours %= 12;
            amPm = "PM";
        } else if (hours == 12) {
            amPm = "PM";
        }
        return hours + ":00 " + amPm;
    }

    private Person mergeTimetables(Person person1, Person person2, int index) {
        Name mergedName = new Name("a");
        Phone phone = new Phone("99999999");
        Email email = new Email("notimportant@no");
        Address address;
        if (index == 0) {
            address = new Address(person1.getName().toString() + ", " + person2.getName().toString());
        } else {
            address = new Address(person1.getAddress().toString() + ", " + person2.getName().toString());
        }
        Set<Tag> mergedTags = new HashSet<>();
        mergedTags.add(new Tag("merged"));
        Map<String, List<TimeSlots>> mergedSlots = mergeTimeSlots(person1.getTimeSlots(), person2.getTimeSlots());
        Map<String, EnrolledModule> enrolledClassMap = new TreeMap<>();


        return new Person(mergedName, phone, email, address, mergedTags, enrolledClassMap,
            mergedSlots);


    }

    private Map<String, List<TimeSlots>> mergeTimeSlots(Map<String, List<TimeSlots>> slots1,
                                                        Map<String, List<TimeSlots>> slots2) {
        TimeSlots[] mon1 = slots1.get("mon").toArray(new TimeSlots[0]);
        TimeSlots[] mon2 = slots2.get("mon").toArray(new TimeSlots[0]);
        TimeSlots[] tue1 = slots1.get("tue").toArray(new TimeSlots[0]);
        TimeSlots[] tue2 = slots2.get("tue").toArray(new TimeSlots[0]);
        TimeSlots[] wed1 = slots1.get("wed").toArray(new TimeSlots[0]);
        TimeSlots[] wed2 = slots2.get("wed").toArray(new TimeSlots[0]);
        TimeSlots[] thu1 = slots1.get("thu").toArray(new TimeSlots[0]);
        TimeSlots[] thu2 = slots2.get("thu").toArray(new TimeSlots[0]);
        TimeSlots[] fri1 = slots1.get("fri").toArray(new TimeSlots[0]);
        TimeSlots[] fri2 = slots2.get("fri").toArray(new TimeSlots[0]);
        List<TimeSlots> finalMon;
        List<TimeSlots> finalTue;
        List<TimeSlots> finalWed;
        List<TimeSlots> finalThu;
        List<TimeSlots> finalFri;
        Map<String, List<TimeSlots>> finalSlots = new HashMap<>();

        finalMon = compareTimeSlots(mon1, mon2);
        finalTue = compareTimeSlots(tue1, tue2);
        finalWed = compareTimeSlots(wed1, wed2);
        finalThu = compareTimeSlots(thu1, thu2);
        finalFri = compareTimeSlots(fri1, fri2);


        finalSlots.put("mon", finalMon);
        finalSlots.put("tue", finalTue);
        finalSlots.put("wed", finalWed);
        finalSlots.put("thu", finalThu);
        finalSlots.put("fri", finalFri);
        return finalSlots;
    }

    List<TimeSlots> compareTimeSlots(TimeSlots[] day1, TimeSlots[] day2) {
        List<TimeSlots> finalDay = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if (day1[i].toString().equalsIgnoreCase("free")
                || day1[i].toString().equalsIgnoreCase("0")) {
                day1[i] = new TimeSlots("0");
            } else {
                try {
                    Integer.parseInt(day1[i].toString());
                } catch (NumberFormatException e) {
                    day1[i] = new TimeSlots("1");
                }
            }

            if (day2[i].toString().equalsIgnoreCase("free")) {
                day2[i] = new TimeSlots("0");
            } else {
                day2[i] = new TimeSlots("1");
            }
            String day1BusyCount = day1[i].toString();
            String day2BusyCount = day2[i].toString();
            int totalBusyCount = Integer.parseInt(day1BusyCount) + Integer.parseInt(day2BusyCount);
            String newBusyCount = Integer.toString(totalBusyCount);
            finalDay.add(new TimeSlots(newBusyCount));
        }
        return finalDay;
    }

}

