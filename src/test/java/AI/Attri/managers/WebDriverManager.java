package AI.Attri.managers;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.Collections;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.codec.w3c.W3CHttpCommandCodec;
import org.openqa.selenium.remote.codec.w3c.W3CHttpResponseCodec;

import Ai.Attri.Library.Constants;

public class WebDriverManager {
	private WebDriver driver;
	Constants constants = new Constants();

	public WebDriverManager() {
	}

	public WebDriver getDriver() throws IOException {
		if (driver == null) driver = createDriver();
		return driver;
	}

	private WebDriver createDriver() throws IOException {
		if (System.getProperty("SELENIUMLAUNCHTYPE").equalsIgnoreCase("NewSession")) createLocalDriver();
		else getCurrentSession();
		return driver;
	}

	private WebDriver createLocalDriver() throws IOException {
		if (System.getProperty("BROWSER").equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Ai.Attri\\chromedriver.exe");

			ChromeOptions choptions = new ChromeOptions();

			LoggingPreferences preferences = new LoggingPreferences();
			preferences.enable(LogType.PERFORMANCE, Level.INFO);
			preferences.enable(LogType.BROWSER, Level.INFO);

			HashMap<String, Object> chromePrefs = new HashMap<>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", constants.getDownloadPath());
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("profile.password_manager_enabled", false);
			chromePrefs.put("safebrowsing.enabled", true);

			if (System.getProperty("openPDFExternally").equalsIgnoreCase("true")) {
				chromePrefs.put("plugins.plugins_disabled", new String[]{"Chrome PDF Viewer"});
				chromePrefs.put("plugins.always_open_pdf_externally", true);
			}

			choptions.setExperimentalOption("prefs", chromePrefs);
			choptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			choptions.setExperimentalOption("useAutomationExtension", false);
			choptions.addArguments("--test-type");
			choptions.addArguments("--disable-gpu");
			choptions.addArguments("--no-first-run");
			choptions.addArguments("--no-default-browser-check");
			choptions.addArguments("--ignore-certificate-errors");
			choptions.setCapability("goog:loggingPrefs", preferences);
			choptions.addArguments("--remote-allow-origins=*");
			if (System.getProperty("EXECUTIONTYPE").equalsIgnoreCase("Headless")) {
				choptions.addArguments("--headless");
				choptions.addArguments("window-size=1920,1080");
				choptions.addArguments("--enable-javascript");
			}
			driver = new ChromeDriver(choptions);

		} else if (System.getProperty("BROWSER").equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\geckodriver.exe");
			String mimeTypes = "application/zip,application/octet-stream,image/jpeg,application/vnd.ms-outlook,text/html,application/pdf";
			FirefoxProfile profile = new FirefoxProfile();

			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("browser.download.dir", constants.getDownloadPath());
			profile.setPreference("browser.helperApps.neverAsk.openFile", mimeTypes);
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimeTypes);
			profile.setPreference("browser.helperApps.alwaysAsk.force", false);
			profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
			profile.setPreference("browser.download.manager.focusWhenStarting", false);
			profile.setPreference("browser.download.manager.useWindow", false);
			profile.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile.setPreference("browser.download.manager.closeWhenDone", false);

			FirefoxOptions ffoptions = new FirefoxOptions();
			ffoptions.setProfile(profile);

			if (System.getProperty("EXECUTIONTYPE").equalsIgnoreCase("Headless")) {
				ffoptions.addArguments("-headless");
			}
			driver = new FirefoxDriver(ffoptions);

		} else if (System.getProperty("BROWSER").equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\msedgedriver.exe");
			driver = new EdgeDriver();

		} else if (System.getProperty("BROWSER").equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();

		} else if (System.getProperty("BROWSER").equalsIgnoreCase("sgchrome")) {
			DesiredCapabilities capability = new DesiredCapabilities();
			capability.setBrowserName("chrome");
			capability.setPlatform(Platform.WINDOWS);
			ChromeOptions options = new ChromeOptions();
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\chromedriver.exe");

			HashMap<String, Object> chromePrefs = new HashMap<>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", constants.getDownloadPath());
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("profile.password_manager_enabled", false);
			chromePrefs.put("safebrowsing.enabled", true);

			if (System.getProperty("openPDFExternally").equalsIgnoreCase("true")) {
				chromePrefs.put("plugins.plugins_disabled", new String[]{"Chrome PDF Viewer"});
				chromePrefs.put("plugins.always_open_pdf_externally", true);
			}

			options.setExperimentalOption("prefs", chromePrefs);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.setExperimentalOption("useAutomationExtension", false);
			options.addArguments("--test-type");
			options.addArguments("--disable-gpu");
			options.addArguments("--no-first-run");
			options.addArguments("--no-default-browser-check");
			options.addArguments("--ignore-certificate-errors");
			if (System.getProperty("EXECUTIONTYPE").equalsIgnoreCase("Headless")) {
				options.addArguments("--headless");
				options.addArguments("window-size=1920,1080");
			}

			options.merge(capability);
			driver = new RemoteWebDriver(new URL(System.getProperty("HUBURL") + "/wd/hub"), options);

		} else if (System.getProperty("BROWSER").equalsIgnoreCase("sgfirefox")) {
			String mimeTypes = "application/zip,application/octet-stream,image/jpeg,application/vnd.ms-outlook,text/html,application/pdf";
			DesiredCapabilities capability = new DesiredCapabilities();
			capability.setBrowserName("firefox");
			capability.setPlatform(Platform.LINUX);
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\geckodriver.exe");

			FirefoxProfile fprofile = new FirefoxProfile();
			fprofile.setPreference("browser.download.folderList", 2);
			fprofile.setPreference("browser.download.manager.showWhenStarting", false);
			fprofile.setPreference("browser.download.dir", constants.getDownloadPath());
			fprofile.setPreference("browser.helperApps.neverAsk.openFile", mimeTypes);
			fprofile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimeTypes);
			fprofile.setPreference("browser.helperApps.alwaysAsk.force", false);
			fprofile.setPreference("browser.download.manager.alertOnEXEOpen", false);
			fprofile.setPreference("browser.download.manager.focusWhenStarting", false);
			fprofile.setPreference("browser.download.manager.useWindow", false);
			fprofile.setPreference("browser.download.manager.showAlertOnComplete", false);
			fprofile.setPreference("browser.download.manager.closeWhenDone", false);

