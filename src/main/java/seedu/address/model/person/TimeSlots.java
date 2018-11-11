package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class TimeSlots implements Serializable {


    private static final String[] SET_VALUES = {"8am    ", "9am    ", "10am   ", "11am    ", "12pm   ",
        "1pm    ", "2pm     ", "3pm     ", "4pm    ", "5pm     ", "6pm    ", "7pm"};
    private static final String[] free = {"free", "free", "free", "free", "free",
        "free", "free", "free", "free", "free", "free", "free"};
    private static final String[] sampleTue = {"CS2040c", "CS2040c", "ST2332", "ST2332", "free", "free",
        "CS2107", "CS2107", "free", "free", "free", "free"};
    private static final String[] sampleWed = {"CS2101", "CS2101", "free", "free", "GES1041",
        "GES1041", "free", "free", "CS2113", "CS2113", "free", "free"};
    private static final String[] sampleThu = {"free", "free", "MA1521", "MA1521", "free", "free",
        "CS2040c", "CS2040c", "free", "free", "free", "free"};
    private static final String[] sampleFri = {"free", "free", "CS2107", "free", "free",
        "free", "CS2101", "free", "free", "free", "free", "free"};

    private String timeslot;

    /**
     * Constructs a {@code Tag}.
     *
     * @param timeslot A valid timeslot.
     */
    public TimeSlots(String timeslot) {
        requireNonNull(timeslot);
        this.timeslot = timeslot;
    }

    /**
     * Creates a map of lists of free time slots
     */
    public static Map<String, List<TimeSlots>> initTimeSlots() {
        String[] days = {"mon", "tue", "wed", "thu", "fri"};
        Map<String, List<TimeSlots>> inittimeslot = new HashMap<>();
        for (String day : days) {
            inittimeslot.put(day, getFreeTimeSlots());
        }

        return inittimeslot;
    }

    /**
     * Creates a map of lists of sample time slots
     */
    public static Map<String, List<TimeSlots>> sampleTimeSlots() {
        Map<String, List<TimeSlots>> sampletimeslot = new HashMap<>();
        sampletimeslot.put("mon", getFreeTimeSlots());
        sampletimeslot.put("tue", getSampleTue());
        sampletimeslot.put("wed", getSampleWed());
        sampletimeslot.put("thu", getSampleThu());
        sampletimeslot.put("fri", getSampleFri());

        return sampletimeslot;
    }

    /**
     * Gets header for timetable for the UI.
     */
    public static String[] getHeader() {
        return SET_VALUES;
    }

    /**
     * Creates a list of free timeslots
     */
    public static List<TimeSlots> getFreeTimeSlots() {
        List<TimeSlots> freeTimeSlots = new ArrayList<>();
        for (String it : free) {
            freeTimeSlots.add(new TimeSlots(it));
        }
        return freeTimeSlots;
    }

    /**
     * Creates a list of sample time slots for tuesday
     */
    public static List<TimeSlots> getSampleTue() {
        List<TimeSlots> tueTimeSlots = new ArrayList<>();
        for (String it : sampleTue) {
            tueTimeSlots.add(new TimeSlots(it));
        }
        return tueTimeSlots;
    }

    /**
     * Creates a list of sample time slots for wednesday
     */
    public static List<TimeSlots> getSampleWed() {
        List<TimeSlots> wedTimeSlots = new ArrayList<>();
        for (String it : sampleWed) {
            wedTimeSlots.add(new TimeSlots(it));
        }
        return wedTimeSlots;
    }

    /**
     * Creates a list of sample time slots for thursday
     */
    public static List<TimeSlots> getSampleThu() {
        List<TimeSlots> thuTimeSlots = new ArrayList<>();
        for (String it : sampleThu) {
            thuTimeSlots.add(new TimeSlots(it));
        }
        return thuTimeSlots;
    }

    /**
     * Creates a list of sample time slots for friday
     */
    public static List<TimeSlots> getSampleFri() {
        List<TimeSlots> friTimeSlots = new ArrayList<>();
        for (String it : sampleFri) {
            friTimeSlots.add(new TimeSlots(it));
        }
        return friTimeSlots;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */

    @Override
    public int hashCode() {
        return timeslot.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return timeslot;
    }

}
