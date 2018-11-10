package seedu.address.model;
//@@author auskure
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalNotesDownloaded.CS2100_NOTES;
import static seedu.address.testutil.TypicalNotesDownloaded.CS2101_NOTES;
import static seedu.address.testutil.TypicalNotesDownloaded.getDifferentNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.NotesDownloadedBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasPerson(null);
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredPersonList().remove(0);
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        NotesDownloaded notesDownloaded = new NotesDownloadedBuilder()
                                                    .withNotes(CS2100_NOTES)
                                                    .withNotes(CS2101_NOTES).build();
        NotesDownloaded differentNotesDownloaded = new NotesDownloaded();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, notesDownloaded, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, notesDownloaded, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook, with everything else constant -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, notesDownloaded, userPrefs)));

        // different notesDownloaded, with everything else constant -> returns false
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentNotesDownloaded, userPrefs)));

        // different filteredList, with everything else constant -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, notesDownloaded, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different notesDownloaded, with everything else constant -> returns false
        modelManager.resetNotesData(getDifferentNotesDownloaded());
        assertFalse(modelManager.equals(new ModelManager(addressBook, notesDownloaded, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.resetNotesData(notesDownloaded);

        // different userPrefs, with everything else constant -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(addressBook, notesDownloaded, differentUserPrefs)));
    }
}
