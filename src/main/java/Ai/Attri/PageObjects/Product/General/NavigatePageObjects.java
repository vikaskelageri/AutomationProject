package Ai.Attri.PageObjects.Product.General;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Ai.Attri.Library.Constants;
import Ai.Attri.Library.SeleniumActions;


public class NavigatePageObjects {
    WebDriver driver;
    Constants constants = new Constants();
    SeleniumActions seleniumAction;


    public NavigatePageObjects(WebDriver driver) {
        this.driver = driver;
        seleniumAction = new SeleniumActions();
//		common = new Common();
    }

    public WebElement getMenu(String actualMenu) {
        return driver.findElement(By.xpath("//div[@class='menu-container']//a[text()='" + actualMenu + "']"));
    }

    public WebElement getSubMenu(String actualMenu, String actualSubMenu) {
        return driver.findElement(By.xpath("//div[@class='menu-container']//li/a[text()='" + actualMenu + "']/parent::li//li//a[text()='" + actualSubMenu + "']")); // Harish R Update the xpath


    }

    public WebElement getTab(String actualTab) {
        return driver.findElement(By.xpath("//img[@title='" + actualTab + "']"));
    }

    public WebElement getTab2(String actualTab) {
        return driver.findElement(By.xpath("(//img[@title='" + actualTab + "'])[2]"));
    }

    public WebElement getSubTab(String value) {
        return uniqueXpath("//td[text()='" + value + "']");
    }

    @FindBy(id = "app-picker")
    public WebElement headerMenu;

    @FindBy(id = "app-picker")
    public WebElement platformMenu;

    @FindBy(xpath = "//button[@id='C1095_52-btn']")
    public WebElement registrationBackBtn;

    public WebElement platformMenu(String value) {
        return driver.findElement(By.xpath("//div[@class='title'][contains(text(),'" + value + "')]"));
    }

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

    // Harish R added this object

    public WebElement getMainTab(String actualTab) {
        return driver.findElement(By.xpath("//li[@id='codeListGroup']/a[text()='" + actualTab + "']"));
    }

    public WebElement getSubtab(String actualTab) {
        return driver.findElement(By.xpath("//li[@id='codeListGroup']/ul/li[1]/a"));
    }

    public WebElement getCodelistSubtab(String actualTab) {
        return driver.findElement(By.xpath("//span[text()='"+actualTab+"']"));
    }
    public WebElement getCodelistSubtab1(String actualTab) {
        return driver.findElement(By.xpath("//ul/li[2]/span[text()='"+actualTab+"']"));
    }

    public WebElement getSubtabDropDown(String actualTab) {
        return driver.findElement(By.xpath("//span[@class='dropdown-menu-label'][text()='" + actualTab + "']"));
    }

    public WebElement left_menu(String val) {
        return driver.findElement(By.xpath("//div[@id='menu-content']/div/div/span[text()='" + val + "']"));
    }

    public WebElement clickondocument() {
        return driver.findElement(By.xpath("//div[@class='list-container-fields']/span/span/img[3]"));

    }

    public WebElement clickapprove() {
        return driver.findElement(By.xpath("//div[@id='actions-container']/div[text()='Approve']"));
    }

    public WebElement clickfinish(String val) {
        return driver.findElement(By.xpath("//input[@value='" + val + "']"));

    }

    public WebElement Subtab(String val) {
        return driver.findElement(By.xpath("//div[@class='horizontal-menu-item-container']/span[text()='My tasks']"));

    }

    @FindBy(xpath = "//button[@id='ext-gen391']")
    public WebElement codelistAddButton;

    //Added by Sushmitha J
    @FindBy(xpath = "//li[@id= 'agidmp_lifecycle']/a[text()= 'LifeCycle']")
    public WebElement lifeCycleMenu;

    @FindBy(xpath = "//div[contains(text(),'Loading...')]")
    public WebElement loadinglable;

    @FindBy(xpath = "//div[contains(text(),'Saving...')]")
    public WebElement savinglable;

    @FindBy(xpath = "//cell[contains(text(),'Loading...')]")
    public WebElement LoadingImglable;

    //added by riyaz on 2/5/2023
    @FindBy(xpath = "//span[@id='C1_133_144_7_145']")
    public WebElement addGear;

    @FindBy(xpath = "//li[@id=\"C1_133_144_7_146\"]/a")
    public WebElement addNew;

    @FindBy(xpath = "//li[@id=\"C1_133_144_7_148\"]")
    public WebElement addNewME;

    @FindBy(xpath = "//input[@id=\"C1_133_144_7_22_11\"]")
    public WebElement searchClick;

    @FindBy(xpath = "//*[@id=\"C1_133_144_7_22_2tbl\"]/tbody/tr[1]/td[1]/input")
    public WebElement selectCheckbox;

    @FindBy(xpath = "//button[@id=\"C1_133_144_7_24-btn\"]")
    public WebElement clickOk;

    @FindBy(xpath = "//div[@class='dropdown-container-left']/div")
    public WebElement subTabsDropDown;

    public WebElement subtabMasteData(String actualTab) {
        return driver.findElement(By.xpath("//div[@class='workspace-navigation-tabs']//ul//li//span[text()='" + actualTab + "']"));
    }
}
	

