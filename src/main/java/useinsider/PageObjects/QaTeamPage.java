package useinsider.PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import useinsider.BaseComponents.BasePage;

public class QaTeamPage extends BasePage {

    WebDriver driver;

    public QaTeamPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    //ELEMENTS
    @FindBy(css = "a[href*='department']")
    WebElement seeQaJobsBtn;

    @FindBy(id = "page-head")
    WebElement pageHeadSection;

    @FindBy(id = "career-team-motivation")
    WebElement motivationSection;

    @FindBy(id = "career-story")
    WebElement becomeInsiderSection;

    @FindBy(id = "find-job-widget")
    WebElement findYourJobSection;

    //ACTION METHODS
    public void waitPageLoad(){
        waitForElementToAppear(pageHeadSection);
        waitForElementToAppear(motivationSection);
        waitForElementToAppear(becomeInsiderSection);
        waitForElementToAppear(findYourJobSection);
    }

    public JobListingPage goToJobListingPage(){
        waitForElementToClick(seeQaJobsBtn);
        seeQaJobsBtn.click();
        JobListingPage jobListingPage = new JobListingPage(driver);
        return jobListingPage;
    }

}
