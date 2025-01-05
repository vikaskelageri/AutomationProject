package Ai.Attri.PageObjects.Product.General;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Ai.Attri.Library.SeleniumActions;

public class AuditTrialPageObjects {
	
	WebDriver driver;
	SeleniumActions seleniumAction;
	public AuditTrialPageObjects(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		seleniumAction = new SeleniumActions();
		
	
	}
	
	
	
	
	@FindBy(className ="agl-info-box-close")
	public WebElement infoMessageClose;
	
	@FindBy(xpath="//div[@class='agl-info-box']")
	public WebElement saveMessage;
	
	@FindBy(id="trg_reason-code")
	public WebElement reasonCodeDropdownArrow;
	
	@FindBy(xpath="//label[contains(.,'New information')]")
	public WebElement reasonCodeDropdown;
	
	@FindBy(xpath="//label[contains(text(),'New information')]")
	public WebElement reasonCodeNewInfo;
	
	@FindBy(xpath="//textarea[@id='reason']")
	public WebElement reasonTextbox;
	
	@FindBy(xpath="//input[contains(@id,'savebtn')]")
	public WebElement saveButton;
	
	@FindBy(xpath="//input[contains(@id,'cancelbtn')]")
	public WebElement cancelButton;
	
	@FindBy(id="audittrail_capture")
	public WebElement auditTrailPopUp;
	
	@FindBy(xpath="//input[@id='esign-userName']")
	public WebElement userNameTextBox;
	
	@FindBy(xpath="//input[@id='esign-password']")
	public WebElement passwordTextBox;
	
	@FindBy(xpath="//input[@id='esign-password']")
	public WebElement commentTextBox;
	
	@FindBy(xpath="//input[@id='audittrail_savebtn']")
	public WebElement applyButton;
	//input[@id = 'C182']
	@FindBy(xpath="//button[contains(text(),'Save')]") 
	public WebElement moduleSaveButton;
	
	// Harish R : C1_19-btn
	@FindBy(xpath="//button[@id='C1_19-btn']") 
	public WebElement obligModuleSaveButton;
	
	// Sheela
	@FindBy(xpath="//button[@id='C1861_1764-btn']") 
	public WebElement sbprModuleSaveButton;

	@FindBy(id="agl-confirm-yes")
	public WebElement yesBtn;

	@FindBy(id="agl-confirm-no")
	public WebElement noBtn;

	@FindBy(xpath = "//div[@role='dialog']")
	public WebElement confirmPop;
	
	// Harish R added this object
	@FindBy(xpath = "//button[@id='ext-gen42']")
	public WebElement codelistSaveButton;
	
	// Harish R added this object
	@FindBy(id="audittrail_esign")
	public WebElement auditTrailEsignPopUp;
	
	// Harish R added this object
	@FindBy(id="audittrail_savebtn")
	public WebElement auditTrailApplyButton;
	
	@FindBy(id="esign-userName")
	public WebElement esignUsername;
	
	@FindBy(id="esign-password")
	public WebElement esignPassword;
	
	// Harish R added this object
	@FindBy(id="esign_comments")
	public WebElement commentsTextbox;
	
	// Harish R added this object during SBPR Save
	@FindBy(xpath="//div[@id='popup_message']")
	public WebElement sbprSaveConfigPopup;
	
	// Harish R added this object for OK Click
	@FindBy(xpath="//input[@id='popup_ok']")
	public WebElement sbprSaveConfigPopupOk;
	

	public String syncrmscmsokbtn="//button[text()='Ok']";
	
	
	
	/// Added by vadiraj ///////////////
    @FindBy(xpath="//button[@id='C19-btn']")
	public WebElement commitmentModSaveButton;
    
    @FindBy(xpath="//div[@id='audittrail_capture_handle']/div[1]/span")
	public WebElement commitmentAuditTialPopup;
    
	public WebElement uniqueXpath(String xpath) {
		xpath = "(" + xpath + ")";
		List<WebElement> list = new ArrayList<>();
		list = driver.findElements(By.xpath(xpath));

		if (list.size() > 1) {
			for (int k = 1; k <= list.size(); k++) {
				String newxpath = xpath + "[" + k + "]";
				if (seleniumAction.isVisible(driver.findElement(By.xpath(newxpath)))) {
					xpath = newxpath;
					break;
				}
			}
		}
		return driver.findElement(By.xpath(xpath));
	}
    
}
