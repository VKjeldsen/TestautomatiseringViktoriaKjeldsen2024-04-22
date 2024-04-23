package stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {

    private WebDriver driver;

    private static void click(WebDriver driver, By by) {
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
    }

    @Given("I am using {string} as a browser")
    public void iAmUsingAsABrowser(String browser) {
        if (browser.equals("edge")) {
            driver = new EdgeDriver();
        } else if (browser.equals("chrome")) {
            driver = new ChromeDriver();
        }
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
        driver.manage().window().maximize();
    }

    @And("the user is of legal age")
    public void theUserIsOfLegalAge() {
        WebElement dateBirth = driver.findElement(By.id("dp"));
        dateBirth.sendKeys("10/04/2000");

        //Klickar i 18+ check box
        click(driver, By.cssSelector("#signup_form > div:nth-child(12) > div > div:nth-child(2) > div.md-checkbox.margin-top-10 > label"));
    }

    @And("I enter a first and {string} name")
    public void iEnterAFirstAndName(String lastName) {
        WebElement firstName = driver.findElement(By.id("member_firstname"));
        firstName.sendKeys("Göran");

        if (!lastName.isEmpty()) {
            WebElement last = driver.findElement(By.id("member_lastname"));
            last.sendKeys("Svensson");
        }
    }

    @And("an email is entered and confirmed")
    public void anEmailIsEnteredAndConfirmed() {
        int randEmail = (int) (Math.random() * 999001) + 1000;

        WebElement email = driver.findElement(By.id("member_emailaddress"));
        email.sendKeys("göran" + randEmail + "@mail.com");

        WebElement confirm = driver.findElement(By.id("member_confirmemailaddress"));
        confirm.sendKeys("göran" + randEmail + "@mail.com");
    }

    @And("a password is entered and {string}")
    public void aPasswordIsEnteredAnd(String retyped) {
        int randPassword = (int) (Math.random() * 999001) + 1000;

        WebElement password = driver.findElement(By.id("signupunlicenced_password"));
        password.sendKeys("Pas$W0rd" + randPassword);

        if (retyped.equals("different")) {
            int randPasswordRetype = (int) (Math.random() * 999001) + 1000;
            WebElement retypedPassword = driver.findElement(By.id("signupunlicenced_confirmpassword"));
            retypedPassword.sendKeys("Pas$W0rd" + randPasswordRetype);
            driver.findElement(By.cssSelector("body > div.bg-met > div.page-wrapper > div")).click();
            //Ovan lade jag till ett klick utanför formuläret då varning kom upp före klick på join vilket verkade störa processen --> nedan kunde inte klickas i
        } else if (retyped.equals("same")) {
            WebElement retypedPassword = driver.findElement(By.id("signupunlicenced_confirmpassword"));
            retypedPassword.sendKeys("Pas$W0rd" + randPassword);
        }
    }

    @And("I {string} agree to the Terms and Conditions")
    public void iAgreeToTheTermsAndConditions(String action) {
        if (action.equals("do")) {
            click(driver, By.cssSelector("#signup_form > div:nth-child(12) > div > div:nth-child(2) > div:nth-child(1) > label"));
        }
    }

    @And("agree to the code of ethics and conduct")
    public void agreeToTheCodeOfEthicsAndConduct() {
        click(driver, By.cssSelector("#signup_form > div:nth-child(12) > div > div:nth-child(7) > label"));
    }

    @When("I click the Confirm and Join button")
    public void iClickTheConfirmAndJoinButton() {
        click(driver, By.name("join"));
    }

    @Then("a {string} will show and the registration process will end")
    public void aWillShowAndTheRegistrationProcessWillEnd(String text) {
        WebElement element;
        if (text.equals("Last Name is required")) {
            element = driver.findElement(By.cssSelector("#signup_form > div:nth-child(6) > div:nth-child(2) > div > span > span"));
        } else if (text.equals("Password did not match")) {
            element = driver.findElement(By.cssSelector("#signup_form > div:nth-child(9) > div > div.row > div:nth-child(2) > div > span > span"));
        } else if (text.equals("You must confirm that you have read and accepted our Terms and Conditions")) {
            element = driver.findElement(By.cssSelector("#signup_form > div:nth-child(12) > div > div:nth-child(2) > div:nth-child(1) > span > span"));
        } else {
            element = driver.findElement(By.cssSelector("body > div > div.page-content-wrapper > div > h2"));
            //Om allt går igenom --> "THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND"
        }
        String actual = element.getText();
        assertEquals(text, actual);
    }
}
