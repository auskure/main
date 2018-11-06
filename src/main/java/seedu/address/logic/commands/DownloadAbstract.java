package seedu.address.logic.commands;
//@@author BearPerson1

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static seedu.address.commons.util.FileUtil.currentDirectory;

/**
 * DownloadAbstract is an abstract class that does the basic setting up of Selenium chrome drivers. Implementation of
 * DownloadAllNotesCommand and DownloadSelectNotesCommand extends on this class
 * <p>
 * DownloadAbstract extends on the command class
 */

public abstract class DownloadAbstract extends Command {

    protected static final String MAC_CHROME_DRIVER_DIRECTORY = "macChromeDriver";

    protected static final String MAC_CHROME_DRIVER_NAME = "chromedriver";

    protected static final String WINDOWS_CHROME_DRIVER_DIRECTORY = "windowsChromeDriver";

    protected static final String WINDOWS_CHROME_DRIVER_NAME = "chromedriver.exe";

    protected static final String DOWNLOAD_FILE_PATH = "/notes";

    protected static final String IVLE_TITLE = "IVLE";

    protected static final String WINDOWS_OS_NAME = "Windows";

    protected static final String MAC_OS_NAME = "Mac";

    protected static final String IVLE_ADDRESS = "https://ivle.nus.edu.sg";

    protected static final String DOWNLOAD_FILE_ONGOING_EXTENSION = "crdownload";

    protected static final String IVLE_USERNAME_FIELD_ID = "ctl00_ctl00_ContentPlaceHolder1_userid";

    protected static final String IVLE_PASSWORD_FIELD_ID = "ctl00_ctl00_ContentPlaceHolder1_password";

    protected static final String IVLE_LOGIN_BUTTON_ID = "ctl00_ctl00_ContentPlaceHolder1_btnSignIn";

    protected static final String IVLE_DOWNLOAD_PAGE_ADDRESS = "https://ivle.nus.edu.sg/v1/File/download_all.aspx";

    protected static final String IVLE_MODULE_LIST_FIELD_ID =
            "ctl00_ctl00_ctl00_ContentPlaceHolder1_ContentPlaceHolder1_ContentPlaceHolder1_ddlModule";

    public static final String NEWLINE_SEPERATOR = "\r\n";

    protected String username;
    protected String password;
    protected String moduleCode;
    protected String currentDirectoryPath;
    protected String downloadPath;
    protected boolean isDownloadDisabled = true;

    public DownloadAbstract(String username, String password, String moduleCode) {
        this.username = username;
        this.password = password;
        this.moduleCode = moduleCode.toLowerCase();
        this.currentDirectoryPath = currentDirectory();
        this.downloadPath = currentDirectoryPath + DOWNLOAD_FILE_PATH;
    }

    /**
     * extractFilesFromJar makes the notes folder to store the notes for the user if it doesnt exists.
     * as well as extract the relevant chromedriver files from inside the jar to outside the jar to be able to be used.
     * <p>
     * this method should only run when running from a fresh jar file.
     *
     * @throws IOException
     */


    protected void extractFilesFromJar() throws IOException {
        File notesFolder = new File(currentDirectoryPath + DOWNLOAD_FILE_PATH);
        if (!notesFolder.exists()) {
            notesFolder.mkdir();
        }
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(WINDOWS_CHROME_DRIVER_DIRECTORY + "/"
                + WINDOWS_CHROME_DRIVER_NAME);
        File chromeDriverDir = new File(WINDOWS_CHROME_DRIVER_DIRECTORY);
        if (!chromeDriverDir.exists()) {
            chromeDriverDir.mkdirs();
        }
        File windowsChromeDriver = new File(WINDOWS_CHROME_DRIVER_DIRECTORY
                + File.separator + WINDOWS_CHROME_DRIVER_NAME);
        if (!windowsChromeDriver.exists()) {
            windowsChromeDriver.createNewFile();
            org.apache.commons.io.FileUtils.copyURLToFile(resource, windowsChromeDriver);
        }
        resource = classLoader.getResource(MAC_CHROME_DRIVER_DIRECTORY + "/" + MAC_CHROME_DRIVER_NAME);
        chromeDriverDir = new File(MAC_CHROME_DRIVER_DIRECTORY);
        if (!chromeDriverDir.exists()) {
            chromeDriverDir.mkdirs();
        }
        File macChromeDriver = new File(MAC_CHROME_DRIVER_DIRECTORY + File.separator
                + MAC_CHROME_DRIVER_NAME);
        if (!macChromeDriver.exists()) {
            macChromeDriver.createNewFile();
            org.apache.commons.io.FileUtils.copyURLToFile(resource, macChromeDriver);
        }
    }

