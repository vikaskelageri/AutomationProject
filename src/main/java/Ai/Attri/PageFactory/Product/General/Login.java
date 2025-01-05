package Ai.Attri.PageFactory.Product.General;

import java.awt.AWTException;
import java.io.IOException;
import java.time.Duration;

import Ai.Attri.Library.SeleniumActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import Ai.Attri.PageObjects.Product.General.AuditTrialPageObjects;
import Ai.Attri.PageObjects.Product.General.LoginPageObjects;

public class Login {
    WebDriver driver;
    WebDriverWait wait;
    LoginPageObjects loginPageObjects;
    Reports reports;
    AuditTrialPageObjects auditTrialPageObjects;

    Constants constants = new Constants();
    SeleniumActions seleniumActions;

    public Login(WebDriver driver, LoginPageObjects loginPageObjects, AuditTrialPageObjects auditTrialPageObjects) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.loginPageObjects = loginPageObjects;
        this.auditTrialPageObjects = auditTrialPageObjects;
        reports = new Reports();
        this.seleniumActions = new SeleniumActions();
    }

    public void launchURL(String URL) throws IOException, AWTException, InterruptedException {
        driver.get(URL);


        }
    public void some() throws IOException, AWTException, InterruptedException {
        loginPageObjects.attri.click();

    }

}