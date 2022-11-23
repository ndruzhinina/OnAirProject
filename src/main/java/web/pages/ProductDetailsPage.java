package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;

public class ProductDetailsPage extends AbstractNewEggPage {

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@class, 'qty-box')]//input")
    private WebElement qtyInput;

    @FindBy(xpath = "//div[contains(@class, 'product-buy')]//button[contains(text(), 'Add to cart ')]")
    private WebElement addToCartBtn;

    @FindBy(xpath = "//h1[contains(@class, 'product-title')]")
    private WebElement productTitleHeader;

    @FindBy(xpath = "//button[contains(text(), 'No, thanks')]")
    private WebElement noThanksBtn;

    @FindBy(xpath = "//div[contains(text(), 'Item has been added to cart.')]")
    private WebElement itemAddedMsg;

    public BigDecimal getCurrentPrice() {
        String dollars = driver.findElement(By.xpath("//li[contains(@class, 'price-current')]//strong")).getText();
        String cents = driver.findElement(By.xpath("//li[contains(@class, 'price-current')]//sup")).getText();

        return new BigDecimal(dollars + cents);
    }

    public String getProductTitle() {
        return productTitleHeader.getText();
    }

    public void addToCart(int qty) {
        qtyInput.clear();
        qtyInput.sendKeys(Integer.toString(qty));
        addToCartBtn.click();
        declinePurchaseProtection();
    }

    public void declinePurchaseProtection() {
        noThanksBtn.click();
        var wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(itemAddedMsg));
    }
}
