package org.example;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import web.pages.ProductDetailsPage;
import web.pages.ShoppingCartPage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AddToCartFromProductDetailsPagesTest {

    private static WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    // Setting up:
    // Add to cart 1 item of the 1st product,
    // 2 items of the 2nd product
    // and so on.
    //
    // Validation:
    // - validate the total price in the cart (before discounts)
    // - validate total number of different products
    // - validate quantities of added products.
    // - validate the titles of products (e.g. the cart contains all the products that were added, and only these)
    @Test
    public void CanAddMultipleProductsToCart() {
        // The product ids for product details page URLs (should be different to prevent summing up):
        var productIds = new String[] {
            "/p/32K-0137-00001",
            "microsoft-elg-00001-surface-arc-mouse/p/2NS-0058-00003"
        };

        var expectedTotal = new BigDecimal(0);
        var productQtys = new HashMap<String, Integer>(); // Save here qtys of each product added

        // Put into the cart 1 item of the 1st product, 2 items of the 2nd, and so on.
        for(int i = 0; i < productIds.length; i++) {

            String productUrl = ConfProperties.getProperty("startpage") + productIds[i];
            driver.get(productUrl);

            var productDetailsPage = new ProductDetailsPage(driver);
            productDetailsPage.closeGdprIfPresent();
            var productTitle = productDetailsPage.getProductTitle();

            BigDecimal currentItemPrice = productDetailsPage.getCurrentPrice();
            int qtyToAdd = i + 1;
            productDetailsPage.addToCart(qtyToAdd);

            productQtys.put(productTitle, qtyToAdd);
            expectedTotal = expectedTotal.add(currentItemPrice.multiply(new BigDecimal(qtyToAdd)));
        }

        driver.get(ConfProperties.getProperty("shoppingcartpage"));
        var shoppingCartPage = new ShoppingCartPage(driver);

        int totalProductsInCart = shoppingCartPage.getNumberOfProducts();
        var totalInCartWithoutDiscounts = shoppingCartPage.getCartTotalWithoutDiscounts();

        // Assertions
        Assert.assertEquals(totalProductsInCart, productIds.length); // The number of different products in the cart
        Assert.assertTrue(totalInCartWithoutDiscounts.equals(expectedTotal)); // Total price

        for(int i = 0; i < productIds.length; i++) {
            int productQtyInCart = shoppingCartPage.getQtyForItem(i);
            String productTitleInCart = shoppingCartPage.getTitleForItem(i);

            Assert.assertTrue(productQtys.containsKey(productTitleInCart)); // Title of the product
            Assert.assertEquals(productQtys.get(productTitleInCart).intValue(), productQtyInCart); // Quantity of the product

            productQtys.remove(productTitleInCart);
        }

        Assert.assertTrue(productQtys.isEmpty()); // Potentially there could be a bug when this fails.
    }

    @AfterEach
    public void afterClass() {
        driver.quit();
    }
}
