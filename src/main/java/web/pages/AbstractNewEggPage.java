package web.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class AbstractNewEggPage {

    protected WebDriver driver;

    @FindBy(xpath = "//button[contains(@class, 'osano-cm-accept-all')]")
    private WebElement gdprAcceptAll;

    public AbstractNewEggPage(WebDriver driver) {
        this.driver = driver;
    }

    public void closeGdprIfPresent() {
        if (gdprAcceptAll.isDisplayed()) {
            gdprAcceptAll.click();
        }
    }
}
