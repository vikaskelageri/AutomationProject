package AI.Attri.StepDefinitions;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import AI.Attri.TestContext;
import Ai.Attri.Library.Constants;
import Ai.Attri.Library.Reports;
import Ai.Attri.Utils.DBUtil;
import Ai.Attri.Utils.FileSystemOperations;
import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class Hooks {
	public static ExtentReports extent = new ExtentReports();
	public ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	public ThreadLocal<Logger> logger = new ThreadLocal<Logger>();
	// public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	static Properties prop = new Properties();
	static Constants constants = new Constants();
	TestContext testContext;
	int stepNumber;
	List<PickleStepTestStep> testSteps;
	Reports reports = new Reports();
	static FileSystemOperations fileSystemOperations =new FileSystemOperations();
	public Hashtable<String, String> tempTable = new Hashtable<String, String>();

	static String testName="";
	public Hooks(TestContext context) {
		testContext = context;
	}

	@BeforeAll(order = 0)
	public static void setSystemProperties() throws FileNotFoundException, IOException {
		prop.load(new FileInputStream(System.getProperty("user.dir")
				+ "/src/test/resources/Ai.Attri/PROPERTIES/Scenario.properties"));

		System.setProperty("Locale", prop.getProperty("Locale"));
		System.setProperty("CaptureDesktopScreenshot", prop.getProperty("CaptureDesktopScreenshot"));
		System.setProperty("BROWSER", prop.getProperty("BROWSER"));
		System.setProperty("SELENIUMLAUNCHTYPE", prop.getProperty("SELENIUMLAUNCHTYPE"));
		System.setProperty("SetSession", prop.getProperty("SetSession"));
		System.setProperty("EXECUTIONTYPE", prop.getProperty("EXECUTIONTYPE"));
		System.setProperty("openPDFExternally", prop.getProperty("openPDFExternally"));
		System.setProperty("HUBURL", prop.getProperty("HUBURL"));
		System.setProperty("LogingLevel", prop.getProperty("LogingLevel"));
		System.setProperty("ReportFolderTime",new GregorianCalendar().getTime().toString().replaceAll(" ", "_").replaceAll(":", "."));
		System.setProperty("OQ_Run", prop.getProperty("OQ_Run"));
		System.setProperty("ZipFilePassword", prop.getProperty("ZipFilePassword"));
	}

	@BeforeAll(order = 1)
	public static void initReport() {
		// extent.set(new ExtentReports());
		ExtentSparkReporter spark = new ExtentSparkReporter(
				constants.getReportsPath() + constants.getProductName() + ".html");
		spark.config().setDocumentTitle(constants.getProductName());
		spark.config().setReportName(constants.getProductName());
		extent.setAnalysisStrategy(AnalysisStrategy.SUITE);
		extent.attachReporter(spark);
	}

	// @Before(order = 0)
	// public void initWebDriver() {
	// System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\main\\resources\\com\\arisglobal\\LSHE2\\chromedriver.exe");
	// driver.set(new ChromeDriver()) ;
	// driver.get().manage().window().maximize();
	// driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	// driver.get().manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
	// driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
	// }

	@Before(order = 0)
	public void initLogger(Scenario scenario) throws Exception {
		logger.set(Logger.getLogger(scenario.getName()));
		logger.get().setLevel(Level.parse(System.getProperty("LogingLevel")));
		String logFileName = constants.getReportsPath() + "Log_" + scenario.getName() + "_"
				+ System.getProperty("ReportFolderTime") + ".log";
		FileHandler fh = new FileHandler(logFileName);
		fh.setLevel(Level.parse(System.getProperty("LogingLevel")));
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
		logger.get().addHandler(fh);
	}

	@Before(order = 1)
	public void initConstants(Scenario scenario)
			throws FileNotFoundException, IOException, AWTException, InterruptedException {
		testContext.scenarioContext.setScenarioConstants();
		testContext.scenarioContext.setUsers();
		testContext.scenarioContext.putScenarioConstant("lsmvXmlPath",
				System.getProperty("user.dir") + "\\src\\test\\resources\\Ai\\Attri\\testdata\\xml");
		testContext.scenarioContext.putScenarioConstant("IExcel",
				System.getProperty("user.dir") + "\\src\\test\\resources\\Ai\\Attri\\IExcel\\");
		testContext.scenarioContext.putScenarioConstant("ScenarioName", scenario.getName());
		testContext.scenarioContext.putScenarioConstant("DownloadFolder",
				System.getProperty("user.dir") + "\\test-output\\");
		testContext.scenarioContext.putScenarioConstant("FileInput", System.getProperty("user.dir")
				+ "\\src\\test\\resources\\Ai\\Attri\\testdata\\InputFiles");
		testContext.scenarioContext.putScenarioConstant("ReportsPath", constants.getReportsPath());
		testContext.scenarioContext.putScenarioConstant("testData",
				System.getProperty("user.dir") + "\\src\\test\\resources\\Ai\\Attri\\testdata\\");

		testContext.scenarioContext.putScenarioConstant("aerFileName", System.getProperty("user.dir")
				+ "\\src\\test\\resources\\Ai\\Attri\\testdata\\aers\\ReviewSelectedAER%s.txt");

		testContext.scenarioContext.putScenarioConstant("DownloadFilePath",
				System.getProperty("user.dir") + "\\test-output\\");
		testContext.scenarioContext.putScenarioConstant("Prerequisite_CasesPath", System.getProperty("user.dir")
				+ "\\src\\test\\resources\\Ai\\Attri\\testdata\\Prerequisite_Cases");
		testContext.scenarioContext.putScenarioConstant("JSONPath",
				System.getProperty("user.dir") + "\\src\\test\\resources\\Ai\\Attri\\JSON\\");
		testContext.scenarioContext.putScenarioConstant("XMLPath",
				System.getProperty("user.dir") + "\\src\\test\\resources\\Ai\\Attri\\XML\\");
		testContext.scenarioContext.putScenarioConstant("updatedJson",
				System.getProperty("user.dir") + "\\test-output\\updated%s.json");
		testContext.scenarioContext.putScenarioConstant("Environmet", "STANDARD");
		testContext.scenarioContext.putScenarioConstant("TC_Count", "1");
	}


	@Before(order = 3)
	public void initScenarioReport(Scenario scenario) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, Exception {
		String OQ_Run = testContext.scenarioContext.getScenarioConstant("OQ_Run");
		if (OQ_Run.equalsIgnoreCase("YES")) {
		 extentTest.set(extent.createTest(scenario.getName()));
		extentTest.get().getExtent().setReportUsesManualConfiguration(true);
		}
		else
		{
			extentTest.set(extent.createTest(scenario.getName()).assignCategory(scenario.getSourceTagNames().toString()));
			extentTest.get().getExtent().setReportUsesManualConfiguration(true);
		}
		stepNumber = 0;
		Field f = scenario.getClass().getDeclaredField("delegate");
		f.setAccessible(true);
		io.cucumber.core.backend.TestCaseState sc = (io.cucumber.core.backend.TestCaseState) f.get(scenario);
		Field f1 = sc.getClass().getDeclaredField("testCase");
		f1.setAccessible(true);
		io.cucumber.plugin.event.TestCase testCase = (io.cucumber.plugin.event.TestCase) f1.get(sc);
		testSteps = testCase.getTestSteps().stream().filter(x -> x instanceof PickleStepTestStep)
				.map(x -> (PickleStepTestStep) x).collect(Collectors.toList());
		Update_TestCaseInfo(extentTest.get(), scenario.getName());
	}

	@BeforeStep(order = 0)
	public void initStepReport() throws Exception {
		String OQ_Run = testContext.scenarioContext.getScenarioConstant("OQ_Run");
		if (OQ_Run.equalsIgnoreCase("YES")) {
			String Result = testSteps.get(stepNumber++).getStep().getText();
			String step = "Step " + stepNumber;
			;
			
			String Description = addBreakForNewLine(
					testContext.scenarioContext.getTestDataFromIMap("TestStepsMapping", step, "Description"));
			String TestData = addBreakForNewLine(
					testContext.scenarioContext.getTestDataFromIMap("TestStepsMapping", step, "Test Data"));
			String ExpectedResult = addBreakForNewLine(
					testContext.scenarioContext.getTestDataFromIMap("TestStepsMapping", step, "Expected Result"));

			Description.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("“", "\"").replaceAll("”", "\"")
					.replaceAll("’", "'").replaceAll("‘", "'").replaceAll("–", "-").replaceAll("  ", " ");
			TestData.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("“", "\"").replaceAll("”", "\"")
					.replaceAll("’", "'").replaceAll("‘", "'").replaceAll("–", "-").replaceAll("  ", " ");
			ExpectedResult.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("“", "\"").replaceAll("”", "\"")
					.replaceAll("’", "'").replaceAll("‘", "'").replaceAll("–", "-").replaceAll("  ", " ");

			constants.setExtentNode(extentTest.get().createNode("Step " + (stepNumber) + " : " + Description));
			// testContext.scenarioContext.putScenarioConstant("StepName", Description);
			reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
					testContext.getWebDriverManager().getDriver(), Status.INFO, "<b> Test Data : </b>" + TestData, false,
					false);
			reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
					testContext.getWebDriverManager().getDriver(), Status.INFO, "<b> Expected Results : </b>" + ExpectedResult,
					false, false);
			// logger.get().log(Level.INFO, "Step # " + (stepNumber) + " : " + "EXPECTED
			// RESULT : " + ExpectedResult);

		} else {
			String Result = testSteps.get(stepNumber++).getStep().getText();
			constants.setExtentNode(extentTest.get().createNode("Step # " + (stepNumber) + " : " + Result));
			testContext.scenarioContext.putScenarioConstant("StepName", Result);
			reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
					testContext.getWebDriverManager().getDriver(), Status.INFO, "EXPECTED RESULT : " + Result, false,
					false);

		}

	}

	@AfterStep(order = 0)
	public void endStepReport(Scenario scenario) throws Exception {
		// final byte[] screenshot = ((TakesScreenshot)
		// testContext.getWebDriverManager().getDriver()).getScreenshotAs(OutputType.BYTES);
		// scenario.attach(screenshot, "image/png", "image");
		Thread.sleep(1000);
		String OQ_Run = testContext.scenarioContext.getScenarioConstant("OQ_Run");
		if (OQ_Run.equalsIgnoreCase("YES")) {
			String step = "Step " + stepNumber;
			String ActualResult = testContext.scenarioContext.getTestDataFromIMap("TestStepsMapping", step,
					"Actual Result");
			ActualResult.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("“", "\"").replaceAll("”", "\"")
					.replaceAll("’", "'").replaceAll("‘", "'").replaceAll("–", "-").replaceAll("  ", " ");
			if (scenario.isFailed()) {
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.INFO, "", true,
						constants.CaptureDesktopScreenshot());
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.FAIL, "<b> Actual Results : </b> Not as Expected",
						false, constants.CaptureDesktopScreenshot());
				//logError(scenario, logger.get());
//				 logger.get().log(Level.INFO, "Step # " + (stepNumber) + " : " + "ACTUAL RESULT : "
//				 + testContext.scenarioContext.getScenarioConstant("StepName") + " : FAILED");
			} else {
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.INFO, "", true,
						constants.CaptureDesktopScreenshot());

				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.PASS,
						"<b> Actual Results : </b> AE, " + ActualResult, false, constants.CaptureDesktopScreenshot());
				// logger.get().log(Level.INFO, "Step # " + (stepNumber) + " : " + "ACTUAL
				// RESULT : "
				// + testContext.scenarioContext.getScenarioConstant("StepName"));
			}

		} else {
			if (scenario.isFailed()) {
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.INFO, "", true,
						constants.CaptureDesktopScreenshot());
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.FAIL,
						"<b> Actual Results : </b> " + testContext.scenarioContext.getScenarioConstant("StepName") + " : FAILED",
						false, constants.CaptureDesktopScreenshot());
				logError(scenario, logger.get());
				logger.get().log(Level.INFO, "Step # " + (stepNumber) + " : " + "<b> Actual Results : </b> "
						+ testContext.scenarioContext.getScenarioConstant("StepName") + " : FAILED");
			} else {
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.INFO, "", true,
						constants.CaptureDesktopScreenshot());
				reports.ExtentReportLog(constants.getReportsPath(), constants.getExtentNode(),
						testContext.getWebDriverManager().getDriver(), Status.PASS,
						"<b> Actual Results : </b> " + testContext.scenarioContext.getScenarioConstant("StepName"), false,
						constants.CaptureDesktopScreenshot());
				logger.get().log(Level.INFO, "Step # " + (stepNumber) + " : " + "<b> Actual Results : </b>"
						+ testContext.scenarioContext.getScenarioConstant("StepName"));
			}
		}
		constants.getExtentNode().getModel().setEndTime(Calendar.getInstance().getTime());
	}

