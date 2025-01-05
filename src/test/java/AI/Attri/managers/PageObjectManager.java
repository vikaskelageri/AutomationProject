package AI.Attri.managers;

import Ai.Attri.PageFactory.Product.General.AuditTrial;
import Ai.Attri.PageFactory.Product.General.Home;
import Ai.Attri.PageFactory.Product.General.Login;
import Ai.Attri.PageFactory.Product.General.Logout;
import Ai.Attri.PageObjects.Product.General.AuditTrialPageObjects;
import Ai.Attri.PageObjects.Product.General.HomePageObject;
import Ai.Attri.PageObjects.Product.General.LoginPageObjects;
import Ai.Attri.PageObjects.Product.General.LogoutPageObjects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

//import Ai.Attri.TestContext;
import Ai.Attri.Library.SeleniumActions;

public class PageObjectManager<administrationPageObjects> {
	private static final String Administration = null;
	private WebDriver driver;

	public PageObjectManager(WebDriver driver) {
		this.driver = driver;

	}

	private SeleniumActions seleniumActions;




	private LoginPageObjects loginPageObjects;
	private Login login;

	public Login getLogin() {
		if (login == null) {
			loginPageObjects = PageFactory.initElements(driver, LoginPageObjects.class);

			auditTrailPageObjects = PageFactory.initElements(driver, AuditTrialPageObjects.class);
			login = new Login(driver, loginPageObjects, auditTrailPageObjects);
		}
		return login;
	}

	private LogoutPageObjects logoutPageObjects;
	private Logout logout;





	private AuditTrialPageObjects auditTrailPageObjects;
	private AuditTrial auditTrail;

	public AuditTrial getAuditTrial() {
		if (auditTrail == null) {
			auditTrailPageObjects = PageFactory.initElements(driver, AuditTrialPageObjects.class);
			auditTrail = new AuditTrial(driver, auditTrailPageObjects);
		}
		return auditTrail;
	}


	private HomePageObject homePageObject;
	private Home home;

	public Home getHome() {
		if (home == null) {
			homePageObject = PageFactory.initElements(driver, HomePageObject.class);
			home = new Home(driver, homePageObject);
		}
		return home;
	}

}








