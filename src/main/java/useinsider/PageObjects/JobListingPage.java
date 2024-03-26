package useinsider.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import useinsider.BaseComponents.BasePage;

import java.util.List;
import java.util.stream.Collectors;

public class JobListingPage extends BasePage {

    WebDriver driver;

    public JobListingPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    //ELEMENTS
    @FindBy(id = "page-head")
    WebElement pageHeadSection;

    @FindBy(id = "career-position-filter")
    WebElement jobFilterSection;

    @FindBy(id = "career-position-list")
    WebElement jobListSection;

    @FindBy(id = "pagination")
    WebElement paginationSection;

    @FindBy(id = "select2-filter-by-location-container")  //css = "span[aria-labelledby*='location']"
    WebElement filterLocationDropdown;

    @FindBy(id = "select2-filter-by-location-results")
    WebElement locationFilterResults;

    @FindBy(xpath = "//li[@id='select2-filter-by-location-result-vxql-Istanbul, Turkey']")  //deprecated
    WebElement istanbulLocationOption;

    @FindBy(id = "select2-filter-by-department-container")
    WebElement filterDepartmentDropdown;

    @FindBy(id = "select2-filter-by-department-results")
    WebElement departmentFilterResults;

    @FindBy(xpath = "//li[@id='select2-filter-by-department-result-7oh6-Quality Assurance']")  //deprecated
    WebElement qaDepartmentOption;

    @FindBy(css = "span ul")
    WebElement filterResults;

    @FindBy(css = "span ul li")
    List<WebElement> dropdownOptions;

    @FindBy(css = ".position-list-item")
    List<WebElement> jobListingItems;

    @FindBy(css = ".position-title")
    List<WebElement> positionTitles;

    @FindBy(css = "a[href*='jobs.lever']")
    List<WebElement> viewRoleButtons;

    By viewRoleBtnBy = By.cssSelector("a[href*='jobs.lever']");

    //ACTION METHODS
    public void waitPageLoad(){
        waitForElementToAppear(pageHeadSection);
        waitForElementToAppear(jobFilterSection);
        waitForElementToAppear(jobListSection);
        waitForElementToAppear(paginationSection);
    }

    public void selectDepartment(String departmentSelection){
        waitForElementToClick(filterDepartmentDropdown);
        clickElementAction(filterDepartmentDropdown);
        waitForElementToAppear(departmentFilterResults);
        waitForAllElementsToAppear(dropdownOptions);
        waitForAllElementsToClick(dropdownOptions);
        //System.out.println("# of selectable department filters: " + dropdownOptions.size());
        WebElement departmentToSelect = dropdownOptions.stream().
                filter(opt->opt.getText().equals(departmentSelection)).
                collect(Collectors.toList()).getFirst();
        waitForElementToClick(departmentToSelect);
        clickElementAction(departmentToSelect);
    }

    public boolean isDepartmentSelected(String departmentSelection){
        waitForElementToAppear(filterDepartmentDropdown);
        String selectedDepartment = filterDepartmentDropdown.getText().split("\n")[1];
        return selectedDepartment != null && selectedDepartment.equals(departmentSelection);
    }

    public void selectLocation(String locationSelection) {
        waitForElementToClick(filterLocationDropdown);
        clickElementAction(filterLocationDropdown);
        waitForElementToAppear(locationFilterResults);
        waitForAllElementsToAppear(dropdownOptions);
        waitForAllElementsToClick(dropdownOptions);
        //System.out.println("# of selectable location filters: " + dropdownOptions.size());
        WebElement locationToSelect = dropdownOptions.stream().
                filter(opt->opt.getText().equals(locationSelection)).
                toList().getFirst();
        waitForElementToClick(locationToSelect);
        clickElementAction(locationToSelect);
    }

    public boolean isLocationSelected(String locationSelection){
        waitForElementToAppear(filterLocationDropdown);
        String selectedLocation = filterLocationDropdown.getText().split("\n")[1];
        return selectedLocation != null && selectedLocation.equals(locationSelection);
    }

    public void filterJobs(String locationSelection, String departmentSelection) {
        selectLocation(locationSelection);
        selectDepartment(departmentSelection);
    }

    public void filterIstQaJobs(){  // deprecated - generic filtering methods based on test data are implemented
        waitForElementToClick(filterLocationDropdown);
        filterLocationDropdown.click();
        waitForElementToAppear(locationFilterResults);
        waitForElementToClick(istanbulLocationOption);
        istanbulLocationOption.click();
        waitForElementToClick(filterDepartmentDropdown);
        filterDepartmentDropdown.click();
        waitForElementToAppear(departmentFilterResults);
        waitForElementToClick(qaDepartmentOption);
        qaDepartmentOption.click();
    }

    public boolean areJobListingsPresent(){
        waitForAllElementsToAppear(jobListingItems);
        //System.out.println("# of filtered job results displayed: " + jobListingItems.size());
        return jobListingItems!=null && !jobListingItems.isEmpty();
    }

    public boolean verifyJobListingResults(String locationSelection, String departmentSelection){
        waitForAllElementsToAppear(jobListingItems);
        List<String> jobListingsPositions = jobListingItems.stream().
                map(item->item.findElement(By.cssSelector(".position-department")).getText()).toList();
        //System.out.println("Department displayed for filtered job results: " + jobListingsPositions.getFirst());
        Boolean areJobPositionsCorrect = jobListingsPositions.isEmpty() || jobListingsPositions.stream().allMatch(departmentSelection::equals);
        List<String> jobListingsLocations = jobListingItems.stream().
                map(item->item.findElement(By.cssSelector(".position-location")).getText()).toList();
        //System.out.println("Location displayed for filtered job results: " + jobListingsLocations.getFirst());
        Boolean areJobLocationsCorrect = jobListingsLocations.isEmpty() || jobListingsLocations.stream().allMatch(locationSelection::equals);
        return areJobPositionsCorrect && areJobLocationsCorrect;
    }

    public void viewJobPosition(String jobRole){
        waitForAllElementsToAppear(jobListingItems);
        WebElement jobToView = jobListingItems.stream().
                filter(job->job.findElement(By.cssSelector(".position-title")).getText().equals(jobRole)).
                toList().getFirst();
        clickElementJs(jobToView.findElement(viewRoleBtnBy));
    }

    public JobApplicationPage goToJobApplicationPage(String jobRole){
        viewJobPosition(jobRole);
        switchBrowserTab();
        JobApplicationPage jobApplicationPage = new JobApplicationPage(driver);
        return jobApplicationPage;
    }

}
