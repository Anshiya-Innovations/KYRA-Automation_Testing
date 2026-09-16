package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.HeaderComponent;
import components.LogoutDialog;
import utils.ConfigReader;
import utils.WaitUtils;

public class ApproverPage extends BasePage {

    private final HeaderComponent headerComponent;
    private final LogoutDialog logoutDialog;

    private final Locator approverCard;
    private final Locator approverTitle;
    private final Locator approverSubtitle;
    private final Locator pendingQueueBtn;
    private final Locator historyLogBtn;
    private final Locator accessRequestsTab;
    private final Locator revokeRequestsTab;
    private final Locator approvalAccessTable;
    private final Locator searchField;
    private final Locator filterBtn;
    private final Locator exportBtn;

    public ApproverPage(Page page) {
        super(page);
        this.headerComponent = new HeaderComponent(page);
        this.logoutDialog = new LogoutDialog(page);

        this.approverCard = page.locator("#application-app-preview-component---AccessPage--approverSectionView, .kyraApproverCard").first();
        this.approverTitle = page.locator(".fioriApproverTitle").first();
        this.approverSubtitle = page.locator(".kyraSubtitleWithTealBar").first();
        this.pendingQueueBtn = page.locator(".kyraApproverPillGroup button:has-text('Pending')").first();
        this.historyLogBtn = page.locator(".kyraApproverPillGroup button:has-text('History')").first();
        this.accessRequestsTab = page.locator(".kyraAccessRevokeBar button:has-text('Access')").first();
        this.revokeRequestsTab = page.locator(".kyraAccessRevokeBar button:has-text('Revoke')").first();
        this.approvalAccessTable = page.locator("#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable, [id$='approvalAccessTable']").first();
        this.searchField = page.locator("input[placeholder*='Search request ID'], .kyraEntitlementsSearchField").first();
        this.filterBtn = page.locator("button.kyraApproverFilterBtn, button:has-text('Filter')").first();
        this.exportBtn = page.locator("button.kyraApproverExportBtn, button:has-text('Export')").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(approverCard, ConfigReader.getNavigationTimeout());
            return isApproverPersonaDisplayed() && approverCard.isVisible();
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

    public boolean isApproverPersonaDisplayed() {
        try {
            String role = headerComponent.getActivePersona();
            return "Approver".equalsIgnoreCase(role);
        } catch (Exception e) {
            return false;
        }
    }

    public String getActiveUserId() {
        return headerComponent.getActiveUserId();
    }

    public String getApproverSectionTitle() {
        WaitUtils.waitForElementVisible(approverTitle);
        return approverTitle.innerText().trim();
    }

    public boolean isUserRequestsTitleDisplayed() {
        try {
            String title = getApproverSectionTitle();
            return "User Requests".equalsIgnoreCase(title);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPendingQueueBtnVisible() {
        return pendingQueueBtn.isVisible();
    }

    public boolean isHistoryLogBtnVisible() {
        return historyLogBtn.isVisible();
    }

    public boolean isAccessRequestsTabVisible() {
        return accessRequestsTab.isVisible();
    }

    public boolean isRevokeRequestsTabVisible() {
        return revokeRequestsTab.isVisible();
    }

    public boolean isApprovalAccessTableVisible() {
        return approvalAccessTable.isVisible();
    }

    public boolean isFilterBtnVisible() {
        return filterBtn.isVisible();
    }

    public boolean isExportBtnVisible() {
        return exportBtn.isVisible();
    }

    public void signOut() {
        headerComponent.clickSignOut();
        if (logoutDialog.isDialogVisible()) {
            logoutDialog.confirmSignOut();
        }
        WaitUtils.stabilize(page, 1000);
    }
}
