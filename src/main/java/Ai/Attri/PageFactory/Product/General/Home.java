package Ai.Attri.PageFactory.Product.General;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import Ai.Attri.PageObjects.Product.General.HomePageObject;


public class Home {

	WebDriver driver;
	WebDriverWait wait;
	HomePageObject homePageObject;
	Reports reports;

	Constants constants = new Constants();

	public Home(WebDriver driver, HomePageObject homePageObject) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.homePageObject = homePageObject;
		reports = new Reports();
	}

	public void switchTo(String value) {


		switch (value) {
		case "LSRIMS":
			
			homePageObject.appLauncher.click();
			wait.until(ExpectedConditions.visibilityOf(homePageObject.lsrimsLink));
			homePageObject.lsrimsLink.click();
			break;
		case "Platform":

			homePageObject.appLauncher.click();
			wait.until(ExpectedConditions.visibilityOf(homePageObject.platformLink));
			homePageObject.platformLink.click();
			wait.until(ExpectedConditions.titleContains("Action Trail Report - Platform"));
			break;
		default:
			break;
			
			
		}
	}
}
