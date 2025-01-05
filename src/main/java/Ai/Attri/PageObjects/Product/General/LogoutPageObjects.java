package Ai.Attri.PageObjects.Product.General;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LogoutPageObjects {
	WebDriver driver;

public LogoutPageObjects(WebDriver driver){
	this.driver = driver;
	}


	@FindBy(xpath="//img[contains(@id,'logout-button')]")
	public WebElement userIcon;
	
	public String logOnButton = "//button[text()='Login']";
	
	@FindBy(xpath="//div[@id='agl-user-dd']")
	public WebElement popupMsg;
	
	@FindBy(xpath="//div[@class='agl-userdd-item agl-logout'][contains(.,'Sign Out')]")
	public WebElement signOutLink;
	
	@FindBy(xpath="//span[@class='agl-conf-message']")
	public WebElement logoutMessage;
	
	@FindBy(xpath="//input[@id='yes']")
	public WebElement confirmYesButton;
	
	@FindBy(xpath="//div[text()='LifeSphere']")
	public WebElement homePageLogo;
}