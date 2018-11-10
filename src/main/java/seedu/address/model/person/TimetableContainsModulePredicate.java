package seedu.address.model.person;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import seedu.address.model.tag.Tag;


//@@E0201942

/**
 * Tests that a {@code Person}'s {@code Tag} matches any of the keywords given.
 */
public class TimetableContainsModulePredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TimetableContainsModulePredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        for (Tag tags : person.getTags()) {
            if (tags.toString().equalsIgnoreCase("[merged]")) {
                return true;
            }
        }
        String[] days = {"mon", "tue", "wed", "thu", "fri"};
        Map<String, List<TimeSlots>> timetable = person.getTimeSlots();
        boolean timeCheck = true;
        int modCheck = 0;
        int count = keywords.size();
        for (int i = 0; i < keywords.size(); i++) {
            for (String day : days) {
                if (keywords.get(i).equalsIgnoreCase(day)) {
                    List<TimeSlots> daySlots = timetable.get(day);
                    int timeIndex = Integer.parseInt(keywords.get(i + 1));
                    TimeSlots checkSlot = daySlots.get(timeIndex);
                    count = count - 2;
                    if (!checkSlot.toString().equalsIgnoreCase("free")) {
                        timeCheck = false;
                    }
                }
            }
        }
        for (String check : keywords) {
            for (String day : days) {
                List<TimeSlots> daySlots = timetable.get(day);
                for (TimeSlots module : daySlots) {
                    if (check.equalsIgnoreCase(module.toString())) {
                        modCheck++;
                        break;
                    }
                }
            }
        }

        return (modCheck == count && timeCheck);
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TimetableContainsModulePredicate
                // instanceof handles nulls
                && keywords.equals(((TimetableContainsModulePredicate) other).keywords));
        // state check
    }

}

