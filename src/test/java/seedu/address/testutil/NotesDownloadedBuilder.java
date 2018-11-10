package seedu.address.testutil;

import seedu.address.model.NotesDownloaded;

/**
 * A utility class to help with building NotesDownloaded objects.
 * Example usage: <br>
 *     {@code NotesDownloaded nd = new NotesDownloadedBuilder().withNotes("John", "Doe").build();}
 */
public class NotesDownloadedBuilder {

    private NotesDownloaded notesDownloaded;

    public NotesDownloadedBuilder() {
        notesDownloaded = new NotesDownloaded();
    }

    /**
     * Adds a new {@code notes} to the {@code NotesDownloaded} that we are building.
     */
    public NotesDownloadedBuilder withNotes(String notes) {
        notesDownloaded.addNotes(notes);
        return this;
    }

    public NotesDownloaded build() {
        return notesDownloaded;
    }
}
