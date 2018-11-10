package seedu.address.logic.commands;
//@@author auskure

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.DeleteSelectNotesCommand.MESSAGE_DELETE_ALL_NOTES_CAUTION;
import static seedu.address.logic.commands.DeleteSelectNotesCommand.MESSAGE_DELETE_ALL_NOTES_SUCCESS;
import static seedu.address.logic.commands.DeleteSelectNotesCommand.MESSAGE_UNAVAILABLE_NOTES;
import static seedu.address.testutil.TypicalModuleCodes.getAllDifferentModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getAllTypicalModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getMultipleDifferentModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getMultipleTypicalModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getAdvancedMixedValidityModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getOneDifferentModuleCode;
import static seedu.address.testutil.TypicalModuleCodes.getOneTypicalModulePrefix;
import static seedu.address.testutil.TypicalModuleCodes.getOneTypicalModuleCode;
import static seedu.address.testutil.TypicalModuleCodes.getSimpleMixedValidityModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getZeroModuleCodes;
import static seedu.address.testutil.TypicalNotesDownloaded.getTypicalNotesDownloaded;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model and ClearNotes) and unit tests for
 * {@code DeleteSelectNotesCommandTest}.
 */
public class DeleteSelectNotesCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalNotesDownloaded(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * checks DeleteSelectNotes executes correctly, given a single valid moduleCode.
     */
    @Test
    public void execute_validSingle_delete_success() {
        Set<String> validNotesToDelete = getOneTypicalModuleCode();
        Set<String> invalidNotesToDelete = getZeroModuleCodes();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(validNotesToDelete);

        String expectedMessage = MESSAGE_DELETE_ALL_NOTES_SUCCESS + validNotesToDelete.toString()
                                     + MESSAGE_DELETE_ALL_NOTES_CAUTION + invalidNotesToDelete.toString();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, validNotesToDelete);

        assertCommandSuccess(deleteSelectNotesCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Tests for the capability of DeleteSelectNotes, to handle requests containing multiple moduleCodes
     */
    @Test
    public void execute_validMultiple_delete_success() {
        Set<String> validNotesToDelete = getMultipleTypicalModuleCodes();
        Set<String> invalidNotesToDelete = getZeroModuleCodes();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(validNotesToDelete);

        String expectedMessage = MESSAGE_DELETE_ALL_NOTES_SUCCESS + validNotesToDelete.toString()
                                    + MESSAGE_DELETE_ALL_NOTES_CAUTION + invalidNotesToDelete.toString();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, validNotesToDelete);

        assertCommandSuccess(deleteSelectNotesCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Tests for the capability of DeleteSelectNotes, to handle requests containing valid and invalid moduleCodes.
     * In this simple version, the test contains one valid moduleCode, and one moduleCode not yet in
     * notesDownloaded, which makes it invalid.
     */
    @Test
    public void execute_validSimple_partial_delete_success() {
        Set<String> notesToDelete = getSimpleMixedValidityModuleCodes();
        Set<String> validNotesToDelete = getOneTypicalModuleCode();
        Set<String> invalidNotesToDelete = getOneDifferentModuleCode();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        String expectedMessage = MESSAGE_DELETE_ALL_NOTES_SUCCESS + validNotesToDelete.toString()
                                    + MESSAGE_DELETE_ALL_NOTES_CAUTION + invalidNotesToDelete.toString();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, notesToDelete);

        assertCommandSuccess(deleteSelectNotesCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Tests for the capability of DeleteSelectNotes, to handle requests containing valid and invalid moduleCodes.
     * In this advanced version, the test contains multiple valid moduleCode, and multiple moduleCode not yet in
     * notesDownloaded, which makes them invalid.
     */
    @Test
    public void execute_validAdvanced_partial_delete_success() {
        Set<String> notesToDelete = getAdvancedMixedValidityModuleCodes();
        Set<String> validNotesToDelete = getAllTypicalModuleCodes();
        Set<String> invalidNotesToDelete = getAllDifferentModuleCodes();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        String expectedMessage = MESSAGE_DELETE_ALL_NOTES_SUCCESS + validNotesToDelete.toString()
                                  + MESSAGE_DELETE_ALL_NOTES_CAUTION + invalidNotesToDelete.toString();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, notesToDelete);

        assertCommandSuccess(deleteSelectNotesCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Tests for the capability of DeleteSelectNotes, to handle requests containing an invalid moduleCode.
     * We assume that this moduleCode not yet in notesDownloaded, which makes it invalid.
     */
    @Test
    public void execute_invalidSingle_delete_failure() {
        Set<String> notesToDelete = getOneDifferentModuleCode();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, notesToDelete);

        assertCommandFailure(deleteSelectNotesCommand, model, commandHistory, MESSAGE_UNAVAILABLE_NOTES);
    }

    /**
     * Tests for the capability of DeleteSelectNotes, to handle requests multiple invalid moduleCodes.
     * We assume that these moduleCodes are not yet in notesDownloaded, which makes them invalid.
     */
    @Test
    public void execute_invalidMultiple_delete_failure() {
        Set<String> notesToDelete = getMultipleDifferentModuleCodes();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, notesToDelete);

        assertCommandFailure(deleteSelectNotesCommand, model, commandHistory, MESSAGE_UNAVAILABLE_NOTES);
    }

    /**
     * Tests for the capability of DeleteSelectNotes, to handle a single request, deleting multiple notes.
     */
    @Test
    public void execute_validMultiple_delete_by_prefix_success() {
        Set<String> notesToDelete = getOneTypicalModulePrefix();
        Set<String> validNotesToDelete = getOneTypicalModulePrefix();
        Set<String> invalidNotesToDelete = getZeroModuleCodes();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        String expectedMessage = MESSAGE_DELETE_ALL_NOTES_SUCCESS + validNotesToDelete.toString()
                + MESSAGE_DELETE_ALL_NOTES_CAUTION + invalidNotesToDelete.toString();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.deleteSelectedNotes(DeleteSelectNotesCommand.COMMAND_WORD, notesToDelete);

        assertCommandSuccess(deleteSelectNotesCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * This integration test tests for the capability of DeleteSelectNotes, to work correctly for a single request
     * after the ClearNotesCommand is run.
     * After ClearNotesCommand, there are no moduleCodes in notesDownloaded, so all moduleCodes are invalid.
     */
    @Test
    public void executeClearNotes_invalidSingle_delete_failure() {
        Set<String> notesToDelete = getOneTypicalModuleCode();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.clearNotesData(ClearNotesCommand.COMMAND_WORD);

        assertCommandSuccess(new ClearNotesCommand(), model, commandHistory,
                                ClearNotesCommand.MESSAGE_SUCCESS, expectedModel);

        assertCommandFailure(deleteSelectNotesCommand, model, commandHistory, MESSAGE_UNAVAILABLE_NOTES);
    }

    /**
     * This integration test tests for the capability of DeleteSelectNotes, to work correctly for a multiple requests
     * after the ClearNotesCommand is run.
     * After ClearNotesCommand, there are no moduleCodes in notesDownloaded, so all moduleCodes are invalid.
     */
    @Test
    public void executeClearNotes_invalidMultiple_delete_failure() {
        Set<String> notesToDelete = getAllTypicalModuleCodes();
        DeleteSelectNotesCommand deleteSelectNotesCommand = new DeleteSelectNotesCommand(notesToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), model.getNotesList(), new UserPrefs());
        expectedModel.clearNotesData(ClearNotesCommand.COMMAND_WORD);

        assertCommandSuccess(new ClearNotesCommand(), model, commandHistory,
                ClearNotesCommand.MESSAGE_SUCCESS, expectedModel);

        assertCommandFailure(deleteSelectNotesCommand, model, commandHistory, MESSAGE_UNAVAILABLE_NOTES);
    }

    @Test
    public void equals() {
        Set<String> oneModuleCode = getOneTypicalModuleCode();
        Set<String> oneDifferentModuleCode = getOneDifferentModuleCode();
        Set<String> multipleModuleCodes = getMultipleTypicalModuleCodes();
        Set<String> mutipleDifferentModuleCodes = getMultipleDifferentModuleCodes();

        DeleteSelectNotesCommand deleteNotesFirstCommand = new DeleteSelectNotesCommand(oneModuleCode);
        DeleteSelectNotesCommand deleteNotesSecondCommand = new DeleteSelectNotesCommand(oneDifferentModuleCode);
        DeleteSelectNotesCommand deleteNotesThirdCommand = new DeleteSelectNotesCommand(multipleModuleCodes);
        DeleteSelectNotesCommand deleteNotesFourthCommand = new DeleteSelectNotesCommand(mutipleDifferentModuleCodes);

        // same object for one moduleCode -> returns true
        assertTrue(deleteNotesFirstCommand.equals(deleteNotesFirstCommand));

        // same values for one moduleCode -> returns true
        DeleteSelectNotesCommand deleteNotesFirstCommandCopy = new DeleteSelectNotesCommand(oneModuleCode);
        assertTrue(deleteNotesFirstCommand.equals(deleteNotesFirstCommandCopy));

        // same object for multiple moduleCodes -> returns true
        assertTrue(deleteNotesThirdCommand.equals(deleteNotesThirdCommand));

        // same values for multiple moduleCodes -> returns true
        DeleteSelectNotesCommand deleteNotesThirdCommandCopy = new DeleteSelectNotesCommand(multipleModuleCodes);
        assertTrue(deleteNotesThirdCommand.equals(deleteNotesThirdCommandCopy));

        // different types -> returns false
        assertFalse(deleteNotesFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteNotesFirstCommand.equals(null));

        // different values of a single moduleCode -> returns false
        assertFalse(deleteNotesFirstCommand.equals(deleteNotesSecondCommand));

        // different values of multiple moduleCodes -> returns false
        assertFalse(deleteNotesThirdCommand.equals(deleteNotesFourthCommand));
    }

}
