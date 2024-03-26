package useinsider.TestComponets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import useinsider.PageObjects.HomePage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class BaseTest {

    public WebDriver driver;
    public HomePage homePage;

    //FACTORY METHOD
    public WebDriver initializeDriver() throws IOException {

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/useinsider/Resources/Global.properties");
        prop.load(fis);
        String browserName = prop.getProperty("browser");

        switch (browserName.toLowerCase()){
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "safari":
                WebDriverManager wdmSafari = WebDriverManager.safaridriver().browserInDocker();
                driver = wdmSafari.create();
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //driver.manage().window().maximize();

        try{
            int width = Integer.parseInt(prop.getProperty("windowWidth"));
            int height = Integer.parseInt(prop.getProperty("windowHeight"));
            Dimension dimension = new Dimension(width, height);
            driver.manage().window().setSize(dimension);
        }
        catch (Exception e){
            driver.manage().window().maximize();
        }
        return driver;
    }

    //data reader method
    public List<HashMap<String, String[]>> getJsonDataToMap(String filePath) throws IOException {
        //read json to string
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        //convert string array to hashmap (Jackson Databind - Object Mapper)
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String,String[]>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String,String[]>>>(){});
        return data;
        //returns: List {Hmap, Hmap, ...}
    }

    //screenshot methods
    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {  //for extent report
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File target = new File(System.getProperty("user.dir")+"/reports/"+testCaseName+".png");
        FileUtils.copyFile(source, target);
        return testCaseName+".png";
        //returns: screenshot path name
    }

    public String getScreenshotAbsPath(String testCaseName, WebDriver driver) throws IOException {  //for database
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File target = new File(System.getProperty("user.dir")+"/reports/"+testCaseName+".png");
        FileUtils.copyFile(source, target);
        return target.getPath();
        //returns: full screenshot path
    }

    @BeforeMethod(alwaysRun = true)
    public HomePage launchApplication() throws IOException {
        driver = initializeDriver();
        homePage = new HomePage(driver);
        homePage.goTo();
        Assert.assertTrue(homePage.verifyHomePageAccessibility());
        homePage.closeCookieOverlay();
        return homePage;
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver(){
        //driver.close();
        driver.quit();
    }

}
