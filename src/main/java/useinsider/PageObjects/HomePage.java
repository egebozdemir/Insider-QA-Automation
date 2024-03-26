package useinsider.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import useinsider.BaseComponents.BasePage;

public class HomePage extends BasePage {

    WebDriver driver;

    public HomePage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    //ELEMENTS
    @FindBy(id = "wt-cli-accept-all-btn")
    WebElement acceptAllCookiesBtn;

    By acceptAllCookiesBtnBy = By.id("wt-cli-accept-all-btn");

    @FindBy(id = "cookie-law-info-bar")
    WebElement cookieOverlay;


    //ACTION METHODS
    public void goTo(){
        driver.get("https://useinsider.com/");
    }

    public Boolean verifyHomePageAccessibility(){
        return driver.getTitle() != null && !driver.getTitle().isEmpty();
    }

    public void closeCookieOverlay(){
        waitForElementToClick(acceptAllCookiesBtnBy);
        acceptAllCookiesBtn.click();
        waitForElementToDisappear(cookieOverlay);
    }

}
