package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents the storage component for the downloaded notes
 */
public interface NotesDownloadStorage {

    /**
     * Returns the file path of the UserPrefs data file.
     */
    Path getNotesFilePath();

    void deleteAllNotes() throws IOException;

    void relocateNotes(String moduleName) throws IOException;

    void unzipNotes(String moduleName) throws IOException;

}
