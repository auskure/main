package seedu.address.logic.commands;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import seedu.address.commons.util.UnzipUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import static seedu.address.commons.util.FileUtil.createDirectoryIfMissing;

public class DownloadAllNotesCommand extends DownloadAbstract {

    public static final String COMMAND_WORD = "downloadAllNotes";

    public static final String MESSAGE_USAGE = "downloadAllNotes user/(username) pass/(password) mod/(moduleCode)";

    private static final String CHECKBOX_XPATH_VALUE = "//input[@type='checkbox']";

    private static final String IVLE_DOWNLOAD_PAGE_BUTTON_ID = "ctl00_ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_ContentPlaceHolder1_btnDownloadSel";


    public DownloadAllNotesCommand(String username, String password, String moduleCode) {
        super(username, password, moduleCode);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        Path downloadTempFolder = Paths.get("tempDownloadStorage");
        Path notesFolder = Paths.get("notes");
        try{
            createDirectoryIfMissing(downloadTempFolder);
            createDirectoryIfMissing(notesFolder);
        } catch (Exception e) {
            throw new CommandException("Failed to create new folders");
        }

        try{
            initializeChromedriverPath();
        }
        catch(NullPointerException npe){
            throw new CommandException(MESSAGE_CHROME_DRIVER_NOT_FOUND);
        }

        WebDriver driver = initializeWebDriver();
        try{
            loginIvle(driver);
        }
        catch(NoSuchElementException nse){
            driver.close();
            throw new CommandException(MESSAGE_UNABLE_REACH_IVLE);
        }
        if(!isLoggedIn(driver)) {
            driver.close();
            throw new CommandException(MESSAGE_USERNAME_PASSWORD_ERROR);
        }
        if(isModuleExisting(driver)){
            initializeDownloadFolder();
            downloadFiles(driver);
            dynamicWaiting();
            driver.close();
            try{
                UnzipUtil.unzipFile(downloadPath, UNZIP_FILE_KEYWORD,
                        currentDirPath, DOWNLOAD_FILE_PATH, moduleCode);
            }
            catch (IOException ioe) {
                throw new CommandException(MESSAGE_FILE_CORRUPTED);
            }
            catch (NullPointerException npe) {
                throw new CommandException(MESSAGE_NOTES_FOLDER_NOT_FOUND);
            }
            return new CommandResult(moduleCode + MESSAGE_SUCCESS
                                        + currentDirPath + DOWNLOAD_FILE_PATH);
        }
        driver.close();
        throw new CommandException(MESSAGE_MODULE_NOT_FOUND);
    }

    /**
     * downloadFiles selects all the available files to be downloaded and then selects the "download files" button
     * @param driver is the current existing WebDriver session
     */

    protected void downloadFiles(WebDriver driver){
        List<WebElement> checkBoxList=driver.findElements(By.xpath(CHECKBOX_XPATH_VALUE));
        for(WebElement checkBox: checkBoxList) {
            if(!checkBox.isSelected()) {
                checkBox.click();
            }
        }
        driver.findElement(By.id(IVLE_DOWNLOAD_PAGE_BUTTON_ID)).click();
    }
}
