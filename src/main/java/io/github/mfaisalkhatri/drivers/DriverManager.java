package io.github.mfaisalkhatri.drivers;

import io.github.mfaisalkhatri.enums.Browsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

import static java.text.MessageFormat.format;

/**
 * Created By Faisal Khatri on 03-05-2023
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final String GRID_URL = "@hub.lambdatest.com/wd/hub";
    private static final Logger LOG = LogManager.getLogger("DriverManager.class");
    private static final String LT_ACCESS_KEY = System.getProperty("LT_ACCESS_KEY");
    private static final String LT_USERNAME = System.getProperty("LT_USERNAME");

    private DriverManager() {
    }

    public static void createDriver(final Browsers browser) {
        switch (browser) {
            case FIREFOX:
                setupFirefoxDriver();
                break;
            case REMOTE_CHROME:
                setupRemoteChrome();
                break;
            case REMOTE_FIREFOX:
                setupRemoteFirefox();
                break;
            case CHROME:
            default:
                setupChromeDriver();
        }
        setupBrowserTimeouts();
    }

    public static WebDriver getDriver() {
        return DriverManager.DRIVER.get();
    }

    private static void setDriver(final WebDriver driver) {
        DriverManager.DRIVER.set(driver);
    }

    public static void quitDriver() {
        if (null != DRIVER.get()) {
            getDriver().quit();
            DRIVER.remove();
        }
    }

    private static HashMap<String, Object> ltOptions() {
        final HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", LT_USERNAME);
        ltOptions.put("accessKey", LT_ACCESS_KEY);
        ltOptions.put("resolution", "2560x1440");
        ltOptions.put("selenium_version", "4.0.0");
        ltOptions.put("build", "LambdaTest Playground Build");
        ltOptions.put("name", "LambdaTest Playground Tests");
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        return ltOptions;
    }

    private static void setupBrowserTimeouts() {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }

    private static void setupChromeDriver() {
        final boolean isHeadless = Boolean.parseBoolean(Objects.requireNonNullElse(System.getProperty("headless"), "true"));

        final ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        if (isHeadless) {
            options.addArguments("--headless");

        }
        setDriver(new ChromeDriver(options));
    }

    private static void setupFirefoxDriver() {
        LOG.info("Setting up Firefox Driver....");
        final boolean isHeadless = Boolean.parseBoolean(Objects.requireNonNullElse(System.getProperty("headless"), "true"));
        final FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--remote-allow-origins=*");
        if (isHeadless) {
            options.addArguments("--headless");
        }
        setDriver(new FirefoxDriver(options));
        LOG.info("Firefox Driver created successfully!");
    }

    private static void setupRemoteChrome() {
        final ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("112.0");
        browserOptions.setCapability("LT:Options", ltOptions());
        try {
            setDriver(new RemoteWebDriver(new URL(format("https://{0}:{1}{2}", LT_USERNAME, LT_ACCESS_KEY, GRID_URL)), browserOptions));
        } catch (final MalformedURLException e) {
            LOG.error("Error setting up cloud browser in remote(LambdaTest", e);
        }

    }

    private static void setupRemoteFirefox() {
        final FirefoxOptions browserOptions = new FirefoxOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("112.0");
        browserOptions.setCapability("LT:Options", ltOptions());
        try {
            setDriver(new RemoteWebDriver(new URL(format("https://{0}:{1}{2}", LT_USERNAME, LT_ACCESS_KEY, GRID_URL)), browserOptions));
        } catch (final MalformedURLException e) {
            LOG.error("Error setting up firefox  browser in LambdaTest", e);
        }

    }

}