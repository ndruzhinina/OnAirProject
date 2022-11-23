package web.pages;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends AbstractNewEggPage {

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = ".//input[contains(@title, 'Search Site')]")
    private WebElement searchField;

    @FindBy(xpath = "//button[contains(@aria-label, 'Close')]")
    private WebElement promoPopupCloseButton;

    @FindBy(xpath = "(//a[contains(@title, 'Home & Outdoors')])[2]")
    private WebElement HomeAndOutdoorMenuLink;

    @FindBy(xpath = "(//a[contains(@title, 'Electronics')])[3]")
    private WebElement ElectronicsMenuLink;

    @FindBy(xpath = "(//a[contains(@title, 'Office Solutions')])[2]")
    private WebElement OfficeSolutionsMenuLink;

    public void inputSearch(String key) {
        closeGdprIfPresent();
        searchField.sendKeys(key);
        closePopupIfPresent();
        searchField.sendKeys(Keys.ENTER);
    }

    public void closePopupIfPresent() {
        if (promoPopupCloseButton.isDisplayed()) {
            promoPopupCloseButton.click();
        }
    }

    public void clickMenuLink(String topic) {
        switch (topic) {
            case "Home & Outdoors":
                HomeAndOutdoorMenuLink.click();;
                break;
            case "Electronics":
                ElectronicsMenuLink.click();
                break;
            case "Office Solutions":
                OfficeSolutionsMenuLink.click();
                break;
            default:
                throw new IllegalArgumentException("There is no such topic.");
        }
    }

    public void checkTheLinkIsCorrect(String topic) {
        switch (topic) {
            case "Home & Outdoors":
                Assert.assertEquals(driver.getCurrentUrl(), "https://www.newegg.com/Home-Outdoors/Store/ID-15");
                break;
            case "Electronics":
                Assert.assertEquals(driver.getCurrentUrl(), "https://www.newegg.com/Electronics/Store/ID-10");
                break;
            case "Office Solutions":
                Assert.assertEquals(driver.getCurrentUrl(), "https://www.newegg.com/Office-Solutions/Store/ID-133");
                break;
            default:
                throw new IllegalArgumentException("There is no such topic.");
        }
    }
}
