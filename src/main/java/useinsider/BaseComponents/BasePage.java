package useinsider.BaseComponents;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import useinsider.PageObjects.CareersPage;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//FACADE CLASS
public class BasePage {

    WebDriver driver;

    public BasePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //ELEMENTS (common for all pages, reusable)
    @FindBy(linkText = "Company")
    WebElement companyLink;

    @FindBy(linkText = "Careers")
    WebElement careersLink;


    //ACTION METHODS (common for all pages, reusable)
    public void waitForElementToAppear(By findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
    }

    public void waitForElementToAppear(WebElement findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(findBy));
    }

    public void waitForAllElementsToAppear(List<WebElement> findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //wait.until(ExpectedConditions.visibilityOfAllElements(findBy));
        for(WebElement element : findBy){
            wait.until(ExpectedConditions.visibilityOf(element));
        }
    }

    public void waitForElementToClick(By findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(findBy));
    }

    public void waitForElementToClick(WebElement findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(findBy));
    }

    public void waitForAllElementsToClick(List<WebElement> findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        for(WebElement element : findBy){
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }
    }

    public void waitForElementToDisappear(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void moveToElementJs(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void clickElementJs(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        js.executeScript("arguments[0].click();", element);
    }

    public void scrollToElementAction(WebElement element){
        int deltaY = element.getRect().y;
        Actions actions = new Actions(driver);
        actions.scrollByAmount(0,deltaY).build().perform();

    }

    public void clickElementAction(WebElement element){
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
    }

    public void swipeElementRightAction(WebElement element){
        Actions actions = new Actions(driver);
        actions.click(element).sendKeys(Keys.ARROW_RIGHT);
        actions.build().perform();
    }

    public boolean isElementVisible(By elementBy){
        return driver.findElement(elementBy).isDisplayed();
    }

    public Boolean isElementPresent(By elementBy){
        int t = driver.findElements(elementBy).size();
        return t > 0;
    }

    public void switchBrowserTab(){
        Set<String> windows = driver.getWindowHandles(); //[parentWindowID, childWindowID, subChildWindowId, ...]
        Iterator<String> iter = windows.iterator();
        String parentID = iter.next();
        String childID = iter.next();
        driver.switchTo().window(childID);
    }

    public CareersPage goToCareersPage(){
        waitForElementToClick(companyLink);
        companyLink.click();
        waitForElementToClick(careersLink);
        careersLink.click();
        CareersPage careersPage = new CareersPage(driver);
        return careersPage;
    }

}
