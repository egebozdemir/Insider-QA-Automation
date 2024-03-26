package useinsider.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import useinsider.BaseComponents.BasePage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CareersPage extends BasePage {

    WebDriver driver;

    public CareersPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    //ELEMENTS
    @FindBy(id = "career-find-our-calling")
    WebElement teamsSection;

    By teamsSectionBy = By.id("career-find-our-calling");

    @FindBy(id = "career-our-location")
    WebElement locationsSection;

    By locationsSectionBy = By.id("career-our-location");

    @FindBy(css = "section[data-id='a8e7b90']")
    WebElement lifeatinsiderSection;

    By lifeatinsiderSectionBy = By.cssSelector("section[data-id='a8e7b90']");

    @FindBy(className = "loadmore")
    WebElement teamsLoadMoreButton;

    @FindBy(className = "job-item")
    List<WebElement> teamItems;

    @FindBy(linkText = "Quality Assurance")
    WebElement qualityassuranceLink;

    @FindBy(css = "a[href*='/open-positions/']")
    WebElement findYourJobButton;

    @FindBy(css = ".glide__slide")
    List<WebElement> locationSlides;

    @FindBy(css = ".glide__slide--active")
    WebElement activeLocation;

    By activeLocationBy = By.cssSelector(".glide__slide--active");

    @FindBy(xpath = "//div[@data-id='fe38935']")
    WebElement lifeAtInsiderSectionText;

    //ACTION METHODS
    public void waitPageLoad(){
        waitForElementToAppear(teamsSection);
        waitForElementToAppear(locationsSection);
        waitForElementToAppear(lifeatinsiderSection);
    }

    public boolean isTeamsPresent(){
        return isElementPresent(teamsSectionBy);
    }

    public boolean isTeamsVisible(){
        return isElementVisible(teamsSectionBy);
    }

    public void expandTeams() {
        clickElementJs(teamsLoadMoreButton);
    }

    public List<WebElement> getTeamItems() throws InterruptedException {
        expandTeams();
        Thread.sleep(3000);
        waitForAllElementsToAppear(teamItems);
        return teamItems;
    }

    public List<String> getTeamTitles() throws InterruptedException {
        List<String> teamTitlesList = getTeamItems().stream()
                .map(item->item.findElement(By.cssSelector("h3")).getText())
                .collect(Collectors.toList());
        return teamTitlesList;
    }

    public Boolean verifyTeamTitles(String[] teamTitles) throws InterruptedException {
        List<String> titles = getTeamTitles();
        if(titles==null || teamTitles==null || titles.size()!=teamTitles.length){
            return false;
        }
        return IntStream.range(0, titles.size())
                .allMatch(i -> titles.get(i).equals(teamTitles[i]));
    }

    public boolean isLocationsPresent(){
        return isElementPresent(locationsSectionBy);
    }

    public boolean isLocationsVisible(){
        return isElementVisible(locationsSectionBy);
    }

    public List<String> getLocationTexts() throws InterruptedException {
        List<String> locationTexts = new ArrayList<>();
        locationTexts.add(activeLocation.findElement(By.cssSelector("p")).getText());
        for (int i = 1; i<locationSlides.size(); i++){
            //scrollToElementAction(activeLocation);
            waitForElementToAppear(activeLocation);
            swipeElementRightAction(activeLocation);
            Thread.sleep(1000);
            locationTexts.add(driver.findElement(By.cssSelector(".glide__slide--active .mb-0")).getText());
        }
        return locationTexts;
    }

    public boolean verifyOfficeLocations(String[] officeLocations) throws InterruptedException {
        List<String> locationTitles = getLocationTexts();
        if(locationTitles==null || officeLocations==null || locationTitles.size()!=officeLocations.length){
            return false;
        }
        return IntStream.range(0, locationTitles.size())
                .allMatch(i -> locationTitles.get(i).equals(officeLocations[i]));
    }

    public boolean isLifeAtInsiderPresent(){
        return isElementPresent(lifeatinsiderSectionBy);
    }

    public boolean isLifeAtInsiderVisible(){
        return isElementVisible(lifeatinsiderSectionBy);
    }

    public boolean verifyLifeAtInsiderText(String lifeAtInsiderText){
        waitForElementToAppear(lifeAtInsiderSectionText);
        if(lifeAtInsiderSectionText.getText()==null || lifeAtInsiderText==null || lifeAtInsiderSectionText.getText().length()!=lifeAtInsiderText.length()){
            return false;
        }
        return lifeAtInsiderSectionText.getText().equals(lifeAtInsiderText);
    }

    public boolean verifyTeamsLocationsLifePresent(){
        return isTeamsPresent() && isLocationsPresent() && isLifeAtInsiderPresent();
    }

    public boolean verifyTeamsLocationsLifeVisible(){
        return isTeamsVisible() && isLocationsVisible() && isLifeAtInsiderVisible();
    }

    public QaTeamPage goToQaTeamPage() {
        expandTeams();
        waitForElementToClick(qualityassuranceLink);
        clickElementJs(qualityassuranceLink);
        QaTeamPage qaTeamPage = new QaTeamPage(driver);
        return qaTeamPage;
    }

    public JobListingPage goToJobsListingPage(){
        clickElementJs(findYourJobButton);
        JobListingPage jobListingPage = new JobListingPage(driver);
        return jobListingPage;
    }

}
