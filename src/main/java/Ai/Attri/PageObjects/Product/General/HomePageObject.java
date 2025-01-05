package Ai.Attri.PageObjects.Product.General;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePageObject {
	
	WebDriver driver;
	
	public HomePageObject(WebDriver driver){
		this.driver = driver;
		}
	
	@FindBy(id="app-picker")
	public WebElement appLauncher;
	
	@FindBy(xpath="(//div[@class='title'][contains(text(),'LSRIMS')])[1]")
	public WebElement lsrimsLink;
	
	@FindBy(xpath="//div[@class='title'][contains(text(),'Platform')]")
	public WebElement platformLink;

}
