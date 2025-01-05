package Ai.Attri.Library;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;


public class SeleniumActions {
	
	Constants constants;
	Reports reports;

	
	
	public SeleniumActions() {
	
		constants = new Constants();
		reports = new Reports();
		
	}
	
	public void downloadPDFinListing(WebDriver driver) throws InterruptedException, IOException, AWTException {
		String parent=driver.getWindowHandle();
		Thread.sleep(3000);
		Set<String>s=driver.getWindowHandles();
		
		Iterator<String> I1= s.iterator();

		while(I1.hasNext()){

		String child_window=I1.next();
		
		if(!parent.equals(child_window)){
			driver.switchTo().window(child_window);
			Thread.sleep(10000);
			reports.ExtentReportLog(constants.getReportsPath(),constants.getExtentNode(), driver,Status.INFO ,"File Download",true,constants.CaptureDesktopScreenshot());
			driver.close();
		}
	}	
		driver.switchTo().window(parent);
    }
	
	public void UploadDocuments(String DocumentPath) throws InterruptedException, AWTException {
		StringSelection stringSelection = new StringSelection(DocumentPath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = null;
		Robot robot1 = null;
		Robot robot2 = null;
		robot = new Robot();
		robot1 = new Robot();
		robot2 = new Robot();
		Thread.sleep(3000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot1.keyPress(KeyEvent.VK_V);
		robot1.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(3000);
		robot2.keyPress(KeyEvent.VK_ENTER);
		robot2.keyRelease(KeyEvent.VK_ENTER);
	}
	
	public void pageRefresh() throws InterruptedException, AWTException {
		Robot robot = null;
		Robot robot1 = null;
		Robot robot2 = null;
		robot = new Robot();
		robot1 = new Robot();
		robot2 = new Robot();
		Thread.sleep(3000);
		robot.keyPress(KeyEvent.VK_F5);
		robot1.keyPress(KeyEvent.VK_F5);
//		robot1.keyRelease(KeyEvent.VK_V);
//		robot.keyRelease(KeyEvent.VK_CONTROL);
//		Thread.sleep(3000);
//		robot2.keyPress(KeyEvent.VK_ENTER);
//		robot2.keyRelease(KeyEvent.VK_ENTER);
	}
	
	public boolean isVisible(WebElement webElement) {

		try {
			boolean visible;
			visible = webElement.isDisplayed();
			return visible;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isSelected(WebElement webElement) {

		try {
			boolean enabled;
			enabled = webElement.isSelected();
			return enabled;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isEnabled(WebElement webElement) {

		try {
			boolean selected;
			selected = webElement.isEnabled();
			return selected;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void ScrollTillElement(WebDriver driver,WebElement webElement) {
	
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
	
	}
	
	/**
     * @objective: To highlight the object in the background in yellow
	 * @author: Amruth
	 * @date: 10-May-2022
     * @return boolean
  	  *@Note: Use with caution! Only where needed !, As this modifies the web element via java script and can lead to UI rendering issues..
     */
	public void highlightObjects(WebElement webElement, WebDriver driver) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;  
		jsExecutor.executeScript("arguments[0].style.background='yellow'",webElement);	
	}
	
	public void jsClickElement(WebDriver driver,WebElement webElement) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
	}
	
	public void switchToFirstChildWindow(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		Set<String> allWindows = driver.getWindowHandles();
			for (String currentWindowHandle : allWindows) {
				if (!currentWindowHandle.equals(parentWindow)) {
					driver.switchTo().window(currentWindowHandle);
					driver.manage().window().maximize();
					Thread.sleep(3000);
				}
			}
	}
	
	public void switchToParentWindow(WebDriver driver, String parentWindow) throws InterruptedException {
		driver.switchTo().window(parentWindow);
	}
	
	public void Scrolldown(WebDriver driver) {
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)", "");
	
	}
	
	public void ScrollUp(WebDriver driver) {
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
	
	}
	
	public void setValue(WebElement webElement, String value) {
		if (!value.trim().equalsIgnoreCase("#skip#")) {
			webElement.clear();
			webElement.sendKeys(value);
		}
	}
}
