import com.sun.javafx.PlatformUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class HotelBookingTest {

    WebDriver driver;

    //adding constructor to provide PageFactory class with driver so @FindBy can initialize elements
    public HotelBookingTest(){
        //Geting rid of the annoying notification popup
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        PageFactory.initElements(driver, this);
    }


    @FindBy(linkText = "Hotels")
    private WebElement hotelLink;

    @FindBy(id = "Tags")
    private WebElement localityTextBox;

    @FindBy(id = "SearchHotelsButton")
    private WebElement searchButton;

    @FindBy(id = "travellersOnhome")
    private WebElement travellerSelection;

    @FindBy(xpath = "//ul[@id='ui-id-1']/li[2]")
    private WebElement searchSuggestions;

    @FindBy(id = "CheckInDate")
    private WebElement checkInDateBox;

    @FindBy(className = "ui-state-highlight")
    private WebElement checkInDate;

    @FindBy(className = "ui-state-active")
    private WebElement checkOutDate;

    @FindBy(xpath = "(//h3[text()='Select your stay preferences:'])[2]")
    private WebElement stayPrefsText;

    @FindBy(xpath = "//div[@class='fRight']/button[2]")
    private WebElement viewDetailsButton;

    @FindBy(xpath = "//button[@class='button booking rate__book bookHotel']")
    private WebElement bookButton;

    @FindBy(id = "itineraryBtn")
    private WebElement itineraryButton;

    @FindBy(id = "username")
    private WebElement email;

    @FindBy(id = "subscribe_newsletter")
    private WebElement subscribeNewsletter;

    @FindBy(id = "LoginContinueBtn_1")
    private WebElement loginContinueButton;

    @FindBy(id = "contactSalutation")
    private WebElement salutation;

    @FindBy(id = "contactFirstName")
    private WebElement firstName;

    @FindBy(id = "contactLastName")
    private WebElement lastName;

    @FindBy(id = "mobileNumber")
    private WebElement mobileNumber;

    @FindBy(id = "specialRequest")
    private WebElement specialRequest;

    @FindBy(id = "travellerBtn")
    private WebElement continueButton;

    @FindBy(id = "paymentSubmit")
    private WebElement paymentSubmitButton;

    @Test
    public void shouldBeAbleToSearchForHotels() {
        setDriverPath();

        driver.get("https://www.cleartrip.com/");
        hotelLink.click();

        //Only entering few characters enough to find Indiranagar, Banglore and clicking the 1st result in dropdown after its ready to be clicked.
        Helper.explicitWaitforElement(driver, 5, localityTextBox).sendKeys("indir");
        Helper.waitForElementToBeClickable(driver, 10, searchSuggestions).click();

        //Select todays and tomorrows date in check-in and check-out respectively.
        checkInDateBox.click();
        checkInDate.click();
        Helper.waitForElementToBeClickable(driver, 5, checkOutDate).click();

        new Select(travellerSelection).selectByVisibleText("1 room, 2 adults");
        searchButton.click();

        //Since it takes a while to display listings, so waiting for a static text to appear to make sure its completely loaded.
        Helper.explicitWaitforElement(driver, 20, stayPrefsText);
        viewDetailsButton.click();

        /*...........

        Since i'm selecting todays and tomorrows dates for booking, sometimes after clicking the view details button
        it shows that there are no rooms available and there is no filter on the site to show listings with only available rooms.
        Hence, using do-while and catch conditions to keep trying the next listing until it finds a listing with an
        available room.

        .............
         */

        boolean success = false;
        int listingNumber = 2;

        do{
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
            try {
                Helper.waitForElementToBeClickable(driver, 20, bookButton).click();
                success = true;
            }catch (NoSuchElementException e){
                driver.close();
                driver.switchTo().window(tabs.get(0));
                driver.findElement(By.xpath("(//div[@class='fRight']/button[2])[" + listingNumber + "]")).click();
                listingNumber++;
            }
        } while (!success);


        //After selecting room click the continue adding traveler button
        itineraryButton.click();

        //Add traveler details
        Helper.explicitWaitforElement(driver, 5, email).sendKeys("anything@random.com");
        subscribeNewsletter.click();
        loginContinueButton.click();

        new Select(Helper.explicitWaitforElement(driver, 10, salutation)).selectByIndex(1);
        firstName.sendKeys("Anonymous");
        lastName.sendKeys("Bird");
        mobileNumber.sendKeys("canthavechars"); //phone number accepting chars without errors
        specialRequest.sendKeys("Blueberries!@#%$^%$^");
        continueButton.click();

        //Asserting that we have reached the payment page
        String actual = Helper.explicitWaitforElement(driver, 10, paymentSubmitButton).getAttribute("value");
        Assert.assertEquals(actual, "Make payment");

        driver.quit();

    }

    private void setDriverPath() {
        if (PlatformUtil.isMac()) {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }
        if (PlatformUtil.isWindows()) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        }
        if (PlatformUtil.isLinux()) {
            System.setProperty("webdriver.chrome.driver", "chromedriver_linux");
        }
    }



}
