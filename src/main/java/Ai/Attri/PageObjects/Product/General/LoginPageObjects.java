package Ai.Attri.PageObjects.Product.General;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LoginPageObjects {
	WebDriver driver;

public LoginPageObjects(WebDriver driver){
	this.driver = driver;
	}
@FindBy(xpath="//div[@id='login-box']")
public WebElement loginbox;


@FindBy(xpath="//input[contains(@id,'Usernamebox1')]")
public WebElement userNameTextbox;

	
@FindBy(xpath="//input[contains(@id,'Passwordbox1')]")
public WebElement passwordTextbox;
	
	public String logOnButton = "//button[text()='Login']";
	
	public String cancelButtonClick = "//input[@value='Cancel']";
	@FindBy(xpath="//div[@class='message-dialog-container']/div/input[2]")
	public WebElement cancelbtn;

	@FindBy(xpath="//div[@id='msgBox']")
	public WebElement waitMethod;
	
	@FindBy(id="popup_message")
	public WebElement popupMessage;
	
	@FindBy(id="popup_ok")
	public WebElement popupYesButton;

	public WebElement left_menu(String val) {
		return driver.findElement(By.xpath("//div[@id='menu-content']/div/div/span[text()='"+val+"']"));
	}

	@FindBy(xpath = "//span[text()='BRAPI000022']")
	public WebElement sub;

	@FindBy(xpath = "//a[text()='Sent Items']")
	public WebElement sent_items;

	@FindBy(xpath = "(//tr[@title='Finish task']/td/table/tbody/tr/td/span)[2]")
	public WebElement finish;


	public WebElement frameElement(String locator) {
		return driver.findElement(By.xpath("//iframe[@name='"+locator+"']"));
	}
	public WebElement frameElements(String locator) {
		return driver.findElement(By.xpath("//iframe[contains(@name,'"+locator+"')]"));
	}



	@FindBy(xpath = "//div[@id='toggle-menu-button']//span[@class='menu-label']")
	public WebElement CollapseMenu;





		@FindBy(xpath="//div[@class='text-base line-clamp-1 text-title']/a")
		public List<WebElement> samp1;


	@FindBy(xpath="//input[@class='rbt-input-main form-control rbt-input']")
	public WebElement search;

	@FindBy(xpath="//div[@class='rbt']//input[1]")
	public WebElement searchexpand;


	@FindBy(xpath="(//div[@class='o-snippet__item'])[1]//div/img")
	public WebElement songimg;

	@FindBy(xpath="//span[@id='player_play_pause']")
	public WebElement pause;

	@FindBy(xpath="//span[@id='player_ellipsis']")
	public WebElement click3btn;

	@FindBy(xpath="//span[text()='Add to Playlist']")
	public WebElement addtoPlaylist;

	@FindBy(xpath="//a[text()='gloomy']")
	public WebElement playlistname;

	@FindBy(xpath="//span[@class='c-search__close']")
	public WebElement clearsearch;

	@FindBy(xpath="(//a[text()='Generative AI'])[1]")
	public WebElement attri;















}
