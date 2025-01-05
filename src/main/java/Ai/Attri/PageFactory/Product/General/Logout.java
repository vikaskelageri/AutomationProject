package Ai.Attri.PageFactory.Product.General;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import Ai.Attri.PageObjects.Product.General.AuditTrialPageObjects;
import Ai.Attri.PageObjects.Product.General.LogoutPageObjects;

public class Logout {
	WebDriver driver;
	WebDriverWait wait;
	LogoutPageObjects logoutPageObjects;
	Reports reports;
	AuditTrialPageObjects auditTrialPageObjects;
	Constants constants = new Constants();
	public Logout(WebDriver driver,LogoutPageObjects logoutPageObjects,AuditTrialPageObjects auditTrialPageObjects){
		this.driver = driver;
		wait= new WebDriverWait(driver, Duration.ofSeconds(30));
		this.logoutPageObjects = logoutPageObjects;
		reports = new Reports();
	}


}
