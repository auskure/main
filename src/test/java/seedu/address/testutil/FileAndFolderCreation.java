package seedu.address.testutil;

import static seedu.address.testutil.TypicalModuleCodes.CS2100_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS2101_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS2102_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS3100_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS3235_MODULE_CODE;
import static seedu.address.testutil.TypicalModuleCodes.CS5240_MODULE_CODE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Assists FileUtilTest with creating the environment for the test, and deleting the environment after the test is over.
 */
public class FileAndFolderCreation {

    public static final Path PARENT_DIRECTORY = Paths.get("src", "test", "data", "FileUtilTest");

    public static final Path EMPTY_SAMPLE_DIRECTORY = Paths.get("src", "test",
            "data", "FileUtilTest", "EmptySampleDirectory");

    public static final Path SAMPLE_DIRECTORY = Paths.get("src", "test",
            "data", "FileUtilTest", "SampleDirectories");

    public static final Path DIFFERENT_SAMPLE_DIRECTORY = Paths.get("src", "test", "data",
            "FileUtilTest", "DifferentSampleDirectories");

    /**
     * Creating the environment for the test
     */
    public static void initialiseTestState() throws IOException {
        // Creating parent directory
        Files.createDirectory(PARENT_DIRECTORY);

        // Creating directory and subdirectories for SAMPLE_DIRECTORY
        Files.createDirectory(SAMPLE_DIRECTORY);
        Path firstModule = Paths.get(SAMPLE_DIRECTORY.toString(), CS2100_MODULE_CODE);
        Path secondModule = Paths.get(SAMPLE_DIRECTORY.toString(), CS2101_MODULE_CODE);
        Path thirdModule = Paths.get(SAMPLE_DIRECTORY.toString(), CS2102_MODULE_CODE);
        Files.createDirectory(firstModule);
        Files.createDirectory(secondModule);
        Files.createDirectory(thirdModule);

        // Creating directory for EMPTY_SAMPLE_DIRECTORY
        Files.createDirectory(EMPTY_SAMPLE_DIRECTORY);

        // Creating directory and subdirectories for DIFFERENT_SAMPLE_DIRECTORY
        Files.createDirectory(DIFFERENT_SAMPLE_DIRECTORY);
        Path fourthModule = Paths.get(DIFFERENT_SAMPLE_DIRECTORY.toString(), CS3100_MODULE_CODE);
        Path fifthModule = Paths.get(DIFFERENT_SAMPLE_DIRECTORY.toString(), CS3235_MODULE_CODE);
        Path sixthModule = Paths.get(DIFFERENT_SAMPLE_DIRECTORY.toString(), CS5240_MODULE_CODE);
        Files.createDirectory(fourthModule);
        Files.createDirectory(fifthModule);
        Files.createDirectory(sixthModule);
    }

    /**
     * Deletes the environment for the test when it is over
     */
    public static void endTestState() throws IOException {
        // Deleting directory and subdirectories for SAMPLE_DIRECTORY
        Path firstModule = Paths.get(SAMPLE_DIRECTORY.toString(), CS2100_MODULE_CODE);
        Path secondModule = Paths.get(SAMPLE_DIRECTORY.toString(), CS2101_MODULE_CODE);
        Path thirdModule = Paths.get(SAMPLE_DIRECTORY.toString(), CS2102_MODULE_CODE);
        Files.delete(firstModule);
        Files.delete(secondModule);
        Files.delete(thirdModule);
        Files.delete(SAMPLE_DIRECTORY);

        // Deleting directory for EMPTY_SAMPLE_DIRECTORY
        Files.delete(EMPTY_SAMPLE_DIRECTORY);

        // Deleting directories and subdirectories for DIFFERENT_SAMPLE_DIRECTORY
        Path fourthModule = Paths.get(DIFFERENT_SAMPLE_DIRECTORY.toString(), CS3100_MODULE_CODE);
        Path fifthModule = Paths.get(DIFFERENT_SAMPLE_DIRECTORY.toString(), CS3235_MODULE_CODE);
        Path sixthModule = Paths.get(DIFFERENT_SAMPLE_DIRECTORY.toString(), CS5240_MODULE_CODE);
        Files.delete(fourthModule);
        Files.delete(fifthModule);
        Files.delete(sixthModule);
        Files.delete(DIFFERENT_SAMPLE_DIRECTORY);

        //Deleting parent directory
        Files.delete(PARENT_DIRECTORY);
    }
}
