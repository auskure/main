package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.FileAndFolderCreation.DIFFERENT_SAMPLE_DIRECTORY;
import static seedu.address.testutil.FileAndFolderCreation.EMPTY_SAMPLE_DIRECTORY;
import static seedu.address.testutil.FileAndFolderCreation.SAMPLE_DIRECTORY;
import static seedu.address.testutil.FileAndFolderCreation.endTestState;
import static seedu.address.testutil.FileAndFolderCreation.initialiseTestState;
import static seedu.address.testutil.TypicalModuleCodes.CS2100_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS2101_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS2102_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS3100_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS3235_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.getMultipleTypicalModuleCodes;
import static seedu.address.testutil.TypicalModuleCodes.getZeroModuleCodes;
import static seedu.address.testutil.TypicalNotesDownloaded.CS2100_NOTES;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Test;

import seedu.address.testutil.Assert;

/**
 * Contains tests for all commands in FileUtil
 */
public class FileUtilTest {

    private static final String VALID_DIRECTORY_NAME = "src";
    private static final String INVALID_DIRECTORY_NAME = "invalid";

    private static final String VALID_FILE_NAME = "copyright.txt";
    private static final String DIFFERENT_VALID_FILE_NAME = "gradle.properties";
    private static final String INVALID_FILE_NAME = "invalid.txt";
    private static final String CLEANED_FILE_NAME = "EmptySampleDirectory";

    private static final String VALID_FILE_PATH = "src/test/data/FileUtilTest/EmptySampleDirectory";
    private static final String INVALID_FILE_PATH = "a\0";

    private static final String SAMPLE_CONTENT = "Mary had a little lamb";
    private static final String DIRECTORY_INDICATOR = "/";
    private static final String PARAM_CURRENT_DIRECTORY = "user.dir";

    private static final Path VALID_FILE = Paths.get(VALID_FILE_NAME);
    private static final Path INVALID_FILE = Paths.get(INVALID_FILE_NAME);

    private static final Path VALID_DIRECTORY = Paths.get(VALID_DIRECTORY_NAME);
    private static final Path INVALID_DIRECTORY = Paths.get(INVALID_DIRECTORY_NAME);

    private final Set<String> sampleDirectories = getMultipleTypicalModuleCodes();
    private final Set<String> emptySampleDirectory = getZeroModuleCodes();

    @Test
    public void currentDirectory() {
        // currentDirectory has to be equals to what the System Call returns
        assertTrue(FileUtil.currentDirectory().equals(System.getProperty(PARAM_CURRENT_DIRECTORY)));
    }

    @Test
    public void isFileExists() throws IOException {
        initialiseTestState();
        // valid file -> true
        assertTrue(FileUtil.isFileExists(VALID_FILE));

        // invalid file -> false as invalid file
        assertFalse(FileUtil.isFileExists(INVALID_FILE));

        // valid directory -> false as not a file
        assertFalse(FileUtil.isFileExists(VALID_DIRECTORY));

        // invalid directory -> false as not a file
        assertFalse(FileUtil.isFileExists(INVALID_DIRECTORY));

        endTestState();
    }

    @Test
    public void isDirectoryExists() throws IOException {
        initialiseTestState();
        // valid directory -> true
        assertFalse(FileUtil.isFileExists(VALID_DIRECTORY));

        // invalid directory -> false as invalid directory
        assertFalse(FileUtil.isFileExists(INVALID_DIRECTORY));

        // valid file -> false as not a directory
        assertTrue(FileUtil.isFileExists(VALID_FILE));

        // invalid file -> false as not a directory
        assertFalse(FileUtil.isFileExists(INVALID_FILE));

        endTestState();
    }

    @Test
    public void loadFolders() throws IOException {
        initialiseTestState();

        Set<String> testDirectories;

        // valid empty directory -> empty Set<String>
        testDirectories = FileUtil.loadFolders(EMPTY_SAMPLE_DIRECTORY);
        assertTrue(testDirectories.equals(emptySampleDirectory));

        // valid non-empty directory -> filled set of strings, with the same elements
        testDirectories = FileUtil.loadFolders(SAMPLE_DIRECTORY);
        assertTrue(testDirectories.equals(sampleDirectories));

        // valid different non-empty directory -> filled set of strings, with the same elements
        testDirectories = FileUtil.loadFolders(DIFFERENT_SAMPLE_DIRECTORY);
        assertFalse(testDirectories.equals(sampleDirectories));

        // invalid directory -> empty Set<String>
        testDirectories = FileUtil.loadFolders(INVALID_DIRECTORY);
        assertTrue(testDirectories.equals(emptySampleDirectory));

        endTestState();
    }

