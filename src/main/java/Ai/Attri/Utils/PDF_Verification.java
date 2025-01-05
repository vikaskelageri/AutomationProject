package Ai.Attri.Utils;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import com.aventstack.extentreports.Status;
import com.testautomationguru.utility.CompareMode;
import com.testautomationguru.utility.PDFUtil;

public class PDF_Verification {
	Reports reports;
	WebDriver driver;
	WebDriverWait wait;
	Constants constants = new Constants();
	public PDF_Verification(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.reports = new Reports();

	}
	public boolean verifyPDFContent(String path, String data) throws IOException {

		PDDocument pd = null;
		String result = "";
		try {
			pd = PDDocument.load(new File(path));
			System.out.println("total no pages:" + pd.getNumberOfPages());
			PDFTextStripper pdf = new PDFTextStripper();
			result= pdf.getText(pd);
			pd.close();		
		} catch (IOException e) {
			throw e;			
		}
		finally {
			try {
				pd.close();
			} catch (Exception e1) {
			}
		}
		return result.contains(data);
	}
	
	public boolean getPDFVerificationStatus(String path, String data) throws IOException {

		boolean status = false;
		PDDocument pd = null;
		try {

			pd = PDDocument.load(new File(path));
			System.out.println("total no pages:" + pd.getNumberOfPages());
			PDFTextStripper pdf = new PDFTextStripper();
			if (pdf.getText(pd).contains(data)) {
				
				status = true;
			} else {

				status = false;
			}		
			

		} catch (IOException e) {
			throw e;
			
		}
		finally {
			try {
				pd.close();
			} catch (Exception e1) {
			}
		}
		return status;
	}

