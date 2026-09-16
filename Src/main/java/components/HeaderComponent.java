package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.WaitUtils;

public class HeaderComponent {

    private final Page page;
    private final Locator signOutBtn;
    private final Locator activePersonaPill;
    private final Locator activeUserIdPill;
    private final Locator welcomeTitle;

    public HeaderComponent(Page page) {
        this.page = page;
        this.signOutBtn = page.locator("button.kyraSignOutHeaderBtn, button:has-text('Sign Out')").first();
        this.activePersonaPill = page.locator(".kyraBannerPersonaRow .kyraHeaderRolePill, .kyraHeaderRolePill").first();
        this.activeUserIdPill = page.locator(".kyraHeaderUserIdPill .kyraUserIdValue, .kyraUserIdValue").first();
        this.welcomeTitle = page.locator("h2.fioriWelcomeTitle, .fioriWelcomeTitle").first();
    }

    public boolean isHeaderVisible() {
        try {
            WaitUtils.waitForElementVisible(signOutBtn);
            return signOutBtn.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getActivePersona() {
        WaitUtils.waitForElementVisible(activePersonaPill);
        return activePersonaPill.innerText().trim();
    }

    public String getActiveUserId() {
        WaitUtils.waitForElementVisible(activeUserIdPill);
        return activeUserIdPill.innerText().trim();
    }

    public void clickSignOut() {
        WaitUtils.waitForElementVisible(signOutBtn);
        signOutBtn.click();
        WaitUtils.stabilize(page, 400);
    }
}
