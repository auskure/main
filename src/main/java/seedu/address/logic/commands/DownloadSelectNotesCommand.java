package seedu.address.logic.commands;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import seedu.address.commons.util.UnzipUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

import java.io.IOException;
import java.util.*;
import java.util.NoSuchElementException;

public class DownloadSelectNotesCommand extends DownloadAbstract{

    public static final String COMMAND_WORD = "downloadSelectNotes";

    public static final String MESSAGE_USAGE = "downloadSelectNotes user/(username) pass/(password) mod/(moduleCode) file/(0,1,2...n))";

    public static final String NEWLINE_SEPERATOR = "\r\n";

    public static final String NO_FILES_SELECTED_MESSAGE = "Please select a file after the \"file/\" tag. Ie: file/(0,1,2...n))";

    public static final String MESSAGE_FILE_DOES_NOT_EXIST_ERROR = "A FILE YOU CHOSE DOES NOT EXIST\r\n DOWNLOAD NOT COMPLETE";

    private static final String WORKBIN_CSS_SELECTOR_ID = "a[href^=\"/workbin\"]";

    private static final String TREEVIEW_CLASS_ID = "TreeView";

    private static final String FILE_DOWNLOAD_LINK_ATTRIBUTE_ID = "href";

    private ArrayList<Integer> fileSelect;
    private String availableDownloadFiles;
    /**
     * secondary constructor to handle execution if user enters values with the PREFIX_SELECT_FILE prefix.
     */

    public DownloadSelectNotesCommand(String username, String password, String moduleCode, String fileSelectInput){
        super(username,password,moduleCode);
        fileSelect = new ArrayList<>();
        for(String id: fileSelectInput.split(",")){
            fileSelect.add(Integer.parseInt(id));
        }
    }

    public DownloadSelectNotesCommand(String username, String password, String moduleCode){
        super(username,password,moduleCode);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        initializeChromedriverPath();
        WebDriver driver=initializeWebDriver();
        try{
        loginIvle(driver);
        }
        catch(NoSuchElementException nse){
            driver.close();
            throw new CommandException(MESSAGE_IVLE_LOGIN_FAIL);
        }
        if(!isLoggedIn(driver)){
            driver.close();
            throw new CommandException(WRONG_PASS_USER_MESSAGE);
        }
        if(isModuleExisting(driver)){
            if(fileSelect==null) {
                availableDownloadFiles = getFileNames(driver);
                driver.close();
                return new CommandResult(availableDownloadFiles);
            }

            initializeDownloadFolder();
            try{
                downloadFiles(driver);
            }
            catch(IndexOutOfBoundsException iobe){
                throw new CommandException(MESSAGE_FILE_DOES_NOT_EXIST_ERROR);
            }
            dynamicWaiting();
            driver.close();
            try{
                UnzipUtil.unzipFile(downloadPath, UNZIP_FILE_KEYWORD,
                        currentDirPath, DOWNLOAD_FILE_PATH, moduleCode);

            } catch (IOException ioe) {
                throw new CommandException(MESSAGE_FILE_CORRUPTED);
            }
            return new CommandResult(moduleCode + MESSAGE_SUCCESS
                    + currentDirPath + DOWNLOAD_FILE_PATH);
        }
        driver.close();
        throw new CommandException(MESSAGE_MODULE_NOT_FOUND);
    }

    /**
     * If user did not enter specified file name after the PREFIX_SELECT_FILES, program will search all available
     * files of the selected module and parse it into a string.
     * @param driver is the current existing WebDriver session
     * @return A string parsed with all the available files from that module.
     */

    private String getFileNames(WebDriver driver){
        WebElement treeview = driver.findElement(By.className(TREEVIEW_CLASS_ID));
        List<WebElement> fileResult = treeview.findElements(By.cssSelector(WORKBIN_CSS_SELECTOR_ID));
        String result= new String();
        for (int i=0; i<fileResult.size(); i++) {
            result+=(i + ": " + fileResult.get(i).getText() + NEWLINE_SEPERATOR);
            //below statements are for debug. todo: remove when publishing
            //System.out.println(fileResult.get(i).getText()); // filename
            //System.out.println(fileResult.get(i).getAttribute("href")); // link
        }
        return result;
    }

    /**
     * Download files, download all the selected files mentioned after the PREFIX_SELECTED_FILES to the specified
     * download location
     * @param driver is the current existing WebDriver session
     */

    protected void downloadFiles(WebDriver driver){
        WebElement treeview = driver.findElement(By.className(TREEVIEW_CLASS_ID));
        List<WebElement> fileResult = treeview.findElements(By.cssSelector(WORKBIN_CSS_SELECTOR_ID));
        for(int fileID:fileSelect) {
            driver.get(fileResult.get(fileID).getAttribute(FILE_DOWNLOAD_LINK_ATTRIBUTE_ID));
        }
    }
}
