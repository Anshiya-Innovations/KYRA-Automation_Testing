package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ConfigReader;

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
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(ConfigReader.isHeadless())
                        .setSlowMo(ConfigReader.getSlowMo())
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
        // Use a completely isolated, clean browser context for each test
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1440, 900)
        );
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