	/*********************************************************************************************************
	 * @Objective: The below method is created to verify data is visible in
	 *             downloaded report and vice versa
	 * @Parameters: path ,data ,IsVisbleVerification
	 * @Output- 
	 * @author: Yashwanth Naidu
	 * @return 
	 * @Date: 03-Sept-2021
	 * @Updated by and when:
	 **********************************************************************************************************/
	public boolean pdfContentVerification(String path, ArrayList<String> data , boolean IsVisbleVerification)throws InvalidPasswordException, IOException{
		ArrayList<String> pass =new ArrayList<String>();
		ArrayList<String> fail =new ArrayList<String>();
		boolean finalResult = false;
		PDDocument pd;
		pd = PDDocument.load(new File(path));
		PDFTextStripper pdf = new PDFTextStripper();
		try {
			String result = pdf.getText(pd);
			for (int i = 0 ; i<data.size() ; i ++) {
				if(result.contains(data.get(i))) {
					pass.add(data.get(i));					
				}else {
					fail.add(data.get(i));
				}
			}

			if(IsVisbleVerification) {

				if(pass.size() > 0) {
					return finalResult = true;

				}
				if(fail.size() > 0) {

					return finalResult = false;
				}
			}else {
				if(fail.size() > 0) {
					return finalResult = false;
				}
				if(pass.size() > 0) {
					return finalResult = true;
				}
			}
		} 
		catch (InvalidPasswordException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		finally {
			pd.close();
		}
		return finalResult;
	}
	
	/*********************************************************************************************************
	 * @Objective: The below method is created to verify literature reference data is visible in
	 *             downloaded report 
	 * @Parameters: path ,data ,IsVisbleVerification,litReferencevalue1,litReferencevalue2
	 * @Output- 
	 * @author: Gokul R
	 * @throws IOException 
	 * @throws InvalidPasswordException 
	 * @Date: 21-Mar-2022
	 * @Updated by and when:
	 **********************************************************************************************************/

	public boolean PdfVerifyForMultipleValuesinSameField(String path, String valueToVerify, ArrayList<String> input,String fieldName) throws InvalidPasswordException, IOException {

		pdfContentVerification(path, input, true);
		ArrayList<String> pass =new ArrayList<String>();
		ArrayList<String> fail =new ArrayList<String>();
		boolean finalResult = false;
		PDDocument pd;
		pd = PDDocument.load(new File(path));
		PDFTextStripper pdf = new PDFTextStripper();
		try {
			String result = pdf.getText(pd);
			for (int i = 0 ; i<input.size() ; i ++) {
				if(result.contains(input.get(i))) {
					pass.add(input.get(i));					
				}else {
					fail.add(input.get(i));
				}
			}

			if(pass.size() > 0) {
				return finalResult = true;
			}
			if(fail.size() > 0) {
				return finalResult = false;
			}     				     					

		}catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return finalResult;
	}
	
	/*********************************************************************************************************
	 * @Objective: The below method is created to verify data is visible in downloaded report. 
	 * It takes arraylist of inputs, which can contain comma for when the data is comming in new line,
	 * function will split based on comma, check he split strings, and add them back while printing.				
	 * @Parameters: path ,data ,IsVisbleVerification
	 * @Output- 
	 * @author: Yashwanth Naidu
	 * @throws AWTException 
	 * @throws InterruptedException 
	 * @Date: 05-oct-2021
	 * @Updated by and when:
	 **********************************************************************************************************/
	public Boolean pdfContentVerificationCommaSplittedForNewLine(String path, ArrayList<String> data , boolean IsVisbleVerification,String splitter)throws InvalidPasswordException, IOException, AWTException, InterruptedException{
		ArrayList<String> pass =new ArrayList<String>();
		ArrayList<String> fail =new ArrayList<String>();
		Boolean success = true;
		PDDocument pd;
		pd = PDDocument.load(new File(path));
		PDFTextStripper pdf = new PDFTextStripper();
		try {
			String result = pdf.getText(pd);
			for (int i = 0 ; i<data.size() ; i ++) {
				String[] dataS = data.get(i).split(splitter);
				Boolean match = true;
				for(String d:dataS){
					if(result.contains(d)) {								
					}else {
						match = false;
						break;
					}
				}
				if(match)pass.add(data.get(i).replaceAll(splitter," "));
				else fail.add(data.get(i).replaceAll(splitter," "));

			}			
			if(IsVisbleVerification) {

				for(String p:pass){
				
					reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "'" + p + "' Data is visible in PDF report successfully.", false,
							constants.CaptureDesktopScreenshot());
				}
				for(String f:fail){
					success= false;
					reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "'" + f + "' Data is visible in PDF report FAILED.", false,
							constants.CaptureDesktopScreenshot());
				}
				return success;
			}
			else {
				for(String p:pass){
					success= false;
					reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "'" + p + "' Data is NOT visible in PDF report FAILED.", false,
							constants.CaptureDesktopScreenshot());
				}
				for(String f:fail){
					
					reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(), driver, Status.INFO, "'" + f + "' Data is NOT visible in PDF report successfully.", false,
							constants.CaptureDesktopScreenshot());
				}
				return success;
			}
		} 
		catch (InvalidPasswordException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		finally {
			pd.close();
		}
	}
	
	/**
	 * This can be used to compare 2 pdf and rturn true to false, and for visual compare mode it will highlight diff in an image file
	 * @param Path1 file1 path wrt framework directory
	 * @param Path2 file2 path wrt framework directory
	 * @param ResultPath for visual mode to see the differences set the folder path
	 * @param mode CompareMode.TEXT_MODE or CompareMode.VISUAL_MODE
	 * @return true to false
	 * @throws IOException
	 */
	public boolean pdfCompare(String Path1, String Path2, String ResultPath, CompareMode mode) throws IOException {
		PDFUtil pdfUtil = new PDFUtil();
		String file1=System.getProperty("user.dir")+Path1;
		String file2=System.getProperty("user.dir")+Path2;
		System.out.println("File 1 Page Count:"+pdfUtil.getPageCount(file1)+" File 2 PageCount:"+ pdfUtil.getPageCount(file2));
		//System.out.println("File 1 Page Content:"+pdfUtil.getText(file1, 1,1)+" File 2 Page Content:"+pdfUtil.getText(file2, 1,1));
		pdfUtil.setCompareMode(mode);
		pdfUtil.highlightPdfDifference(true);
		pdfUtil.compareAllPages(true);
		//pdfUtil.enableLog();
		pdfUtil.setImageDestinationPath(System.getProperty("user.dir")+ResultPath);		
		return pdfUtil.compare(file1, file2);
	}
	
	/**
	 * 
	 * @param Path1 file1 path wrt framework directory
	 * @param Path2 file2 path wrt framework directory
	 * @param start start page
	 * @param stop stop page
	 * @return true to false if page are same
	 * @throws IOException
	 */
	public boolean pdfComparePages(String Path1, String Path2, int start, int stop) throws IOException {
		PDFUtil pdfUtil = new PDFUtil();
		String file1=System.getProperty("user.dir")+Path1;
		String file2=System.getProperty("user.dir")+Path2;
		System.out.println("File 1 Page Count:"+pdfUtil.getPageCount(file1)+" File 2 PageCount:"+ pdfUtil.getPageCount(file2));
		//System.out.println("File 1 Page Content:"+pdfUtil.getText(file1, 1,1)+" File 2 Page Content:"+pdfUtil.getText(file2, 1,1));
		pdfUtil.setCompareMode(CompareMode.VISUAL_MODE);

		return pdfUtil.compare(file1, file2,start,stop);
	}
	
	/**
	 * Use this for printing the text of pdf in string format for a short page range ,for writing the regex pattern.
	 * Refer this link for help on how to write regex >https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
	 * and Use notepad++ or any online regex tester for testing the regex pattern.
	 * @param Path1 file path wrt framework directory
	 * @param Regex regex pattern to search
	 * @param expectedValue expected value to match
	 * @param start start page
	 * @param stop stop page
	 * @return true to false if the expected value matches the regex pattern search
	 * @throws IOException
	 * 
	 */
	public boolean pdfVerifySpecificPagesUsingRegex(String Path1, String Regex, String expectedValue,int start, int stop) throws IOException {
		PDFUtil pdfUtil = new PDFUtil();
		String file1=System.getProperty("user.dir")+Path1;
		String pageContent = pdfUtil.getText(file1, start, stop);
		System.out.println(pageContent);
		Pattern p = Pattern.compile(Regex);  
		Matcher m = p.matcher(pageContent);
		while(m.find()) {
			System.out.println(m.group());		
			if (m.group(1).equals(expectedValue))
				return true;
		}
		return false;		
	}
	
	/**
	 * 
	 * @param Path1 file path wrt framework directory
	 * @param Regex regex pattern to search
	 * @param expectedValue expected value to match
	 * @return true to false if the expected value matches the regex pattern search
	 * @throws IOException
	 */
	public boolean pdfVerifyAllPagesUsingRegex(String Path1, String Regex, String expectedValue) throws IOException{
		PDFUtil pdfUtil = new PDFUtil();
		Boolean status = false;
		String file1=System.getProperty("user.dir")+Path1;
		String pageContent="";
		try {
			pageContent = pdfUtil.getText(file1);
		} catch (IOException e) {
			throw e;
		}
		Pattern p = Pattern.compile(Regex);  
		Matcher m = p.matcher(pageContent);
		while(m.find()) {
			System.out.println(m.group(1));		
			if (m.group(1).equals(expectedValue)) {
				status=true;
//				Reports.ExtentReportLog("PDF Report Verification Is Successfull ", Status.INFO,
//						"'" + expectedValue + "' Data is visible in PDF report successfully.", false);
				break;
			}
		}
		if(!status) {
			status=true;
//			Reports.ExtentReportLog("PDF Report Verification Is Successfull ", Status.INFO,
//					"'" + expectedValue.toString() + "' Data is not visible in PDF report.", false);
		}
		return status;
	}
	
}
