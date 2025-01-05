package Ai.Attri.Library;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class Constants {
	public static WebDriver driver;


	public Locale getLocale() {
		String locale = System.getProperty("Locale");
		if(locale.equalsIgnoreCase("ja_JP")) return Locale.JAPAN;	
		else if(locale.equalsIgnoreCase("en_US")) return Locale.US;
		else return Locale.US;
	}
	
	public String getRBPath(String className) {
		return "\\com\\arisglobal\\LSHE2\\ResourceBundles\\LSRIMS\\"+className.split(".LSRIMS.")[1].replace("PageObjects", "").replaceAll("\\.", "\\\\");
	}
	
	private static ThreadLocal<ExtentTest> extentNode = new ThreadLocal<ExtentTest>();
	
	public void setExtentNode(ExtentTest ExtentTest) {
		extentNode.set(ExtentTest); 
	}
	
	public ExtentTest getExtentNode() {
		return extentNode.get(); 
	}
	
	public void destroyExtentNode() {
		extentNode.remove(); 
	}
	
	public String getReportsPath() {
		return System.getProperty("user.dir") + "\\test-output\\" + System.getProperty("ReportFolderTime")+"\\";
	}	
	public String getDownloadPath() {
		return System.getProperty("user.dir") + "\\test-output\\";
	}
	
	public String getProductName() {
		return "Attri";
	}
	
	public boolean CaptureDesktopScreenshot() {
		if(System.getProperty("CaptureDesktopScreenshot").equalsIgnoreCase("TRUE"))return true;
		else return false;
	}
	
	public String getInternallyApprovedChangesColor() {
		return "rgba(255, 145, 70, 1)";
	}
	
	public String getAddActionColor() {
		return "rgba(144, 238, 144, 1)";
	}
	
	public String getUpdateActionColor() {
		return "rgba(255, 255, 153, 1)";
	}
	
	public String getDeleteActionColor() {
		return "rgba(255, 127, 127, 1)";
	}
	
	public String getNotHighlightedColor() {
		return "rgba(255, 255, 255, 1)";
	}	
	
	public static String LSRIMS_Output = System.getProperty("user.dir")
            + "\\test-output\\" + System.getProperty("ReportFolderTime")+"\\";	
}