    @Test
    public void isValidPath() throws IOException {
        initialiseTestState();

        // valid path
        assertTrue(FileUtil.isValidPath(VALID_FILE_PATH));

        // invalid path
        assertFalse(FileUtil.isValidPath(INVALID_FILE_PATH));

        // null path -> throws NullPointerException
        Assert.assertThrows(NullPointerException.class, () -> FileUtil.isValidPath(null));

        endTestState();
    }

    @Test
   public void cleanName() throws IOException {
        initialiseTestState();

        String testName;

        // {@code testName} does not contain a "/" -> testName unchanged
        testName = VALID_DIRECTORY_NAME;
        assertTrue(FileUtil.cleanName(testName).equals(VALID_DIRECTORY_NAME));

        // {@code testName} contains a "/" -> the part before the "/" (inclusive) is removed
        testName = VALID_FILE_PATH;
        assertTrue(FileUtil.cleanName(testName).equals(CLEANED_FILE_NAME));

        endTestState();
    }

    @Test
    public void cleanModuleCode() throws IOException {
        initialiseTestState();

        String testModuleCode;

        // {@code testModuleCode} does not contain a " " -> testModuleCode unchanged
        testModuleCode = CS2100_MODULE_CODE;
        assertTrue(FileUtil.cleanModuleCode(testModuleCode).equals(CS2100_MODULE_CODE));

        // {@code testModuleCode} does not contain a " " ->  the part after the " "(inclusive) is removed
        testModuleCode = CS2100_NOTES;
        assertTrue(FileUtil.cleanModuleCode(testModuleCode).equals(CS2100_MODULE_CODE));

        endTestState();
    }

    /**
     * These tests integrates tests for both createIfMissing and deleteAllNotes,
     * as they are required to be tested together
     * Simple commands such as FileUtil.isFileExists are also incorporated into the tests.
     */
    @Test
    public void createIfMissing() throws IOException {
        initialiseTestState();

        Path testFile = Paths.get(VALID_FILE_PATH, VALID_FILE_NAME);
        Path testParentDirectory = Paths.get(VALID_FILE_PATH);

        // To ensure that {@code testFile} does not exist
        assertFalse(FileUtil.isFileExists(testFile));

        // {@code testFile} will be created if it does not exist.
        FileUtil.createIfMissing(testFile);
        assertTrue(FileUtil.isFileExists(testFile));

        // no extra {@code testFile} will be created if it already exists.
        FileUtil.createIfMissing(testFile);
        File tempFile = new File(FileUtil.currentDirectory() + DIRECTORY_INDICATOR + VALID_FILE_PATH
                                + DIRECTORY_INDICATOR + VALID_FILE_NAME);
        tempFile.delete();
        assertFalse(FileUtil.isFileExists(testFile));

        // To ensure that testFile is now deleted, and that the folder is cleaned for the next tests
        assertFalse(FileUtil.isFileExists(testFile));

        endTestState();
    }

    /**
     * These tests integrates tests for both createDirectoryIfMissing and deleteAllNotes,
     * as they are required to be tested together
     * Simple commands such as FileUtil.isDirectoryExists are also incorporated into the tests.
     */
    @Test
    public void createDirectoryIfMissing() throws IOException {
        initialiseTestState();

        Path testDirectory = Paths.get(VALID_FILE_PATH, VALID_DIRECTORY_NAME);
        Path testParentDirectory = Paths.get(VALID_FILE_PATH);

        // To ensure that {@code testDirectory} does not exist
        assertFalse(FileUtil.isDirectoryExists(testDirectory));

        // {@code testDirectory} will be created if it does not exist.
        FileUtil.createDirectoryIfMissing(testDirectory);
        assertTrue(FileUtil.isDirectoryExists(testDirectory));

        // no extra {@code testDirectory} will be created if it already exists.
        FileUtil.createDirectoryIfMissing(testDirectory);
        File tempFile = new File(FileUtil.currentDirectory() + DIRECTORY_INDICATOR + VALID_FILE_PATH
                                    + DIRECTORY_INDICATOR + VALID_DIRECTORY_NAME);
        tempFile.delete();
        assertFalse(FileUtil.isDirectoryExists(testDirectory));

        // To ensure that testDirectory is now deleted
        FileUtil.deleteAllFiles(testParentDirectory);
        assertFalse(FileUtil.isDirectoryExists(testDirectory));

        endTestState();
    }

