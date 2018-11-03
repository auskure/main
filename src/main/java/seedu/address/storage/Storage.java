package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.NotesDownloadEvent;
import seedu.address.commons.events.model.NotesEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage, NotesDownloadStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleAddressBookChangedEvent(AddressBookChangedEvent abce);

    @Override
    Path getNotesFilePath();

    @Override
    void deleteAllNotes() throws IOException;

    @Override
    void relocateNotes(String moduleName) throws IOException;

    @Override
    void unzipNotes(String moduleName) throws IOException;

    /**
     * Depending on the type of the event, this executes operations on notes currently stored in your computer
     * Raises {@link IOException} if there was an error during saving.
     */
    void handleNotesManipulationEvent(NotesEvent notesEvent) throws IOException;

    /**
     * Depending on the type of the event, this executes operations on new notes downloaded
     * Raises {@link IOException} if there was an error during saving.
     */
    void handleNotesDownloadedEvent(NotesDownloadEvent notesDownloadEvent) throws IOException;
}
