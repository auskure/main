package seedu.address.commons.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

/**
 * Writes, reads, relocates and deletes files
 */
public class FileUtil {

    private static final String CHARSET = "UTF-8";

    private static final String PARAM_CURRENT_DIRECTORY = "user.dir";

    /**
     * Helps to find the current directory
     */
    public static String currentDirectory() {
        return System.getProperty(PARAM_CURRENT_DIRECTORY);
    }

    public static boolean isFileExists(Path file) {
        return Files.exists(file) && Files.isRegularFile(file);
    }

    public static boolean isDirectoryExists(Path directory) {
        return Files.exists(directory) && Files.isDirectory(directory);
    }

    /**
     * Helps to find all valid folders in a chosen directory
     */
        public static Set<String> loadFolders(Path directory){
        File targetDirectory = new File(currentDirectory() + "/" + directory.toString());
        Set<String> folderNames = new TreeSet<>();
        //user currently has no downloaded notes
        if (!isDirectoryExists(directory)) {
            return folderNames;
        }
        String currentName;
        File[] filesList = targetDirectory.listFiles();
        for (File tempFolder : filesList) {
            if (tempFolder.isHidden() || tempFolder.isFile()) {
                continue;
            }
            currentName = tempFolder.getName();
            folderNames.add(currentName);
        }
        return folderNames;
    }


    /**
     * Returns true if {@code path} can be converted into a {@code Path} via {@link Paths#get(String)},
     * otherwise returns false.
     * @param path A string representing the file path. Cannot be null.
     */
    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ipe) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string that does not include "/"
     * @param name A string representing a folder/file name, and hence, should not have "/" within the name
     */
    public static String cleanName(String name) {
        if (!name.contains("/")) {
            return name;
        }
        return name.substring(name.lastIndexOf("/") + 1);
    }

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(Path file) throws IOException {
        if (!isFileExists(file)) {
            createFile(file);
        }
    }

    /**
     * Creates a directory if it does not exist.
     * @throws IOException if the directory cannot be created.
     */
    public static void createDirectoryIfMissing(Path directory) throws IOException {
        if (!isDirectoryExists(directory)) {
            createDirectory(directory);
        }
    }

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     */
    public static void createFile(Path file) throws IOException {
        if (Files.exists(file)) {
            return;
        }

        createParentDirsOfFile(file);

        Files.createFile(file);
    }

    /**
     * Creates a directory if it does not exist.
     */
    public static void createDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            return;
        }

        Files.createDirectory(directory);
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    public static void createParentDirsOfFile(Path file) throws IOException {
        Path parentDir = file.getParent();

        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    /**
     * Writes given string to a file.
     * Will create the file if it does not exist yet.
     */
    public static void writeToFile(Path file, String content) throws IOException {
        Files.write(file, content.getBytes(CHARSET));
    }

    /**
     * Moves all files in a given folder, to a designated folder
     */

    public static void relocateFiles(Path folder, String designatedFolder) {
        File currentDirectory = new File(folder.toString());
        String targetFolder = currentDirectory.toString() + "/" + designatedFolder + "/";
        String currentName;
        File[] filesList = currentDirectory.listFiles();
        for (File file : filesList) {
            if (file.isDirectory() || file.isHidden()) {
                continue;
            }
            currentName = file.getName();
            file.renameTo(new File(targetFolder + currentName));
        }
    }

    /**
     * Deletes all files in a given folder
     */
    public static void deleteAllFiles(Path folder) {
        File currentDirectory = new File(folder.toString());
        File[] filesList = currentDirectory.listFiles();
        for (File file : filesList) {
            //No bookkeeping files stored by the Operating System should be deleted
            if (file.isHidden()) {
                continue;
            }

            String[] directoryContents = file.list();
            if (file.isDirectory() && directoryContents != null) {
                recursiveDelete(file);
                continue;
            }
            file.delete();
        }
    }

    /**
     * Deletes all files in a given folder
     */
    public static void deleteSelectedFolders(Path folder, Set<String> folderNames) {
        File currentDirectory = new File(folder.toString());
        String currentName;
        File[] filesList = currentDirectory.listFiles();
        Boolean isTarget;
        for (File tempFolder : filesList) {
            currentName = tempFolder.getName();
            isTarget = false;
            for (String name : folderNames) {
                if (currentName.contains(name)) {
                    isTarget = true;
                }
            }

            //No bookkeeping files stored by the Operating System should be deleted
            if (tempFolder.isHidden()) {
                continue;
            }

            if (!isTarget) {
                continue;
            }

            String[] directoryContents = tempFolder.list();
            if (tempFolder.isDirectory() && directoryContents != null) {
                recursiveDelete(tempFolder);
            }
            tempFolder.delete();
        }
    }

    /**
     * Helps to delete notes in an occupied folder
     */
    private static void recursiveDelete(File file) {
        if (file.isDirectory()) {
            File[] directoryContents = file.listFiles();
            if (directoryContents != null) {
                for (File content : directoryContents) {
                    recursiveDelete(content);
                }
            }
        }
        file.delete();
    }

}
