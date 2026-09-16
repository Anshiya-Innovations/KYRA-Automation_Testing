package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ViewportSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TestFailureWatcher.class)
public abstract class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;

    protected BrowserContext context;
    protected Page page;

    protected String currentPersona;
    protected String currentEmployeeId;
    protected String currentStep = "Initialization";

    @BeforeAll
    public static void setUpClass() {
        playwright = Playwright.create();
        List<String> args = new ArrayList<>();
        if (ConfigReader.isMaximized()) {
            args.add("--start-maximized");
        }
        // Enforce 1:1 device scale factor to prevent high-DPI Windows display scaling from over-zooming
        double scaleFactor = ConfigReader.getDeviceScaleFactor();
        if (scaleFactor > 0) {
            args.add("--force-device-scale-factor=" + scaleFactor);
        }
        args.add("--window-size=" + ConfigReader.getViewportWidth() + "," + ConfigReader.getViewportHeight());
        args.add("--disable-blink-features=AutomationControlled");

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(ConfigReader.isHeadless())
                        .setSlowMo(ConfigReader.getSlowMo())
                        .setArgs(args)
        );
    }

    @AfterAll
    public static void tearDownClass() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    @BeforeEach
    public void setUp() {
        Browser.NewContextOptions options = new Browser.NewContextOptions();
        if (ConfigReader.isMaximized()) {
            // Null viewport allows Chromium to use full native maximized window dimensions
            options.setViewportSize((ViewportSize) null);
        } else {
            options.setViewportSize(ConfigReader.getViewportWidth(), ConfigReader.getViewportHeight());
        }

        context = browser.newContext(options);
        page = context.newPage();

        page.setDefaultTimeout(ConfigReader.getDefaultTimeout());
        page.setDefaultNavigationTimeout(ConfigReader.getNavigationTimeout());
    }

    @AfterEach
    public void tearDown() {
        if (context != null) {
            context.close();
            context = null;
        }
    }

    public Page getPage() {
        return page;
    }

    public String getCurrentPersona() {
        return currentPersona;
    }

    public void setCurrentPersona(String currentPersona) {
        this.currentPersona = currentPersona;
    }

    public String getCurrentEmployeeId() {
        return currentEmployeeId;
    }

    public void setCurrentEmployeeId(String currentEmployeeId) {
        this.currentEmployeeId = currentEmployeeId;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }
}
