package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.util.List;

public class SearchResultsPage extends AbstractNewEggPage {

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = ".//button[contains(text(), 'View Details')]/preceding-sibling::button")
    private WebElement addToCartButton;

    @FindBy(xpath = "//div[contains(@class, 'modal-dialog')]//button[contains(text(), 'Add to cart ')]")
    private WebElement addToCartBtn;

    @FindBy(xpath = "//div[contains(@class, 'modal-dialog')]")
    private WebElement cartStatusDialog;

    @FindBy(xpath = "//div[contains(@class, 'modal-dialog')]//div[contains(@class, 'message-success')]//div[contains(text(), 'Item has been added to cart.')]")
    private WebElement itemAddedToCartMessage;

    @FindBy(xpath = "//div[contains(@class, 'item-cells-wrap')]//div[contains(@class, 'item-container')]")
    private List<WebElement> items;

    @FindBy(xpath = "//li[contains(text(), 'Search Results:')]")
    private WebElement pageSearchTitle;

    public BigDecimal getCurrentPriceForItem(int number) {
        WebElement item = items.get(number);
        String dollars = item.findElement(By.xpath(".//li[contains(@class, 'price-current')]//strong")).getText();
        String cents = item.findElement(By.xpath(".//li[contains(@class, 'price-current')]//sup")).getText();

        return new BigDecimal(dollars + cents);
    }

    public BigDecimal getCartSubTotalForAddedItem() {

        var wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(cartStatusDialog));

        String price = cartStatusDialog
                .findElement(By.xpath(".//div[contains(@class , 'item-actions')]/div[contains(@class , 'item-summary')]/strong"))
                .getText();
        System.out.println(price);
        return new BigDecimal(price.substring(3));
    }

    public void chooseProduct(int number) {
        items.get(number).findElement(By.xpath(".//div[contains(@class, 'btn-quickview')]")).click();
        addToCartBtn.click();
    }

    public boolean isCartStatusModalVisible() {
        return cartStatusDialog.isDisplayed();
    }

    public boolean isItemAddedToCartMessageDisplayed() {
        return itemAddedToCartMessage.isDisplayed();
    }

    public void searchPageIsOpened() {
        WebDriverWait wait = new WebDriverWait(driver, 5000);
        wait.until(ExpectedConditions.visibilityOf(pageSearchTitle));
    }
}
