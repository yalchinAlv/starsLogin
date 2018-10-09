import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author Yalchin Aliyev
 *
 */
public class StarsLogin {

    // get stars username and password from environmental variables
    private final static String STARS_USERNAME = System.getenv("stars_username");
    private final static String STARS_PASSWORD = System.getenv("stars_password");

    // get bilkent webmail username and password from environmental variables
    private final static String WEBMAIL_USERNAME = System.getenv("webmail_username");
    private final static String WEBMAIL_PASSWORD = System.getenv("webmail_password");
    private final static String WEBMAIL_HOST = "mail.bilkent.edu.tr";

    // location of chomedriver.exe
    private final static String DRIVER_LOC = "C:\\WebDrivers\\chromedriver.exe";

    public static void main(String[] args) {

        // set the location of chromedriver.exe
        System.setProperty("webdriver.chrome.driver", DRIVER_LOC);

        // set options for chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");

        // start chrome driver
        WebDriver driver = new ChromeDriver(options);

        try {

            driver.get("https://stars.bilkent.edu.tr/srs");

            // enter stars username and password
            driver.findElement(By.id("LoginForm_username")).sendKeys(STARS_USERNAME);
            driver.findElements(By.tagName("input")).get(1).sendKeys(STARS_PASSWORD);

            // click Login button
            driver.findElement((By.tagName("button"))).click();

            // wait for email to be sent
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // get two-factor auth code from email
            int code = CheckEmail.getCode(WEBMAIL_HOST, WEBMAIL_USERNAME, WEBMAIL_PASSWORD);

            if (code > 0) {
                driver.findElement(By.id("EmailVerifyForm_verifyCode")).sendKeys(code + "");
                driver.findElement(By.tagName("button")).click();
            } else {
                System.err.println("Code not found!");
                driver.quit();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            driver.quit();
        }
    }
}
