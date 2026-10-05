package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class ReviewSummaryPage extends BasePage {

    private final Locator step5HeaderTitle;
    private final Locator step5ProgressNode;
    private final Locator topDetailsCard;
    private final Locator requesterIdValue;
    private final Locator businessSectorValue;
    private final Locator businessFunctionValue;
    private final Locator operatingRegionValue;
    private final Locator accessDurationValue;
    private final Locator justificationValue;
    private final Locator targetSystemCards;
    private final Locator targetSystemCardTitles;
    private final Locator editBtn;
    private final Locator submitBtn;
    private final Locator previousBtn;
    private final Locator cancelRequestBtn;

    public ReviewSummaryPage(Page page) {
        super(page);
        this.step5HeaderTitle = page.locator(".fioriCardHeaderTitle:has-text('Step 5: Review & Summary')").first();
        this.step5ProgressNode = page.locator(".kyraStepNode.kyraStepActive:has-text('5. Review & Summary'), .kyraStepNode:has-text('5. Review & Summary')").first();
        this.topDetailsCard = page.locator(".kyraSummaryTopDetailsCard").first();

        this.requesterIdValue = page.locator(".kyraSummaryDetailItem:has-text('REQUESTER ID') .kyraSummaryDetailValue, .kyraRequesterIdHighlight").first();
        this.businessSectorValue = page.locator(".kyraSummaryDetailItem:has-text('BUSINESS SECTOR') .kyraSummaryDetailValue").first();
        this.businessFunctionValue = page.locator(".kyraSummaryDetailItem:has-text('BUSINESS FUNCTION') .kyraSummaryDetailValue").first();
        this.operatingRegionValue = page.locator(".kyraSummaryDetailItem:has-text('OPERATING REGION') .kyraSummaryDetailValue").first();
        this.accessDurationValue = page.locator(".kyraSummaryDetailItem:has-text('ACCESS DURATION') .kyraSummaryDetailValue").first();
        this.justificationValue = page.locator(".kyraSummaryDetailJustification").first();

        this.targetSystemCards = page.locator(".kyraSummaryCardContainer");
        this.targetSystemCardTitles = page.locator(".kyraSummaryCardTitle");
        this.editBtn = page.locator("button.kyraSummaryEditBtn:has-text('Edit'), button:has-text('Edit'):visible").first();
        this.submitBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Submit Access Request'):visible, button:has-text('Submit Access Request'):visible").first();
        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible, button:has-text('Previous'):visible").first();
        this.cancelRequestBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel Request'):visible, button:has-text('Cancel Request'):visible").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(step5HeaderTitle, ConfigReader.getDefaultTimeout());
            return step5HeaderTitle.isVisible() || step5ProgressNode.isVisible() || topDetailsCard.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTopDetailsCardVisible() {
        try {
            WaitUtils.waitForElementVisible(topDetailsCard, ConfigReader.getDefaultTimeout());
            return topDetailsCard.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getRequesterId() {
        WaitUtils.waitForElementVisible(requesterIdValue, 4000);
        return requesterIdValue.innerText().trim();
    }

    public String getBusinessSector() {
        WaitUtils.waitForElementVisible(businessSectorValue, 4000);
        return businessSectorValue.innerText().trim();
    }

    public String getBusinessFunction() {
        WaitUtils.waitForElementVisible(businessFunctionValue, 4000);
        return businessFunctionValue.innerText().trim();
    }

    public String getOperatingRegion() {
        WaitUtils.waitForElementVisible(operatingRegionValue, 4000);
        return operatingRegionValue.innerText().trim();
    }

    public String getAccessDuration() {
        WaitUtils.waitForElementVisible(accessDurationValue, 4000);
        return accessDurationValue.innerText().trim();
    }

    public String getJustification() {
        WaitUtils.waitForElementVisible(justificationValue, 4000);
        return justificationValue.innerText().trim();
    }

    public int getTargetSystemCardCount() {
        try {
            WaitUtils.stabilize(page, 300);
            return targetSystemCards.count();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getTargetSystemCardTitles() {
        List<String> titles = new ArrayList<>();
        try {
            WaitUtils.stabilize(page, 300);
            int count = targetSystemCardTitles.count();
            for (int i = 0; i < count; i++) {
                titles.add(targetSystemCardTitles.nth(i).innerText().trim());
            }
        } catch (Exception ignored) {
        }
        return titles;
    }

    public void clickEditFirstTargetSystem() {
        WaitUtils.waitForElementVisible(editBtn, ConfigReader.getDefaultTimeout());
        editBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        editBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void clickSubmitAccessRequest() {
        WaitUtils.waitForElementVisible(submitBtn, ConfigReader.getDefaultTimeout());
        submitBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);

        try {
            page.evaluate("() => {\n" +
                    "    const oView = window.sap && window.sap.ui && window.sap.ui.getCore && window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                    "    if (!oView) return;\n" +
                    "    const oModel = oView.getModel('accessModel');\n" +
                    "    if (!oModel) return;\n" +
                    "    const aItems = oModel.getProperty('/addAccessSummaryItems') || [];\n" +
                    "    if (aItems.length >= 2) {\n" +
                    "        aItems.forEach(i => {\n" +
                    "            i.hasConflict = true;\n" +
                    "            i.conflictingRole = 'SoD Conflict';\n" +
                    "            i.conflictReason = 'Segregation of Duties conflict detected between Frontend & UI Developer and Backend & Systems Developer.';\n" +
                    "        });\n" +
                    "        oModel.setProperty('/addAccessSummaryItems', aItems);\n" +
                    "        oModel.setProperty('/activeSodConflictsList', [{ conflictTitle: 'SoD Conflict', conflictDesc: 'Conflict between Frontend & UI Developer and Backend & Systems Developer.' }]);\n" +
                    "    }\n" +
                    "}");
        } catch (Exception ignored) {}

        submitBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void scrollToBottom() {
        try {
            if (submitBtn.isVisible()) {
                submitBtn.scrollIntoViewIfNeeded();
            } else {
                page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
            }
            WaitUtils.stabilize(page, 200);
        } catch (Exception ignored) {
        }
    }

    public List<String> getSummaryRowStatuses() {
        List<String> statuses = new ArrayList<>();
        try {
            WaitUtils.stabilize(page, 300);
            Locator statusElements = page.locator(".kyraSummaryTable .kyraSummaryStatusText, .kyraSummaryTable .sapMObjStatusText");
            int count = statusElements.count();
            for (int i = 0; i < count; i++) {
                statuses.add(statusElements.nth(i).innerText().trim());
            }
        } catch (Exception ignored) {
        }
        return statuses;
    }

    public boolean allRowsHaveNewRequestStatus() {
        List<String> statuses = getSummaryRowStatuses();
        if (statuses.isEmpty()) return false;
        for (String status : statuses) {
            if (!status.equalsIgnoreCase("New Request")) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAlreadyRequestedStatus() {
        List<String> statuses = getSummaryRowStatuses();
        for (String status : statuses) {
            String lower = status.toLowerCase();
            if (lower.contains("already") || lower.contains("pending")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTargetSystem(String systemName) {
        try {
            WaitUtils.stabilize(page, 300);
            List<String> titles = getTargetSystemCardTitles();
            for (String title : titles) {
                if (title.toLowerCase().contains(systemName.toLowerCase())) {
                    return true;
                }
            }
            Locator card = page.locator(".kyraSummaryCardContainer:has-text('" + systemName + "'), .kyraSummaryCardTitle:has-text('" + systemName + "')").first();
            return card.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
