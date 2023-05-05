package io.github.mfaisalkhatri.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static io.github.mfaisalkhatri.drivers.DriverManager.getDriver;

/**
 * Created By Faisal Khatri on 03-05-2023
 */
public class SimpleFormPage {

    public void enterMessage(String message) {
        enterMessageField().sendKeys(message);
        getCheckedValueBtn().click();
    }

    public String yourMessage() {
        return getDriver().findElement(By.cssSelector("p#message")).getText();
    }

    private WebElement enterMessageField() {
        return getDriver().findElement(By.cssSelector("input#user-message"));
    }

    private WebElement getCheckedValueBtn() {
        return getDriver().findElement(By.cssSelector("#showInput"));
    }
}