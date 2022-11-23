package web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartPage extends AbstractNewEggPage {

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@class, 'item-cells-wrap')]//div[contains(@class, 'item-container')]")
    private List<WebElement> items;

    @FindBy(xpath = "//div[contains(@class, 'summary-content')]")
    private WebElement summaryBlock;

    public BigDecimal getCurrentPriceForItem() {
        String dollars = driver.findElement(By.xpath("//li[contains(@class, 'price-current')]//strong")).getText();
        String cents = driver.findElement(By.xpath("//li[contains(@class, 'price-current')]//sup")).getText();

        return new BigDecimal(dollars + cents);
    }

    public int getNumberOfProducts() {
       return (int) items.stream().count();
    }

    public int getQtyForItem(int num) {
        var webElement = items.get(num).findElement(By.xpath(".//div[contains(@class, 'item-qty')]/input"));
        return Integer.parseInt(webElement.getAttribute("value"));
    }

    public String getTitleForItem(int num) {
        var webElement = items.get(num).findElement(By.xpath(".//div[contains(@class, 'item-info')]/a"));
        return webElement.getText();
    }

    public BigDecimal getCartTotalWithoutDiscounts() {
        var webElement = summaryBlock.findElement(By.xpath(".//ul/li[contains(label, 'Item(s):')]//strong"));
        return new BigDecimal(webElement.getText());
    }
}
