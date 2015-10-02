package mobile.android.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by imy on 15/09/2015.
 */
public class AndroidTestBase {

    protected static AppiumDriver driver;
    protected Properties properties;

    public AppiumDriver getDriver() {
        if (driver == null) {
            try {
                setUp();
            } catch (Exception e) {
                System.out.println("[ERROR!] Was not able to return Appium Driver");
                e.printStackTrace();
            }
        }
        return driver;
    }

    @BeforeSuite
    public void setUp() throws Exception {
        properties = new Properties();

        String device = "androidDevice";
        String deviceName = System.getProperty("deviceName");

        File appDir = new File("src/main/resources/apps");
        String appName = System.getProperty("appName");
        File app = new File(appDir, appName);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.BROWSER_NAME, "");

        if (device.equalsIgnoreCase("androidSimulator")) {
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, "AndroidSimulator");
        } else if (device.equalsIgnoreCase("androidDevice")) {
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        }

        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        caps.setCapability("app-package", "com.example");
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 50);

        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps) {
            public MobileElement scrollTo(String s) {
                return null;
            }

            public MobileElement scrollToExact(String s) {
                return null;
            }
        };

        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
    }

    @AfterSuite
    public void tearDown() throws Exception {
        driver.quit();
    }

    private void readProperties() {
        // Reads some settings from the "application.properties" file
        String configFile = System.getProperty("configFile", "application.properties");
        properties = new Properties();
        try {
            properties.load(AndroidTestBase.class.getResourceAsStream("/" + configFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
