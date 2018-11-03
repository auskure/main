package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static seedu.address.commons.util.FileUtil.createDirectoryIfMissing;
import static seedu.address.commons.util.FileUtil.deleteAllFiles;
import static seedu.address.commons.util.FileUtil.relocateFiles;
import static seedu.address.commons.util.UnzipUtil.unzipFile;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class NotesDownloadStorageHelper implements NotesDownloadStorage {

    private Path notesFilePath;
    private Path absoluteNotesFilePath;

    public NotesDownloadStorageHelper(Path notesFilePath) {
        this.notesFilePath = notesFilePath;
        this.absoluteNotesFilePath = notesFilePath.toAbsolutePath();
    }

    public Path getNotesFilePath() {
        return notesFilePath;
    }

    /**
     * Deletes all notes in the notes folder
     */
    public void deleteAllNotes() throws IOException {
        createDirectoryIfMissing(absoluteNotesFilePath);

        deleteAllFiles(absoluteNotesFilePath);
    }

    /**
     * Relocates notes to their appropriate folders
     */
    public void relocateNotes(String moduleName) throws IOException{
        Path moduleDirectory = Paths.get(notesFilePath.toString(), moduleName);
        createDirectoryIfMissing(moduleDirectory);
        relocateFiles(absoluteNotesFilePath, moduleName);
    }

    /**
     * Unzips newly downloaded notes in the notes folder
     */
    public void unzipNotes(String moduleName) throws IOException{
        unzipFile(absoluteNotesFilePath, moduleName);
    }

}
