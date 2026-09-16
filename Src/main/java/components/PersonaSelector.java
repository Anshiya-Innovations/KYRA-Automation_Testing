package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.WaitUtils;

public class PersonaSelector {

    private final Page page;
    private final Locator roleSelectContainer;
    private final Locator roleSelectLabel;

    public PersonaSelector(Page page) {
        this.page = page;
        this.roleSelectContainer = page.locator("#application-app-preview-component---Login--roleSelect, [id$='roleSelect']").first();
        this.roleSelectLabel = page.locator("#application-app-preview-component---Login--roleSelect-label, [id$='roleSelect-label']").first();
    }

    public boolean isAvailable() {
        try {
            WaitUtils.waitForElementVisible(roleSelectContainer);
            return roleSelectContainer.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectPersona(String personaName) {
        WaitUtils.waitForElementVisible(roleSelectContainer);
        WaitUtils.stabilize(page, 200);

        // Click the visible container to open the SAPUI5 dropdown popup
        roleSelectContainer.click();
        WaitUtils.stabilize(page, 300);

        // Target the visible option inside the opened popover
        // Note: The placeholder item "Select persona" is hidden, so we specifically wait for the target option
        Locator targetOption = page.locator(".sapMPopover li[role='option'], li[role='option']")
                .filter(new Locator.FilterOptions().setHasText(personaName))
                .first();

        WaitUtils.waitForElementVisible(targetOption, 8000);
        targetOption.click();
        WaitUtils.stabilize(page, 300);
    }

    public String getSelectedPersona() {
        WaitUtils.waitForElementVisible(roleSelectLabel);
        return roleSelectLabel.innerText().trim();
    }

    public boolean verifyPersonaSelected(String expectedPersona) {
        String actual = getSelectedPersona();
        return expectedPersona.equalsIgnoreCase(actual);
    }
}
