package Ai.Attri.PageFactory.Product.General;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;

import Ai.Attri.Library.SeleniumActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.PageObjects.Product.General.AuditTrialPageObjects;
import Ai.Attri.PageObjects.Product.General.NavigatePageObjects;

public class Navigate {
    WebDriver driver;
    WebDriverWait wait;
    NavigatePageObjects navigatePageObjects;
    Actions actions;

    AuditTrialPageObjects auditTrialPageObjects;
    SeleniumActions seleniumActions;

    public Navigate(WebDriver driver, NavigatePageObjects navigatePageObjects, AuditTrialPageObjects auditTrialPageObjects) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.navigatePageObjects = navigatePageObjects;

        actions = new Actions(driver);
        this.seleniumActions = new SeleniumActions();
    }



    public void navigateToSubtabDocuments(String Tab) throws InterruptedException, IOException, AWTException {
        Thread.sleep(5000);
        driver.switchTo().defaultContent();

        Thread.sleep(3000);
        navigatePageObjects.getCodelistSubtab(Tab).click();
        driver.switchTo().defaultContent();

    }

 

}
