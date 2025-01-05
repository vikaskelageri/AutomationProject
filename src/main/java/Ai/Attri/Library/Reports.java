package Ai.Attri.Library;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import Ai.Attri.Utils.TakeSnapShotWithHeader;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class Reports {

	/**********************************************************************************************************
	 * @Objective:Log message,screenshot and status
	 * @author:Adarsh
	 * @throws AWTException,IOException 
	 * @throws InterruptedException 
	 * @Date : 06-21-2019
	 **********************************************************************************************************/
	public void ExtentReportLog(String ReportPath, ExtentTest extentTest, WebDriver driver, Status Status, String message, Boolean CaptureScreenshot,Boolean CaptureDesktopScreenshot) throws IOException, AWTException, InterruptedException {
	
	
		if (!message.contains("#skip#")) {
			if (CaptureScreenshot) {
				String imgpath = this.CaptureScreenshot(driver, ReportPath, CaptureDesktopScreenshot);				
				extentTest.log(Status, message,MediaEntityBuilder.createScreenCaptureFromPath(imgpath).build());				
			} else {
				extentTest.log(Status, message);
			}
		}
	}
	
	private String CaptureScreenshot(WebDriver driver, String path, Boolean CaptureDesktopScreenshot) throws IOException, AWTException, InterruptedException {
		String imgpath = Thread.currentThread().getId() + "-" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".png";
		String newpath = path + imgpath;
		File src = null;		
		if (CaptureDesktopScreenshot) {		
			CaptureDesktopScreenshot(newpath);
		} else {
			src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File(newpath));
		}	
		return imgpath;
	}
	private void CaptureDesktopScreenshot(String path) throws IOException, AWTException, InterruptedException {
		String newpath = null;
		File src = null;
		File output = new File(System.getProperty("user.dir")+"\\output.png");
		if (!output.exists()) {
		output.createNewFile(); // if file already exists will do nothing 
		}
		File snapshot = new File(System.getProperty("user.dir")+"\\snapshot.png");
		if (!snapshot.exists()) {
			snapshot.createNewFile(); // if file already exists will do nothing 
		}
		
		src = ((TakesScreenshot) Constants.driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File(System.getProperty("user.dir")+"\\snapshot.png"));
		String urlCurrentSession = Constants.driver.getCurrentUrl();
		new TakeSnapShotWithHeader().updateHeaderUrl(urlCurrentSession);
		new TakeSnapShotWithHeader().verticallyAttachSnapShot();
		Thread.sleep(2000);
		File srcUpdate = new File(System.getProperty("user.dir")+"\\output.png");
		FileUtils.copyFile(srcUpdate, new File(path));
		Thread.sleep(2000);
		
		/*Robot robot = new Robot();	String format = "png";
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);		
		ImageIO.write(screenFullImage, format, new File(path));	*/
		
		
	}

}
