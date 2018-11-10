package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.NotesEvent;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final FilteredList<Person> filteredPersons;
    private final NotesDownloaded notesDownloaded;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyNotesDownloaded notesData, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedAddressBook(addressBook);
        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        notesDownloaded = new NotesDownloaded(notesData);
    }

    public ModelManager() {
        this(new AddressBook(), new NotesDownloaded(), new UserPrefs());
    }

    @Override
    public void resetAddressBookData(ReadOnlyAddressBook newData) {
        versionedAddressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
        indicateAddressBookChanged();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    // ================ Notes Manipulation ==============================

    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    @Override
    public void resetNotesData(ReadOnlyNotesDownloaded newData) {
        notesDownloaded.clear();
        notesDownloaded.setNotes(newData.getNotesList());
    }

    /**
     * Returns an unmodifiable view of the list of downloaded notes
     */
    @Override
    public ReadOnlyNotesDownloaded getNotesList() {
        return notesDownloaded;
    }

    /**
     * clears the list of notes
     */
    public void clearNotesData(String event) {
        notesDownloaded.clear();
        indicateNotesManipulated(event);
    }

    /**
     * add a new entry to the list of downloaded notes
     */
    public void addNotes(String event, String moduleCode) {
        notesDownloaded.addNotes(moduleCode);
        indicateNotesManipulated(event, moduleCode);
    }

    /**
     * remove existing notes from the list of downloaded notes, and deletes those notes from storage
     */
    public void deleteSelectedNotes(String event, Set<String> moduleCodes) {
        notesDownloaded.deleteSelectedNotes(moduleCodes);
        indicateNotesManipulated(event, moduleCodes);
    }

    /**
     * Raises an event to indicate that the current notes are manipulated
     */
    private void indicateNotesManipulated(String event, Set<String> moduleCodes) {
        raise(new NotesEvent(event, moduleCodes));
    }

    /** Raises an event to indicate the current notes are manipulated */
    private void indicateNotesManipulated(String event, String moduleCode) {
        Set<String> tempSet = new TreeSet<>();
        tempSet.add(moduleCode);
        raise(new NotesEvent(event, tempSet));
    }

    /** Raises an event to indicate the current notes are manipulated */
    private void indicateNotesManipulated(String event) {
        Set<String> tempSet = new TreeSet<>();
        raise(new NotesEvent(event, tempSet));
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && filteredPersons.equals(other.filteredPersons)
                && notesDownloaded.equals(other.notesDownloaded);
    }

}
