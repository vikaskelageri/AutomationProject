package Ai.Attri.PageFactory.Product.General;

import java.awt.AWTException;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import Ai.Attri.Library.SeleniumActions;
import Ai.Attri.PageObjects.Product.General.AuditTrialPageObjects;
//import com.arisglobal.LSHE2.PageObjects.LSRIMS.General.CommonPageObjects;
import com.aventstack.extentreports.Status;

public class AuditTrial {

	WebDriver driver;
	WebDriverWait wait;
	AuditTrialPageObjects auditTrialPageObjects;
	Actions actions;
	SeleniumActions seleniumAction;
	Reports reports;
	Constants constants;
//	CommonPageObjects commonPageObjects;


	public AuditTrial(WebDriver driver, AuditTrialPageObjects auditTrialPageObjects) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.auditTrialPageObjects = auditTrialPageObjects;
		actions = new Actions(driver);
		seleniumAction = new SeleniumActions();
		reports = new Reports();
		constants = new Constants();
	}
	
	public void saveAudit(String reason)throws IOException, AWTException, InterruptedException {
		Thread.sleep(5000);
		
		wait.until(ExpectedConditions.visibilityOf(auditTrialPageObjects.auditTrailPopUp));
		auditTrialPageObjects.reasonCodeDropdownArrow.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		if (seleniumAction.isVisible(auditTrialPageObjects.reasonCodeDropdown)) {

			auditTrialPageObjects.reasonCodeDropdown.click();
			auditTrialPageObjects.reasonTextbox.sendKeys(reason);
			// CommonModule.takeScreenShot();
			auditTrialPageObjects.saveButton.click();
			Thread.sleep(10000);
			reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "",
					true, constants.CaptureDesktopScreenshot());
			if(seleniumAction.isVisible(auditTrialPageObjects.confirmPop)) {
				auditTrialPageObjects.yesBtn.click();
			}

			wait.until(ExpectedConditions.invisibilityOf(auditTrialPageObjects.auditTrailPopUp));
			//auditTrialPageObjects.infoMessageClose.click();
			Thread.sleep(2000);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		}
	}


}
