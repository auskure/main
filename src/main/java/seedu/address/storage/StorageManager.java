package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.NotesDownloadEvent;
import seedu.address.commons.events.model.NotesEvent;
import seedu.address.commons.events.model.NotesSelectedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.ClearNotesCommand;
import seedu.address.logic.commands.DeleteSelectNotesCommand;
import seedu.address.logic.commands.DownloadAllNotesCommand;
import seedu.address.logic.commands.DownloadSelectNotesCommand;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private NotesDownloadStorage notesDownloadStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage,
                          NotesDownloadStorage notesDownloadStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.notesDownloadStorage = notesDownloadStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    // ================ Notes Download methods ==============================

    @Override
    public Path getNotesFilePath() {
        return notesDownloadStorage.getNotesFilePath();
    }

    @Override
    public void deleteAllNotes() throws IOException {
        notesDownloadStorage.deleteAllNotes();
    }

    @Override
    public void deleteSelectedNotes(Set<String> moduleNames) throws IOException {
        notesDownloadStorage.deleteSelectedNotes(moduleNames);
    }

    @Override
    public void relocateNotes(String moduleName) throws IOException {
        notesDownloadStorage.relocateNotes(moduleName);
    }

    @Override
    public void unzipNotes(String moduleName) throws IOException {
        notesDownloadStorage.unzipNotes(moduleName);
    }

    @Override
    @Subscribe
    public void handleAllNotesManipulationEvent(NotesEvent notesEvent) throws IOException {
        logger.info(LogsCenter.getEventHandlingLogMessage(notesEvent, "all notes manipulated"));

        final String commandWord = notesEvent.getEvent();
        //switch-case syntax is used here, to easily allow for future expandability.
        switch (commandWord) {

        case ClearNotesCommand.COMMAND_WORD:
            deleteAllNotes();
            return;

        default:
            throw new IOException();
        }
    }

    @Override
    @Subscribe
    public void handleNotesDownloadedEvent(NotesDownloadEvent notesDownloadEvent) throws IOException {
        logger.info(LogsCenter.getEventHandlingLogMessage(notesDownloadEvent, "notes downloaded"));

        final String commandWord = notesDownloadEvent.getEvent();
        switch (commandWord) {

        case DownloadAllNotesCommand.COMMAND_WORD:
            unzipNotes(notesDownloadEvent.getModuleCode());
            return;

        case DownloadSelectNotesCommand.COMMAND_WORD:
            relocateNotes(notesDownloadEvent.getModuleCode());
            return;

        default:
            throw new IOException();
        }
    }

    @Override
    @Subscribe
    public void handleSelectedNotesManipulationEvent(NotesSelectedEvent notesSelectedEventEvent) throws IOException {
        logger.info(LogsCenter.getEventHandlingLogMessage(notesSelectedEventEvent,
                "selected notes manipulated"));

        final String commandWord = notesSelectedEventEvent.getEvent();
        //switch-case syntax is used here, to easily allow for future expandability.
        switch (commandWord) {

        case DeleteSelectNotesCommand.COMMAND_WORD:
            deleteSelectedNotes(notesSelectedEventEvent.getModuleCodes());
            return;

        default:
            throw new IOException();
        }
    }

}
