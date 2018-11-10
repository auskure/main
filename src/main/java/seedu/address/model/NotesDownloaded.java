package seedu.address.model;
//@@author auskure
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by the nature of the Set Data Structure)
 */
public class NotesDownloaded implements ReadOnlyNotesDownloaded {

    private final Set<String> notesList;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        notesList = new TreeSet<>();
    }

    public NotesDownloaded() {}

    /**
     * Creates a notesDownloaded, using the notes the {@code toBeCopied}
     */
    public NotesDownloaded(ReadOnlyNotesDownloaded toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Creates a notesDownloaded object, which keeps track of the notes downloaded,
     * using the notes the {@code toBeCopied}
     */
    public NotesDownloaded(Set<String> toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// notes overwrite operations

    /**
     * Replaces the contents of the notes list with {@code notes}.
     */
    public void setNotes(Set<String> notes) {
        this.notesList.clear();
        this.notesList.addAll(notes);
    }

    /**
     * Resets the existing data of this {@code NotesDownloaded} with {@code newData}.
     */
    public void resetData(ReadOnlyNotesDownloaded newData) {
        requireNonNull(newData);

        setNotes(newData.getNotesList());
    }

    /**
     * Resets the existing data of this {@code NotesDownloaded} with {@code newData}.
     */
    public void resetData(Set<String> newData) {
        requireNonNull(newData);

        setNotes(newData);
    }

    /**
     * Clears the existing data of this {@code NotesDownloaded}.
     */
    public void clear() {
        notesList.clear();
    }

    //// notes-level operations

    /**
     * Adds new notes to the NotesDownloaded
     * If the notes already exist, no new entry will be created.
     */
    public void addNotes(String n) {
        notesList.add(n);
    }

    /**
     * Removes {@code moduleCodes} from this {@code notes}.
     */
    public void deleteSelectedNotes(Set<String> moduleCodes) {
        for (String tempName : moduleCodes) {
            for (Iterator<String> iterator = notesList.iterator(); iterator.hasNext();) {
                String tempNotes = iterator.next();
                if (tempNotes.contains(tempName)) {
                    iterator.remove();
                }
            }
        }
    }

    //// util methods
    @Override
    public String toString() {
        return notesList.toString();
    }

    @Override
    public Set<String> getNotesList() {
        return Collections.unmodifiableSet(notesList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NotesDownloaded // instanceof handles nulls
                && notesList.equals(((NotesDownloaded) other).notesList));
    }

    @Override
    public int hashCode() {
        return notesList.hashCode();
    }
}