//	@After(order = 10)
//	public void closeBrowser() throws InterruptedException {
//		// try {
//		// driver.get().quit();
//		// //Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
//		// driver.remove();
//		// Thread.sleep(3*1000);
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// } catch (InterruptedException e) {
//		// e.printStackTrace();
//		// }
//		testContext.getWebDriverManager().closeDriver();
//
//	}

	// updated for Live Status
	@After(order = 10)
	public void endScenarioReportUpdateInDB(Scenario scenario) throws Exception {

		String DB_Name = testContext.scenarioContext.getScenarioConstant("pbidb");
		String DB_UserName = testContext.scenarioContext.getScenarioConstant("pbiuser");
		String DB_Password = testContext.scenarioContext.getScenarioConstant("pbipassword");
		String PBI_Run = testContext.scenarioContext.getScenarioConstant("PBI_Run");
		String PBI_ReRun = testContext.scenarioContext.getScenarioConstant("PBI_ReRun");
		String ProductVersion = testContext.scenarioContext.getScenarioConstant("ProductVersion");
		String ProductBuild = testContext.scenarioContext.getScenarioConstant("ProductBuild");
		String prodName = testContext.scenarioContext.getScenarioConstant("prodName");
		String customername = "";
		if (testContext.scenarioContext.getScenarioConstant("Environmet").equalsIgnoreCase("Standard")) {
			customername = testContext.scenarioContext.getScenarioConstant("customername");
		} else {
			customername = testContext.scenarioContext.getScenarioConstant("Environmet");
		}

		String resultSlNo = "";
		String testsuitename = testContext.scenarioContext.getScenarioConstant("testsuitename");
		String logmessage = testContext.scenarioContext.getScenarioConstant("logmessage");
		String testCaseName = scenario.getName().toString();//
		String status = extentTest.get().getStatus().toString().toUpperCase();
		String TC_Count = testContext.scenarioContext.getScenarioConstant("TC_Count");

		if (PBI_Run.equalsIgnoreCase("YES")) {
			// String status=extentTest.get().getStatus().toString().toUpperCase();//
			// String statusDate =getCurrenttime().toString();
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			String statusDate = formatter.format(date);
			statusDate = statusDate.replace(":", "");
			statusDate = statusDate.replace("-", "");
			statusDate = statusDate.replace(" ", "");
			if (PBI_ReRun.equalsIgnoreCase("YES")) {
				for (int i = 1; i <= Integer.parseInt(TC_Count); i++) {
					String testCaseName_Child = testCaseName + "_" + i;
					try {
						Connection con = new DBUtil().getDBConnection(DB_Name, DB_UserName, DB_Password);
						ResultSet result = new DBUtil().performRead(con,
								"SELECT * FROM automation_test_execution_results WHERE testcasename ='"
										+ testCaseName_Child + "' ORDER BY slno desc limit 1");
						System.out.println("Contents of the table");
						while (result.next()) {
							resultSlNo = result.getString("slno");
							System.out.println("Slno: " + resultSlNo);
						}
						new DBUtil().performWrite(con,
								"UPDATE automation_test_execution_results SET status = '" + status + "',executiondate='"
										+ LocalDate.now() + "',	version	='" + ProductVersion + "',build='"
										+ ProductBuild + "', datetimestamp	='" + statusDate + "'  WHERE slno = '"
										+ resultSlNo + "'");
						System.out.println("Updated row Slno:   " + resultSlNo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {

				for (int i = 1; i <= Integer.parseInt(TC_Count); i++) {
					String testCaseName_Child = testCaseName + "_" + i;
					try {

						Connection con = new DBUtil().getDBConnection(DB_Name, DB_UserName, DB_Password);
						new DBUtil().performWrite(con,
								"INSERT INTO automation_test_execution_results(product,testsuitename,"
										+ "testcasename,logmessage,status,executiondate,datetimestamp,version,build,environment,customername,executionsequence) "
										+ "VALUES('" + prodName + "','" + testsuitename + "','" + testCaseName_Child
										+ "','" + logmessage + "','" + status + "','" + LocalDate.now() + "','"
										+ statusDate + "','" + ProductVersion + "','" + ProductBuild
										+ "','Automation','" + customername + "','1')");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	@After(order = 9)
	public void endScenarioReport() {
		extentTest.get().getModel().setEndTime(Calendar.getInstance().getTime());
		constants.destroyExtentNode();
		extentTest.remove();
	}

	@AfterAll(order = 2)
	public static void taskKill() throws IOException {
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
	}

	@AfterAll(order = 1)
	public static void publishReport() {
		extent.flush();
	}
	
	@AfterAll(order = 0)
	public static void zipReport() throws IOException  {
		
//		fileSystemOperations.createFolder(constants.getDownloadPath()+testName);
		//fileSystemOperations.passwordProtectReportFile(constants.getDownloadPath(), constants.getDownloadPath()+testName, System.getProperty("ZipFilePassword"));
	//	passwordProtectReportFile(constants.getDownloadPath(),System.getProperty("ReportFolderTime"),constants.getDownloadPath()+testName,System.getProperty("ZipFilePassword"));
	}


	private void logError(Scenario scenario, Logger logger)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Field f = scenario.getClass().getDeclaredField("delegate");
		f.setAccessible(true);

		Object objectScenario = f.get(scenario);
		Field fieldStepResults = objectScenario.getClass().getDeclaredField("stepResults");
		fieldStepResults.setAccessible(true);

		ArrayList<Result> results = (ArrayList<Result>) fieldStepResults.get(objectScenario);
		for (Result result : results) {
			if (result.getError() != null)
				logger.log(Level.SEVERE, "Exception thrown :", result.getError());
		}
	}

	private void Update_TestCaseInfo(ExtentTest extentTest, String scenarioName) throws Exception {
		testName=testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("TEST NAME"));
		extentTest.info("<b>Test Name:</b> <br>"
				+ testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("TEST NAME")));
		extentTest.info("<b>Author:</b>  <br>"
				+ testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("AUTHOR")));
		extentTest.info("<b>Approval Status:</b> <br>"
				+ testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("APPROVAL STATUS")));
		extentTest.info("<b>Purpose:</b> <br>" + addBreakForNewLine(
				testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("PURPOSE"))));
		extentTest.info("<b>Pre-Requisites:</b> <br>" + addBreakForNewLine(
				testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("PRE-REQUISITE"))));
		extentTest.info("<b>Test Conditions:</b> <br>" + addBreakForNewLine(
				testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName, ("TEST CONDITIONS"))));
		extentTest.info("<b>Special Instructions:</b> <br>" + addBreakForNewLine(testContext.scenarioContext
				.getTestDataFromIMap("TestCaseInfo", scenarioName, ("SPECIAL INSTRUCTIONS"))));
		extentTest.info("<b>Execution Environment:</b> <br>" + testContext.scenarioContext
				.getTestDataFromIMap("TestCaseInfo", scenarioName, ("EXECUTION ENVIRONMENT")));
		/*
		 * extentTest.info("<b>PRODUCT VERSION:</b> " +
		 * testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName,
		 * ("PRODUCT VERSION"))); extentTest.info( "<b>REQUIREMENTS MAPPING:</b> " +
		 * addBreakForNewLine(testContext.scenarioContext.getTestDataFromIMap(
		 * "TestCaseInfo",scenarioName,("REQUIREMENTS MAPPING"))));
		 * extentTest.info("<b>MODULE:</b> " +
		 * testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo",scenarioName,(
		 * "MODULE"))); extentTest.info( "<b>EXECUTOR:</b> " +
		 * testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName,
		 * ("EXECUTOR"))); extentTest.info( "<b>PRE-EXECUTION REVIEWER: </b> " +
		 * testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo",
		 * scenarioName,("PRE-EXECUTION REVIEWER"))); extentTest.info(
		 * "<b>APPROVER: </b> " +
		 * testContext.scenarioContext.getTestDataFromIMap("TestCaseInfo", scenarioName,
		 * ("APPROVER")));
		 */ }

	private String addBreakForNewLine(String value) {
		String sent = null, withBrk = "";
		String[] sentences = value.replace("<", "&lt;").replace(">", "&gt;").split("\\.");
		for (int i = 0; i < sentences.length; i++) {
			sent = sentences[i] + "." + " <br> ";
			withBrk = withBrk + sent;
		}
		return withBrk;

	}
	
	
	
	/**********************************************************************************************************
	 * @Objective: The below method is created to zip extent report and make it
	 *             password protected
	 * @InputParameters: reportPath,zipFolderPath,password as String
	 * @OutputParameters:
	 * @author:Adarsh
	 * @throws InterruptedException 
	 * @Date : 19-Mar-2021
	 * @UpdatedByAndWhen:
	 **********************************************************************************************************/
	public static void passwordProtectReportFile(String reportfolderPath, String reportfolderName,String zipFolderPath ,String password) {
		File fol = new File("");
		fol = new File(reportfolderPath + "\\" + "newnew");
		File src= new File(reportfolderPath+reportfolderName);
		fol.mkdir();
		try {
			FileUtils.copyDirectoryToDirectory(src, fol);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
String reportPath=reportfolderPath+"newnew";
			String[] reportName = reportPath.split("\\\\");
			File directoryPath = new File(reportPath);
			File filesList[] = directoryPath.listFiles();
			ArrayList<File> filesToAdd = new ArrayList<File>();
			for (File file : filesList) {
				if (file.getAbsolutePath().contains(".html")) {
					filesToAdd.add(new File(file.getAbsolutePath()));

				} else if (file.isDirectory()) {

					fol = new File(zipFolderPath + "\\" + file.getName());
					fol.mkdir();
					try {
						FileUtils.copyDirectory(file, fol);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				else {
					filesToAdd.add(new File(file.getAbsolutePath()));}

			}

			ZipParameters zipParameters = new ZipParameters();
			zipParameters.setEncryptFiles(true);
			zipParameters.setEncryptionMethod(EncryptionMethod.AES);
			zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
			ZipFile zipFile = new ZipFile(reportfolderPath + "\\" + reportName[reportName.length - 2] + ".zip",password.toCharArray());
			zipFile.addFiles(filesToAdd, zipParameters);
		//	zipFile.addFolder(fol, zipParameters);
//			try {
//				FileUtils.deleteDirectory(fol);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

}
