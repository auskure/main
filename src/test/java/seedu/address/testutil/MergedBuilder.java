package seedu.address.testutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.TimeSlots;
import seedu.address.model.tag.Tag;

public class MergedBuilder {
    private Person mergedPerson;

    /**
     * Takes a list of Person objects and creates a merged Person contact.
     */
    public MergedBuilder(List<Person> personsToMerge, String groupName) {
        for (int j = 0; j < personsToMerge.size() - 1; j++) {
            personsToMerge.set(j + 1, mergeTimetables(personsToMerge.get(j), personsToMerge.get(j + 1), j, groupName));
        }
        this.mergedPerson = personsToMerge.get(personsToMerge.size() - 1);
    }

    /**
     * Returns the merged Person.
     */
    public Person getMergedPerson() {
        return this.mergedPerson;
    }

    /**
     * Merges 2 people into a single person with a merged timetable
     */
    private Person mergeTimetables(Person person1, Person person2, int index, String groupName) {
        Name mergedName = new Name(groupName);
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
        Map<String, seedu.address.model.enrolledmodule.EnrolledModule> enrolledClassMap = new TreeMap<>();


        return new Person(mergedName, phone, email, address, mergedTags, enrolledClassMap,
                mergedSlots);


    }

    /**
     * Creates a new merged timetable from 2 timetables.
     */
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

    /**
     * Compares 2 lists of time slots and returns a merged list.
     */
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
