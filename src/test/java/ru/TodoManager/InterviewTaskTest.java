package ru.TodoManager;

import io.restassured.response.Response;
import io.restassured.http.Cookie;
import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.lang.String;

import static org.hamcrest.MatcherAssert.assertThat;

public class InterviewTaskTest extends Assert {

    String driverPath = "C:\\chromedriver1.exe";
    public WebDriver driver;

    @Test(dataProvider = "getGoodCredentials",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testUI"})
    public void checkGoodCredentials(String username, String password) {
        InterviewTask.getLogin(driver, username, password);
        try {
            driver.findElement(By.xpath("//div[@id='main-page']"));
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            throw ex;
        }
        driver.manage().deleteAllCookies();
    }

    @Test(dataProvider = "getBadCredentials",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testUI"})
    public void checkBadCredentials(String username, String password) {
        InterviewTask.getLogin(driver, username, password);
        String elementError = "";
        try {
            elementError = driver.findElement(By.xpath("//span[@class='error']")).getText();
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            throw ex;
        }
        Assert.assertTrue(elementError.contains("Неверные логин или пароль"));
        driver.manage().deleteAllCookies();
    }

    @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testUI"})
    public void checkLogOutUI(String username, String password) {
        driver.manage().deleteAllCookies();
        InterviewTask.getLogin(driver, username, password);
        driver.findElement(By.xpath("//*[@class='page-header']/div/button")).click();
        driver.findElement(By.xpath("//*[@id='login-page']"));
    }

    @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testAPI"})
    public void checkApiLogin(String username, String password) {
        Response response = InterviewTask.getLoginAPI(username, password);
        InterviewTask.checkResponseStatus(response);
    }

        @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testAPI"})
    public void checkApiLogout(String username, String password) {
        Response response = InterviewTask.getLogoutAPI(username, password);
        InterviewTask.checkResponseStatus(response);
    }

    @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testAPI"},
            dependsOnMethods = "checkToDoRemoveAPI")
    public void checkToDoCreateAPI(String username, String password) {
        Response responseLogin = InterviewTask.getLoginAPI(username, password);
        Response responseCreateToDo = InterviewTask.CreateToDoApi(responseLogin.getDetailedCookie("Authorization"));
        InterviewTask.checkResponseStatus(responseCreateToDo);
    }

    @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testUI"})
    public void checkToDoCreateUI(String username, String password) {
        driver.manage().deleteAllCookies();
        InterviewTask.getLogin(driver, username, password);
        String[] arrAfterCreate = InterviewTask.CreateToDoUI(driver);

        String result = Integer.toString(Integer.parseInt(arrAfterCreate[0]) + 1);

        Assert.assertEquals(result, arrAfterCreate[1]);
        Assert.assertEquals(arrAfterCreate[2], arrAfterCreate[3]);
        driver.manage().deleteAllCookies();
    }

    @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testUI"})
    public void checkToDoRemoveUI(String username, String password) {
        driver.manage().deleteAllCookies();

        String[] arrAfterRemove = InterviewTask.removeToDoUI(driver, username, password);
        String result = Integer.toString(Integer.parseInt(arrAfterRemove[1]) + 1);
        Assert.assertEquals(result, arrAfterRemove[0]);
        driver.manage().deleteAllCookies();
    }

    @Test(dataProvider = "credoLogin",
            dataProviderClass = InterviewTaskData.class,
            groups = {"testAPI"})
    public void checkToDoRemoveAPI(String username, String password) {
        Response responseLogin = InterviewTask.getLoginAPI(username, password);
        Cookie cookie = responseLogin.getDetailedCookie("Authorization");
        Response responseRemoveTodo = InterviewTask.RemoveToDoApi(cookie);
        InterviewTask.checkResponseStatus(responseRemoveTodo);
    }


    @BeforeTest(groups = {"testUI"})
    public void launchBrowser() {
        System.out.println("this is before test");
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
    }

    @AfterTest(groups = {"testUI"})
    public void terminateBrowser() {
        System.out.println("this is after test");
        driver.close();
    }


}