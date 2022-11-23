package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import web.pages.MainPage;
import web.pages.SearchResultsPage;

import java.util.concurrent.TimeUnit;

public class CheckTheLinkTests {

    private static MainPage mainPage;
    private static SearchResultsPage searchResultsPage;


    private static WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String url = ConfProperties.getProperty("startpage");
        driver.get(url);

        mainPage = new MainPage(driver);
    }

    @ParameterizedTest
    @ValueSource(strings = {"mouse" })
    public void checkSearchLinkFormat(String key) {
        mainPage.inputSearch(key);
        searchResultsPage = new SearchResultsPage(driver);
        searchResultsPage.searchPageIsOpened();
        String actualLink = driver.getCurrentUrl();

        Assertions.assertTrue(actualLink.startsWith(ConfProperties.getProperty("startpage")));
        Assertions.assertTrue(actualLink.endsWith(key));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Home & Outdoors", "Electronics", "Office Solutions"})
    public void checkTheLinkIsCorrect(String topic) {
        mainPage.clickMenuLink(topic);
        mainPage.checkTheLinkIsCorrect(topic);
    }

    @AfterEach
    public void afterClass() {
        driver.quit();
    }
}
