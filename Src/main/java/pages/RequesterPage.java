package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.HeaderComponent;
import components.LogoutDialog;
import utils.ConfigReader;
import utils.WaitUtils;

public class RequesterPage extends BasePage {

    private final HeaderComponent headerComponent;
    private final LogoutDialog logoutDialog;

    private final Locator myAccessTabBtn;
    private final Locator myHistoryTabBtn;
    private final Locator activeEntitlementsTitle;
    private final Locator entitlementsSearchField;
    private final Locator myAccessTable;
    private final Locator cardPendingRequests;
    private final Locator cardAddAccess;
    private final Locator cardRemoveAccess;
    private final Locator approverSectionView;

    public RequesterPage(Page page) {
        super(page);
        this.headerComponent = new HeaderComponent(page);
        this.logoutDialog = new LogoutDialog(page);

        this.myAccessTabBtn = page.locator("div.kyraSectionToggleBar button:has-text('My Access')").first();
        this.myHistoryTabBtn = page.locator("div.kyraSectionToggleBar button:has-text('My History')").first();
        this.activeEntitlementsTitle = page.locator("h4.fioriCardHeaderTitle:has-text('Active Entitlements')").first();
        this.entitlementsSearchField = page.locator("input.kyraEntitlementsSearchField, input[placeholder*='Search entitlements']").first();
        this.myAccessTable = page.locator("#application-app-preview-component---AccessPage--myAccessTable, [id$='myAccessTable']").first();
        this.cardPendingRequests = page.locator("#application-app-preview-component---AccessPage--cardPendingRequests, [id$='cardPendingRequests']").first();
        this.cardAddAccess = page.locator("#application-app-preview-component---AccessPage--cardAddAccess, [id$='cardAddAccess']").first();
        this.cardRemoveAccess = page.locator("#application-app-preview-component---AccessPage--cardRemoveAccess, [id$='cardRemoveAccess']").first();
        this.approverSectionView = page.locator("#application-app-preview-component---AccessPage--approverSectionView, .kyraApproverCard").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(headerComponent.isHeaderVisible() ? page.locator(".kyraHeaderRolePill").first() : page.locator("header.sapFShellBar").first(), ConfigReader.getNavigationTimeout());
            return isRequesterPersonaDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public HeaderComponent getHeaderComponent() {
        return headerComponent;
    }

    public LogoutDialog getLogoutDialog() {
        return logoutDialog;
    }

    public boolean isRequesterPersonaDisplayed() {
        try {
            String role = headerComponent.getActivePersona();
            return "Requester".equalsIgnoreCase(role);
        } catch (Exception e) {
            return false;
        }
    }

    public String getActiveUserId() {
        return headerComponent.getActiveUserId();
    }

    public boolean isMyAccessTabVisible() {
        return myAccessTabBtn.isVisible();
    }

    public boolean isMyHistoryTabVisible() {
        return myHistoryTabBtn.isVisible();
    }

    public boolean isActiveEntitlementsTitleVisible() {
        return activeEntitlementsTitle.isVisible();
    }

    public boolean isEntitlementsSearchVisible() {
        return entitlementsSearchField.isVisible();
    }

    public boolean isMyAccessTableVisible() {
        return myAccessTable.isVisible();
    }

    public boolean isCardAddAccessVisible() {
        return cardAddAccess.isVisible();
    }

    public void clickAddAccess() {
        WaitUtils.waitForElementVisible(cardAddAccess, ConfigReader.getDefaultTimeout());
        cardAddAccess.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        cardAddAccess.click();
        WaitUtils.stabilize(page, 400);
    }

    public boolean isCardPendingRequestsVisible() {
        return cardPendingRequests.isVisible();
    }

    public boolean isCardRemoveAccessVisible() {
        return cardRemoveAccess.isVisible();
    }

    public boolean isApproverSectionHidden() {
        return !approverSectionView.isVisible();
    }

    public void signOut() {
        headerComponent.clickSignOut();
        if (logoutDialog.isDialogVisible()) {
            logoutDialog.confirmSignOut();
        }
        WaitUtils.stabilize(page, 1000);
    }
}
