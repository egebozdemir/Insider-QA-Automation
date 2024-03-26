package useinsider.Tests;

import org.testng.asserts.SoftAssert;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import useinsider.PageObjects.CareersPage;
import useinsider.PageObjects.JobApplicationPage;
import useinsider.PageObjects.JobListingPage;
import useinsider.PageObjects.QaTeamPage;
import useinsider.TestComponets.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BootcampTest extends BaseTest {

    @Test(dataProvider = "getData")
    public void genericJobSearchTest(HashMap<String,String[]> input) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        CareersPage careersPage = homePage.goToCareersPage();
        careersPage.waitPageLoad();
        Assert.assertTrue(careersPage.verifyTeamsLocationsLifePresent());
        softAssert.assertTrue(careersPage.verifyTeamTitles(input.get("teamTitles")));
        softAssert.assertTrue(careersPage.verifyOfficeLocations(input.get("officeLocations")));
        softAssert.assertTrue(careersPage.verifyLifeAtInsiderText(input.get("lifeAtInsiderText")[0]));
        JobListingPage jobListingPage = careersPage.goToJobsListingPage();
        jobListingPage.waitPageLoad();
        jobListingPage.filterJobs(input.get("jobLocation")[0], input.get("jobDepartment")[0]);
        Thread.sleep(5000);
        Assert.assertTrue(jobListingPage.areJobListingsPresent());
        softAssert.assertTrue(jobListingPage.verifyJobListingResults(input.get("jobLocation")[0], input.get("jobDepartment")[0]));
        JobApplicationPage jobApplicationPage = jobListingPage.goToJobApplicationPage(input.get("jobRoleTitle")[0]);
        jobApplicationPage.waitPageLoad();
        Assert.assertTrue(jobApplicationPage.verifyLeverPageUrl(input.get("leverApplicationUrl")[0]));
        softAssert.assertTrue(jobApplicationPage.verifyJobTitle(input.get("jobRoleTitle")[0]));
        softAssert.assertAll();
    }

    @Test
    public void qaJobSearchTest() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        CareersPage careersPage = homePage.goToCareersPage();
        careersPage.waitPageLoad();
        Assert.assertTrue(careersPage.verifyTeamsLocationsLifePresent());
        QaTeamPage qaTeamPage = careersPage.goToQaTeamPage();
        qaTeamPage.waitPageLoad();
        JobListingPage jobListingPage = qaTeamPage.goToJobListingPage();
        jobListingPage.waitPageLoad();
        softAssert.assertTrue(jobListingPage.isDepartmentSelected("Quality Assurance"));
        jobListingPage.selectLocation("Istanbul, Turkey");
        softAssert.assertTrue(jobListingPage.isLocationSelected("Istanbul, Turkey"));
        Thread.sleep(5000);
        Assert.assertTrue(jobListingPage.areJobListingsPresent());
        softAssert.assertTrue(jobListingPage.verifyJobListingResults("Istanbul, Turkey", "Quality Assurance"));
        JobApplicationPage jobApplicationPage = jobListingPage.goToJobApplicationPage("Software Quality Assurance Engineer");
        jobApplicationPage.waitPageLoad();
        Assert.assertTrue(jobApplicationPage.verifyLeverPageUrl("jobs.lever.co"));
        softAssert.assertTrue(jobApplicationPage.verifyJobTitle("Software Quality Assurance Engineer"));
        softAssert.assertAll();
    }

    @DataProvider
    public Object[][] getData() throws IOException{
        //calling getJsonDataToMap inherited from BaseTest to get List of Hashmaps as test data
        List<HashMap<String,String[]>> data = getJsonDataToMap(System.getProperty("user.dir")+"/src/test/java/useinsider/Data/BootcampTask.json");
        return new Object[][] {{data.get(0)}, {data.get(1)}}; //second test data (invalid) to showcase failed case
    }
}