			FirefoxOptions foptions = new FirefoxOptions();
			foptions.setProfile(fprofile);

			if (System.getProperty("EXECUTIONTYPE").equalsIgnoreCase("Headless")) {
				foptions.addArguments("-headless");
			}

			foptions.merge(capability);
			driver = new RemoteWebDriver(new URL(System.getProperty("HUBURL") + "/wd/hub"), foptions);
		} else {

		}
		Constants.driver = driver;
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(300));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(300));
		driver.manage().deleteAllCookies();
		if (System.getProperty("SetSession").equalsIgnoreCase("Yes")) setSession(driver);
		return driver;
	}

	public void setSession(WebDriver driver) throws IOException {

		HttpCommandExecutor executor = (HttpCommandExecutor) ((RemoteWebDriver) driver).getCommandExecutor();
		URL url = executor.getAddressOfRemoteServer();
		SessionId session_id = ((RemoteWebDriver) driver).getSessionId();
		String sessionID = session_id.toString();
		String URL = url.toString();

		OutputStream output = new FileOutputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\session.properties");
		Properties prop = new Properties();
		prop.setProperty("sessionID", sessionID);
		prop.setProperty("URL", URL);
		prop.store(output, null);
	}

	public void getCurrentSession() throws IOException {

		InputStream input = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\Ai\\Attri\\session.properties");
		Properties prop = new Properties();
		prop.load(input);
		String sessionID = prop.getProperty("sessionID");
		String URL = prop.getProperty("URL");
		URL url = new URL(URL);
		RemoteWebDriver driver = createDriverFromSession(sessionID, url);
		this.driver = driver;
	}

	public RemoteWebDriver createDriverFromSession(final String sessionId, URL command_executor) {
		CommandExecutor executor = new HttpCommandExecutor(command_executor) {

			@Override
			public Response execute(Command command) throws IOException {
				Response response = null;
				if (command.getName() == "newSession") {
					response = new Response();
					response.setSessionId(sessionId);
					response.setStatus(0);
					response.setValue(Collections.<String, String>emptyMap());

					try {
						Field commandCodec = null;
						commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
						commandCodec.setAccessible(true);
						commandCodec.set(this, new W3CHttpCommandCodec());

						Field responseCodec = null;
						responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
						responseCodec.setAccessible(true);
						responseCodec.set(this, new W3CHttpResponseCodec());
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

				} else {
					response = super.execute(command);
				}
				return response;
			}
		};
		return new RemoteWebDriver(executor, new DesiredCapabilities());
	}

	public void closeDriver() throws InterruptedException {
		driver.quit();
		Thread.sleep(3000);
	}

}