    /**
     * These tests integrates tests for both createFile and deleteAllNotes,
     * as they are required to be tested together
     * Simple commands such as FileUtil.isFileExists are also incorporated into the tests.
     */
    @Test
    public void createFile() throws IOException {
        initialiseTestState();

        Path testFile = Paths.get(VALID_FILE_PATH, VALID_FILE_PATH, VALID_FILE_NAME);
        Path testParentDirectory = Paths.get(VALID_FILE_PATH);

        // To ensure that {@code testFile} does not exist
        assertFalse(FileUtil.isFileExists(testFile));

        // {@code testFile} will be created if it does not exist.
        FileUtil.createFile(testFile);
        assertTrue(FileUtil.isFileExists(testFile));

        // no extra {@code testFile} will be created if it already exists.
        FileUtil.createFile(testFile);
        File tempFile = new File(FileUtil.currentDirectory() + DIRECTORY_INDICATOR + VALID_FILE_PATH
                                    + DIRECTORY_INDICATOR + VALID_FILE_PATH + DIRECTORY_INDICATOR + VALID_FILE_NAME);
        tempFile.delete();
        assertFalse(FileUtil.isFileExists(testFile));

        // To ensure that {@code testDirectory} and all parent directories are now deleted
        FileUtil.deleteAllFiles(testParentDirectory);
        assertFalse(FileUtil.isFileExists(testFile));

        endTestState();
    }

    /**
     * These tests integrates tests for both createParentDirsOfFile and deleteAllNotes,
     * as they are required to be tested together
     * Simple commands such as FileUtil.isDirectoryExists are also incorporated into the tests.
     */
    @Test
    public void createParentDirsOfFile() throws IOException {
        initialiseTestState();

        Path testFile = Paths.get(VALID_FILE_PATH, VALID_FILE_PATH, VALID_FILE_NAME);
        Path testParent = Paths.get(VALID_FILE_PATH, VALID_DIRECTORY_NAME);
        Path testMainParentDirectory = Paths.get(VALID_FILE_PATH);

        // To ensure that {@code testFile} does not exist
        assertFalse(FileUtil.isDirectoryExists(testParent));

        // {@code testParent} will be created if it does not exist.
        FileUtil.createParentDirsOfFile(testFile);
        assertTrue(FileUtil.isDirectoryExists(testParent));

        // To ensure that {@code testMainParentDirectory} and all parent directories are now deleted
        FileUtil.deleteAllFiles(testMainParentDirectory);
        assertFalse(FileUtil.isFileExists(testFile));

        // To ensure that no {@code testParent} will be created if the entered file does not have a parent
        testFile = Paths.get(VALID_FILE_PATH, VALID_FILE_NAME);
        FileUtil.createParentDirsOfFile(testFile);
        assertFalse(FileUtil.isDirectoryExists(testParent));

        // To ensure that {@code testDirectory} and all parent directories are now deleted
        FileUtil.deleteAllFiles(testMainParentDirectory);
        assertFalse(FileUtil.isFileExists(testFile));

        endTestState();
    }

    /**
     * These tests integrates tests for both readFromFile and writeToFile and deleteAllNotes,
     * as they are required to be tested together
     */
    @Test
    public void readAndWriteFiles() throws IOException {
        initialiseTestState();

        Path testFile = Paths.get(VALID_FILE_PATH, VALID_FILE_NAME);
        Path testParentDirectory = Paths.get(VALID_FILE_PATH);
        String testContent;

        // To ensure that {@code testFile} does not exist
        assertFalse(FileUtil.isFileExists(testFile));

        // To ensure writeToFile creates a file
        FileUtil.writeToFile(testFile, SAMPLE_CONTENT);
        assertTrue(FileUtil.isFileExists(testFile));

        // To ensure that the content is written and hence, read correctly.
        testContent = FileUtil.readFromFile(testFile);
        assertTrue(testContent.equals(SAMPLE_CONTENT));

        // To ensure that {@code testDirectory} and all parent directories are now deleted
        FileUtil.deleteAllFiles(testParentDirectory);
        assertFalse(FileUtil.isFileExists(testFile));

        endTestState();
    }

