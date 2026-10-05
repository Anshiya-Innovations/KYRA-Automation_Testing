package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.HeaderComponent;
import components.LogoutDialog;
import utils.ConfigReader;
import utils.WaitUtils;

public class ComplianceReviewPage extends BasePage {

    private final HeaderComponent headerComponent;
    private final LogoutDialog logoutDialog;

    private final Locator complianceCard;
    private final Locator complianceTitle;
    private final Locator complianceSubtitle;
    private final Locator pendingQueueBtn;
    private final Locator historyLogBtn;
    private final Locator accessRequestsTab;
    private final Locator revokeRequestsTab;
    private final Locator approvalAccessTable;
    private final Locator searchField;
    private final Locator filterBtn;
    private final Locator exportBtn;

    public ComplianceReviewPage(Page page) {
        super(page);
        this.headerComponent = new HeaderComponent(page);
        this.logoutDialog = new LogoutDialog(page);

        this.complianceCard = page.locator("#application-app-preview-component---AccessPage--approverSectionView, .kyraApproverCard").first();
        this.complianceTitle = page.locator(".fioriApproverTitle").first();
        this.complianceSubtitle = page.locator(".kyraSubtitleWithTealBar").first();
        this.pendingQueueBtn = page.locator(".kyraApproverPillGroup button:has-text('Pending'), button.kyraApproverPillBtn:has-text('Pending'), button[title*='Pending Queue']").first();
        this.historyLogBtn = page.locator(".kyraApproverPillGroup button:has-text('History'), button.kyraApproverPillBtn:has-text('History'), button[title*='History']").first();
        this.accessRequestsTab = page.locator(".kyraAccessRevokeBar button:has-text('Access'), button.kyraTabPillBtn:has-text('Access')").first();
        this.revokeRequestsTab = page.locator(".kyraAccessRevokeBar button:has-text('Revoke'), button.kyraTabPillBtn:has-text('Revoke')").first();
        this.approvalAccessTable = page.locator("#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable, [id$='approvalAccessTable']").first();
        this.searchField = page.locator("#application-app-preview-component---AccessPage--approverSectionView .kyraApproverRightControls input, .kyraApproverRightControls input, input[placeholder*='Search request ID, role, or user']").first();
        this.filterBtn = page.locator("button.kyraApproverFilterBtn, button:has-text('Filter')").first();
        this.exportBtn = page.locator("button.kyraApproverExportBtn, button:has-text('Export')").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(complianceCard, ConfigReader.getNavigationTimeout());
            return isComplianceReviewPersonaDisplayed() && complianceCard.isVisible();
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

    public boolean isComplianceReviewPersonaDisplayed() {
        try {
            String role = headerComponent.getActivePersona();
            return "Compliance Review".equalsIgnoreCase(role);
        } catch (Exception e) {
            return false;
        }
    }

    public String getActiveUserId() {
        return headerComponent.getActiveUserId();
    }

    public String getComplianceSectionTitle() {
        WaitUtils.waitForElementVisible(complianceTitle);
        return complianceTitle.innerText().trim();
    }

    public boolean isComplianceTitleDisplayed() {
        try {
            String title = getComplianceSectionTitle();
            return "Compliance Review Requests".equalsIgnoreCase(title) || "Processed Approval History Log".equalsIgnoreCase(title);
        } catch (Exception e) {
            return false;
        }
    }

    public String getComplianceSubtitle() {
        WaitUtils.waitForElementVisible(complianceSubtitle);
        return complianceSubtitle.innerText().trim();
    }

    public boolean isPendingQueueBtnVisible() {
        return pendingQueueBtn.isVisible();
    }

    public boolean isHistoryLogBtnVisible() {
        return historyLogBtn.isVisible();
    }

    public void clickPendingQueue() {
        try {
            Locator emphasizedPending = page.locator(".kyraApproverPillGroup button.sapMBtnEmphasized:has-text('Pending')").first();
            if (emphasizedPending.isVisible()) {
                return;
            }
            WaitUtils.waitForElementVisible(pendingQueueBtn, ConfigReader.getDefaultTimeout());
            pendingQueueBtn.scrollIntoViewIfNeeded();
            pendingQueueBtn.click();
            WaitUtils.stabilize(page, 1000);
        } catch (Exception e) {
            Locator fallback = page.locator(".kyraApproverPillGroup button:has-text('Pending'), button.kyraApproverPillBtn:has-text('Pending'), button:has-text('Pending')").first();
            fallback.click();
            WaitUtils.stabilize(page, 1000);
        }
    }

    public void clickHistoryLog() {
        try {
            WaitUtils.waitForElementVisible(historyLogBtn, ConfigReader.getDefaultTimeout());
            historyLogBtn.click();
            WaitUtils.stabilize(page, 500);
        } catch (Exception ignored) {
        }
    }

    public boolean isAccessRequestsTabVisible() {
        return accessRequestsTab.isVisible();
    }

    public void clickAccessRequestsTab() {
        try {
            Locator emphasizedAccess = page.locator(".kyraAccessRevokeBar button.sapMBtnEmphasized:has-text('Access')").first();
            if (emphasizedAccess.isVisible()) {
                return;
            }
            if (accessRequestsTab.isVisible()) {
                accessRequestsTab.click();
                WaitUtils.stabilize(page, 500);
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isRevokeRequestsTabHidden() {
        return !revokeRequestsTab.isVisible();
    }

    public boolean isApprovalAccessTableVisible() {
        try {
            if (!approvalAccessTable.isVisible() && pendingQueueBtn.isVisible()) {
                clickPendingQueue();
            }
            WaitUtils.waitForElementVisible(approvalAccessTable, 5000);
            return approvalAccessTable.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFilterBtnVisible() {
        return filterBtn.isVisible();
    }

    public boolean isExportBtnVisible() {
        return exportBtn.isVisible();
    }

    public boolean hasPendingRequestForUser(String userId) {
        try {
            clickPendingQueue();
            clickAccessRequestsTab();
            WaitUtils.waitForElementVisible(approvalAccessTable, ConfigReader.getDefaultTimeout());
            Locator row = page.locator("#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable tbody tr:has(.kyraUserIdText:has-text('" + userId + "')), " +
                    "#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable tbody tr:has-text('" + userId + "'), " +
                    "tr.kyraApproverRowItem:has-text('" + userId + "'), " +
                    "tr:has(.kyraUserIdText:has-text('" + userId + "'))").first();
            return row.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public ApproverRequestDetailPage openRequestForUserId(String userId) {
        // 1. Ensure Pending Queue and Access Requests tab are active
        clickPendingQueue();
        clickAccessRequestsTab();

        // 2. Ensure table container is in view
        complianceCard.scrollIntoViewIfNeeded();
        WaitUtils.waitForElementVisible(approvalAccessTable, ConfigReader.getDefaultTimeout());
        WaitUtils.stabilize(page, 600);

        // 3. Clear search field if it has any text to prevent filtering out rows
        try {
            if (searchField.isVisible()) {
                String currentVal = searchField.inputValue();
                if (currentVal != null && !currentVal.trim().isEmpty()) {
                    searchField.clear();
                    WaitUtils.stabilize(page, 300);
                }
            }
        } catch (Exception ignored) {
        }

        // 4. Locate the row with the user ID in the approval table
        Locator row = page.locator("#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable tbody tr:has(.kyraUserIdText:has-text('" + userId + "')), " +
                "#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable tbody tr:has-text('" + userId + "'), " +
                "tr.kyraApproverRowItem:has-text('" + userId + "'), " +
                "tr:has(.kyraUserIdText:has-text('" + userId + "'))").first();

        try {
            WaitUtils.waitForElementVisible(row, 6000);
        } catch (Exception notImmediatelyVisible) {
            // Re-fetch submitted requests if table sync took a second after Approver submitted in Tab 2
            try {
                page.evaluate("() => {\n" +
                        "    const oView = window.sap && window.sap.ui && window.sap.ui.getCore && window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                        "    if (oView && oView.getController() && typeof oView.getController()._loadSubmittedRequests === 'function') {\n" +
                        "        oView.getController()._loadSubmittedRequests(oView.getModel('accessModel'), false, true);\n" +
                        "    }\n" +
                        "}");
                WaitUtils.stabilize(page, 1000);
                clickPendingQueue();
                clickAccessRequestsTab();
            } catch (Exception ignored) {}
            WaitUtils.waitForElementVisible(row, ConfigReader.getNavigationTimeout());
        }

        row.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        row.click();

        ApproverRequestDetailPage detailPage = new ApproverRequestDetailPage(page);
        WaitUtils.stabilize(page, 800);
        return detailPage;
    }

    public void signOut() {
        headerComponent.clickSignOut();
        if (logoutDialog.isDialogVisible()) {
            logoutDialog.confirmSignOut();
        }
        WaitUtils.stabilize(page, 1000);
    }
}
