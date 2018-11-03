package seedu.address.model.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class TimeSlots implements Serializable {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public static final String[] SET_VALUES = new String[] {"8am    ", "9am    ", "10am   ", "11am    ", "12pm   ",
        "1pm    ", "2pm     ", "3pm     ", "4pm    ", "5pm     ", "6pm    ", "7pm"};
    private static final String[] mon = new String[] {"free", "free", "free", "free", "free"
        , "free", "free", "free", "free", "free", "free", "free", "free", "free", "free"};
    public static final String[] tue = new String[] {"free", "free", "free", "free", "free", "free", "free", "free",
        "free", "free", "free", "free", "free", "free", "free"};

    public static final String[] wed = new String[] {"free", "free", "free", "free", "free", "free", "free", "free",
        "free", "free", "free", "free", "free", "free", "free"};
    public static final String[] thu = new String[] {"free", "free", "free", "free", "free", "free", "free", "free",
        "free", "free", "free", "free", "free", "free", "free"};
    public static final String[] fri = new String[] {"free", "free", "free", "free", "free", "free", "free", "free",
        "free", "free", "free", "free", "free", "free", "free"};
    private static final String[] sampleTue = new String[] {"CS2040c", "CS2040c", "ST2332", "ST2332", "free", "free",
        "CS2107", "CS2107", "free", "free", "free", "free"};
    private static final String[] sampleWed = new String[] {"CS2101", "CS2101", "free", "free", "GES1041",
        "GES1041", "free", "free", "CS2113", "CS2113", "free", "free"};
    private static final String[] sampleThu = new String[] {"free", "free", "MA1521", "MA1521", "free", "free",
        "CS2040c", "CS2040c", "free", "free", "free", "free"};
    private static final String[] sampleFri = new String[] {"free", "free", "CS2107", "free", "free",
        "free", "CS2101", "free", "free", "free", "free", "free"};


    public String timeslot;


    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public TimeSlots(String timeslot) {
        requireNonNull(timeslot);
        this.timeslot = timeslot;
    }

    public static Map<String, List<TimeSlots>> initTimeSlots() {
        Map<String, List<TimeSlots>> inittimeslot = new HashMap<>();
        inittimeslot.put("mon", getMon());
        inittimeslot.put("tue", getTue());
        inittimeslot.put("wed", getWed());
        inittimeslot.put("thu", getThu());
        inittimeslot.put("fri", getFri());

        return inittimeslot;
    }

    public static Map<String, List<TimeSlots>> sampleTimeSlots() {
        Map<String, List<TimeSlots>> sampletimeslot = new HashMap<>();
        sampletimeslot.put("mon", getMon());
        sampletimeslot.put("tue", getSampleTue());
        sampletimeslot.put("wed", getSampleWed());
        sampletimeslot.put("thu", getSampleThu());
        sampletimeslot.put("fri", getSampleFri());

        return sampletimeslot;
    }

    public static String[] getHeader() {
        return SET_VALUES;
    }

    public static List<TimeSlots> getMon() {
        List<TimeSlots> monTimeSlots = new ArrayList<>();
        for (String it : mon) {
            monTimeSlots.add(new TimeSlots(it));
        }
        return monTimeSlots;
    }

    public static List<TimeSlots> getTue() {
        List<TimeSlots> tueTimeSlots = new ArrayList<>();
        for (String it : tue) {
            tueTimeSlots.add(new TimeSlots(it));
        }
        return tueTimeSlots;
    }

    public static List<TimeSlots> getWed() {
        List<TimeSlots> wedTimeSlots = new ArrayList<>();
        for (String it : wed) {
            wedTimeSlots.add(new TimeSlots(it));
        }
        return wedTimeSlots;
    }

    public static List<TimeSlots> getThu() {
        List<TimeSlots> thuTimeSlots = new ArrayList<>();
        for (String it : thu) {
            thuTimeSlots.add(new TimeSlots(it));
        }
        return thuTimeSlots;
    }

    public static List<TimeSlots> getFri() {
        List<TimeSlots> friTimeSlots = new ArrayList<>();
        for (String it : fri) {
            friTimeSlots.add(new TimeSlots(it));
        }
        return friTimeSlots;
    }

    public static List<TimeSlots> getSampleTue() {
        List<TimeSlots> tueTimeSlots = new ArrayList<>();
        for (String it : sampleTue) {
            tueTimeSlots.add(new TimeSlots(it));
        }
        return tueTimeSlots;
    }

    public static List<TimeSlots> getSampleWed() {
        List<TimeSlots> wedTimeSlots = new ArrayList<>();
        for (String it : sampleWed) {
            wedTimeSlots.add(new TimeSlots(it));
        }
        return wedTimeSlots;
    }

    public static List<TimeSlots> getSampleThu() {
        List<TimeSlots> thuTimeSlots = new ArrayList<>();
        for (String it : sampleThu) {
            thuTimeSlots.add(new TimeSlots(it));
        }
        return thuTimeSlots;
    }

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
