package io.github.mfaisalkhatri.tests;

import static io.github.mfaisalkhatri.drivers.DriverManager.getDriver;
import static org.testng.Assert.assertEquals;

import io.github.mfaisalkhatri.pages.SimpleFormPage;
import org.testng.annotations.Test;

/**
 * Created By Faisal Khatri on 03-05-2023
 */
public class SeleniumPlaygroundTests extends BaseTest {

    @Test
    public void testSimpleFormDemo () {
        getDriver ().navigate ()
            .to ("https://www.lambdatest.com/selenium-playground/simple-form-demo");
        String message = "This is a test!";
        SimpleFormPage simpleFormPage = new SimpleFormPage ();
        simpleFormPage.enterMessage (message);
        assertEquals (simpleFormPage.yourMessage (), message);
    }
}