    /**
     * These tests integrates tests for both relocateFiles and deleteAllNotes,
     * as they are required to be tested together.
     * This is ideal for deleteAllFiles as deletion of non-empty folders is involved.
     */
    @Test
    public void relocateFilesAndDeleteAllFiles() throws IOException {
        initialiseTestState();

        Path testFolder = Paths.get(VALID_FILE_PATH, VALID_DIRECTORY_NAME);
        Path testFileOneSource = Paths.get(VALID_FILE_PATH, VALID_FILE_NAME);
        Path testFileTwoSource = Paths.get(VALID_FILE_PATH, DIFFERENT_VALID_FILE_NAME);
        Path testFileOneTarget = Paths.get(VALID_FILE_PATH, VALID_DIRECTORY_NAME, VALID_FILE_NAME);
        Path testFileTwoTarget = Paths.get(VALID_FILE_PATH, VALID_DIRECTORY_NAME, DIFFERENT_VALID_FILE_NAME);
        Path testParentDirectory = Paths.get(VALID_FILE_PATH);

        // Create {@code testFileOneSource, testFileTwoSource and testFolder}
        FileUtil.createIfMissing(testFileOneSource);
        FileUtil.createIfMissing(testFileTwoSource);
        FileUtil.createDirectoryIfMissing(testFolder);


        // To ensure that {@code testFileOneSource, testFileTwoSource and testFolder} exist
        assertTrue(FileUtil.isFileExists(testFileOneSource));
        assertTrue(FileUtil.isFileExists(testFileTwoSource));
        assertTrue(FileUtil.isDirectoryExists(testFolder));

        FileUtil.relocateFiles(testParentDirectory, VALID_DIRECTORY_NAME.toString());

        // To ensure that {@code testFileOneSource and testFileTwoSource} do not exist in their source paths anymore.
        assertFalse(FileUtil.isFileExists(testFileOneSource));
        assertFalse(FileUtil.isFileExists(testFileTwoSource));

        // To ensure that {@code testFileOneTarget and testFileTwoTarget} exist in their target paths.
        assertTrue(FileUtil.isFileExists(testFileOneTarget));
        assertTrue(FileUtil.isFileExists(testFileTwoTarget));

        // To ensure that {@code testDirectory} and all parent directories are now deleted
        FileUtil.deleteAllFiles(testParentDirectory);
        assertFalse(FileUtil.isFileExists(testFolder));

        endTestState();
    }

    /**
     * These tests integrates tests for both deleteSelectFiles and deleteAllNotes,
     * as they are required to be tested together.
     */
    @Test
    public void deleteSelectFiles() throws IOException {
        initialiseTestState();

        Path testFolderOneToDelete = Paths.get(VALID_FILE_PATH, CS2100_MODULE_CODE);
        Path testFolderTwoToDelete = Paths.get(VALID_FILE_PATH, CS2101_MODULE_CODE);
        Path testFolderThreeToDelete = Paths.get(VALID_FILE_PATH, CS2102_MODULE_CODE);
        Path testFolderOneToStay = Paths.get(VALID_FILE_PATH, CS3100_MODULE_CODE);
        Path testFolderTwoToStay = Paths.get(VALID_FILE_PATH, CS3235_MODULE_CODE);
        Path testParentDirectory = Paths.get(VALID_FILE_PATH);

        // Create {@code testFileOneSource, testFileTwoSource and }
        FileUtil.createDirectoryIfMissing(testFolderOneToDelete);
        FileUtil.createDirectoryIfMissing(testFolderTwoToDelete);
        FileUtil.createDirectoryIfMissing(testFolderThreeToDelete);
        FileUtil.createDirectoryIfMissing(testFolderOneToStay);
        FileUtil.createDirectoryIfMissing(testFolderTwoToStay);


        // To ensure that all relevant folders required for the test exists
        assertTrue(FileUtil.isDirectoryExists(testFolderOneToDelete));
        assertTrue(FileUtil.isDirectoryExists(testFolderTwoToDelete));
        assertTrue(FileUtil.isDirectoryExists(testFolderThreeToDelete));
        assertTrue(FileUtil.isDirectoryExists(testFolderOneToStay));
        assertTrue(FileUtil.isDirectoryExists(testFolderTwoToStay));

        FileUtil.deleteSelectedFolders(testParentDirectory, getMultipleTypicalModuleCodes());

        // To ensure that folders to be deleted are deleted, and folders meant to stay is kept.
        assertFalse(FileUtil.isDirectoryExists(testFolderOneToDelete));
        assertFalse(FileUtil.isDirectoryExists(testFolderTwoToDelete));
        assertFalse(FileUtil.isDirectoryExists(testFolderThreeToDelete));
        assertTrue(FileUtil.isDirectoryExists(testFolderOneToStay));
        assertTrue(FileUtil.isDirectoryExists(testFolderTwoToStay));

        // To ensure that {@code testDirectory} and all parent directories are now deleted
        FileUtil.deleteAllFiles(testParentDirectory);
        assertFalse(FileUtil.isDirectoryExists(testFolderOneToDelete));
        assertFalse(FileUtil.isDirectoryExists(testFolderTwoToDelete));
        assertFalse(FileUtil.isDirectoryExists(testFolderThreeToDelete));
        assertFalse(FileUtil.isDirectoryExists(testFolderOneToStay));
        assertFalse(FileUtil.isDirectoryExists(testFolderTwoToStay));

        endTestState();
    }

}
