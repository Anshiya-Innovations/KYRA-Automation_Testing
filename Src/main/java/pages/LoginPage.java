package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.PersonaSelector;
import utils.ConfigReader;
import utils.WaitUtils;

public class LoginPage extends BasePage {

    private final PersonaSelector personaSelector;
    private final Locator employeeInput;
    private final Locator signInButton;
    private final Locator errorBanner;
    private final Locator errorMessage;
    private final Locator cardContainer;

    public LoginPage(Page page) {
        super(page);
        this.personaSelector = new PersonaSelector(page);
        this.employeeInput = page.locator("#application-app-preview-component---Login--idInput-inner, [id$='idInput-inner']").first();
        this.signInButton = page.locator("#application-app-preview-component---Login--signInButton, [id$='signInButton']").first();
        this.errorBanner = page.locator("#application-app-preview-component---Login--kyraLoginErrorBanner, .kyraCustomErrorBanner").first();
        this.errorMessage = page.locator(".kyraErrorMessage").first();
        this.cardContainer = page.locator("#kyraLoginCardContainer, .kyraLoginCardContainer").first();
    }

    public void navigate() {
        String url = ConfigReader.getBaseUrl();
        page.navigate(url);
        applyZoom();
        WaitUtils.waitForNetworkIdleOrStabilized(page);
    }

    public void applyZoom() {
        double zoom = ConfigReader.getBrowserZoom();
        if (zoom > 0 && zoom != 1.0) {
            String zoomPercent = (int) (zoom * 100) + "%";
            try {
                page.evaluate("zoom => {" +
                        "  if (document.body) document.body.style.zoom = zoom;" +
                        "  if (document.documentElement) document.documentElement.style.zoom = zoom;" +
                        "}", zoomPercent);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public boolean isLoaded() {
        try {
            applyZoom();
            WaitUtils.waitForElementVisible(employeeInput, ConfigReader.getDefaultTimeout());
            WaitUtils.waitForElementVisible(signInButton, ConfigReader.getDefaultTimeout());
            return employeeInput.isVisible() && signInButton.isVisible() && personaSelector.isAvailable();
        } catch (Exception e) {
            return false;
        }
    }

    public PersonaSelector getPersonaSelector() {
        return personaSelector;
    }

    public void selectPersona(String personaName) {
        personaSelector.selectPersona(personaName);
    }

    public String getSelectedPersona() {
        return personaSelector.getSelectedPersona();
    }

    public void enterEmployeeId(String employeeId) {
        WaitUtils.waitForElementVisible(employeeInput);
        employeeInput.fill(employeeId);
        WaitUtils.stabilize(page, 200);
    }

    public String getEnteredEmployeeId() {
        WaitUtils.waitForElementVisible(employeeInput);
        return employeeInput.inputValue();
    }

    public void clickSignIn() {
        WaitUtils.waitForElementVisible(signInButton);
        signInButton.click();
        WaitUtils.stabilize(page, 500);
    }

    public boolean hasError() {
        try {
            return errorBanner.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        if (hasError()) {
            return errorMessage.innerText().trim();
        }
        return "";
    }
}
