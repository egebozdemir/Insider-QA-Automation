package useinsider.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import useinsider.BaseComponents.BasePage;

import java.util.List;

public class JobApplicationPage extends BasePage {

    WebDriver driver;

    public JobApplicationPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //ELEMENTS
    @FindBy(tagName = "h2")
    WebElement jobTitle;

    @FindBy(css = "div[data-qa='job-description']")
    WebElement jobDescription;

    @FindBy(css = "a[href*='/apply']")
    List<WebElement> applyButtons;


    //ACTION METHODS
    public void waitPageLoad(){
        waitForElementToAppear(jobTitle);
        waitForElementToAppear(jobDescription);
        waitForAllElementsToAppear(applyButtons);
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    public String getPageUrl(){
        return driver.getCurrentUrl();
    }

    public String getJobTitle(){
        return jobTitle.getText();
    }

    public boolean verifyLeverPageUrl(String leverUrlPart){
        return getPageUrl().contains(leverUrlPart);
    }

    public boolean verifyJobTitle(String roleTitle){
        return getJobTitle().equals(roleTitle);
    }

}
