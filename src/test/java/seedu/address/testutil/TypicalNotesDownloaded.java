package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.NotesDownloaded;

/**
 * A class that help to get typical notes downloaded.
 */
public class TypicalNotesDownloaded {

    public static final String CS2100_NOTES = "CS2100 Introduction to Computer Architecture";
    public static final String CS2101_NOTES = "CS2101 Effective Communication for Computing Professionals";
    public static final String CS2102_NOTES = "CS2102 Introduction to Database Systems";
    public static final String CS2103_NOTES = "CS2103 Introduction to Software Engineering";
    public static final String CS2104_NOTES = "CS2104 Introduction to Computer Engineering";
    public static final String CS2105_NOTES = "CS2105 Introduction to Networks";
    public static final String CS2106_NOTES = "CS2106 Introduction to Operating Systems";
    public static final String CS2107_NOTES = "CS2107 Introduction to Information Security";

    // Manually added
    public static final String CS3100_NOTES = "CS3100 Introduction to Artificial Intelligence";
    public static final String CS3235_NOTES = "CS3235 Advanced Information Security";
    public static final String CS5240_NOTES = "CS5240 Quantum Computing and SuperComputers";

    private TypicalNotesDownloaded() {} // prevents instantiation

    /**
     * Returns an {@code NotesDownloaded} with all the typical notes downloaded.
     */
    public static NotesDownloaded getTypicalNotesDownloaded() {
        NotesDownloaded nd = new NotesDownloaded();
        for (String notes : getTypicalNotes()) {
            nd.addNotes(notes);
        }
        return nd;
    }

    /**
     * Returns an {@code NotesDownloaded} with all the notes that are not typically in the
     * set returned by {@code getTypicalNotesDownloaded}.
     */
    public static NotesDownloaded getDifferentNotesDownloaded() {
        NotesDownloaded nd = new NotesDownloaded();
        for (String notes : getDifferentNotes()) {
            nd.addNotes(notes);
        }
        return nd;
    }

    public static List<String> getTypicalNotes() {
        return new ArrayList<>(Arrays.asList(CS2100_NOTES, CS2101_NOTES, CS2102_NOTES, CS2103_NOTES, CS2104_NOTES,
                                                CS2105_NOTES, CS2106_NOTES, CS2107_NOTES));
    }

    public static List<String> getDifferentNotes() {
        return new ArrayList<>(Arrays.asList(CS3100_NOTES, CS3235_NOTES, CS5240_NOTES));
    }
}