    /**
     * initializeChromeDriverPaths dynamically sets the download path of the files and location of chromeDriver
     * so that its relative to where this project is stored and what OS the user is using.
     * <p>
     * downloadPath will change from the root directory location of the application to the
     * location of the tempDownloadStorage
     */
    protected void initializeChromeDriverPaths() {
        if (System.getProperty("os.name").contains(WINDOWS_OS_NAME)) {
            System.setProperty("webdriver.chrome.driver", currentDirectoryPath + "/" +
                    WINDOWS_CHROME_DRIVER_DIRECTORY + "/" + WINDOWS_CHROME_DRIVER_NAME);
        } else if (System.getProperty("os.name").contains(MAC_OS_NAME)) {
            System.setProperty("webdriver.chrome.driver", currentDirectoryPath + "/" + MAC_CHROME_DRIVER_DIRECTORY + "/"
                    + MAC_CHROME_DRIVER_NAME);
        }
    }

    /**
     * initializeWebDriver sets the download path of the chromeDriver
     * Additionally, chromeDriver has disabled headless downloading as a new security feature, thus the alternative
     * to having the chrome windows blocking the UI is to shift it to a unviewable location at the bottom of the screen.
     */

    protected WebDriver initializeWebDriver() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadPath);
        chromePrefs.put("browser.setDownloadBehavior", "allow");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setPosition(new Point(-2000, 0));
        return driver;
    }

    /**
     * isModuleExisting checks if the module explicitly provided by the user is Available to the user.
     *
     * @param driver is the current existing WebDriver session
     * @return true if found, false if not.
     */

    protected boolean isModuleExisting(WebDriver driver) {
        driver.get(IVLE_DOWNLOAD_PAGE_ADDRESS);
        Select dropDown = new Select(driver.findElement(By.id(IVLE_MODULE_LIST_FIELD_ID)));
        List<WebElement> itemsModules = dropDown.getOptions();
        int itemCount = itemsModules.size();
        /**
         *i starts at 1 because 0 is reserved for "select module"
         *an iterator is used because the dropDown element is selected by index,
         *thus search is more logical to be sequential.
         */
        for (int i = 1; i < itemCount; i++) {
            if (isModuleMatching(itemsModules.get(i).getText().toLowerCase())) {
                moduleCode = itemsModules.get(i).getText();
                dropDown.selectByIndex(i);
                return true;
            }
        }
        return false;
    }

    /**
     * isModuleMatching is a helper function of isModuleExisting, it iterates through the moduleCode as provided by
     * the user, and checks it character by character against all the mods that IVLE displays.
     *
     * @param input is the string checked against the mod field that the user provided
     * @return true if it exists on IVLE, else not.
     */

    protected boolean isModuleMatching(String input) {
        try {
            for (int i = 0; i < moduleCode.length(); i++) {
                if (input.charAt(i) != moduleCode.charAt(i)) {
                    return false;
                }
            }
            return true;
        } catch (StringIndexOutOfBoundsException sioobe) {
            return false;
        }
    }

    /**
     * isLoggedIn checks if user has successfully logged in with the provided credentials.
     */

    protected boolean isLoggedIn(WebDriver driver) {
        return !(driver.getTitle().contains(IVLE_TITLE));
    }

    /**
     * loginIvle attempts to login to IVLE with the provided credentials.
     *
     * @param driver is the current WebDriver session
     */

    protected void loginIvle(WebDriver driver) {

        driver.get(IVLE_ADDRESS);
        driver.findElement(By.id(IVLE_USERNAME_FIELD_ID)).sendKeys(username);
        driver.findElement(By.id(IVLE_PASSWORD_FIELD_ID)).sendKeys(password);
        driver.findElement(By.id(IVLE_LOGIN_BUTTON_ID)).click();
    }

    protected abstract void downloadFiles(WebDriver driver);

    /**
     * dynamicWaiting implements "busy waiting" to prevent premature termination of chromeDriver in event that
     * the file download size requires more time than the default timeout of chromeDriver
     */

    protected void dynamicWaiting() throws InterruptedException {
        String[] keyExtentions = {DOWNLOAD_FILE_ONGOING_EXTENSION};
        do {
            Thread.sleep(100);
        } while (!org.apache.commons.io.FileUtils.listFiles
                (new File(downloadPath), keyExtentions, false).isEmpty());
    }

    /**
     * initializeDownloadFolder deletes instances of "downloading" file types, to prevent dynamicWaiting running
     * indefinitely
     */

    protected void initializeDownloadFolder() {
        File folder = new File(downloadPath);
        File[] filesList = folder.listFiles();

        for (int i = 0; i < filesList.length; i++) {
            File currentFile = filesList[i];
            if (currentFile.getName().endsWith(DOWNLOAD_FILE_ONGOING_EXTENSION)) {
                filesList[i].delete();
            }
        }
    }
}
