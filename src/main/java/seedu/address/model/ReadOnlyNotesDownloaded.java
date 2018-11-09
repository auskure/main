package seedu.address.model;

import java.util.Set;

/**
 * Unmodifiable view of the list of downloaded modules
 */
public interface ReadOnlyNotesDownloaded {

    /**
     * Returns an unmodifiable view of the modules set.
     * This set will not contain any duplicate persons.
     */
    public Set<String> getNotesList();

}
