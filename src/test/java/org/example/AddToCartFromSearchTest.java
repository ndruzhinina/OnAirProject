package org.example;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import web.pages.SearchResultsPage;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class AddToCartFromSearchTest {

    private static SearchResultsPage searchResultsPage;
    private static WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    // Add to cart one item of a product listed in the product search result page.
    // Use the Quick View popup modal to add the item (do not go to the product details page)
    // Validation:
    // - user can see "item was added message"
    // - the cart subtotal equals to the current price of the product.
    @ParameterizedTest
    @ValueSource(strings = {"mouse" })
    public void CanAddProductToCartFromSearchPageResult(String key) {
        String url = ConfProperties.getProperty("searchresultpage") + key;
        driver.get(url);

        searchResultsPage = new SearchResultsPage(driver);
        searchResultsPage.closeGdprIfPresent();
        BigDecimal currentItemPrice = searchResultsPage.getCurrentPriceForItem(1);

        // Open the Quick View modal and add to cart trom there.
        searchResultsPage.chooseProduct(1);

        Assertions.assertTrue(searchResultsPage.isCartStatusModalVisible());
        Assertions.assertTrue(searchResultsPage.isItemAddedToCartMessageDisplayed());
        Assertions.assertTrue(currentItemPrice.equals(searchResultsPage.getCartSubTotalForAddedItem()));
    }

    @AfterEach
    public void afterClass() {
        driver.quit();
    }

}
