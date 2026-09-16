package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class AccessValidationPage extends BasePage {

    private final Locator step4HeaderTitle;
    private final Locator step4ProgressNode;
    private final Locator thresholdLimitsTitle;
    private final Locator restrictedRecordsTitle;
    private final Locator duplicateRolesTitle;
    private final Locator thresholdLimitsBadge;
    private final Locator restrictedRecordsBadge;
    private final Locator duplicateRolesBadge;
    private final Locator previousBtn;
    private final Locator cancelBtn;
    private final Locator nextBtn;

    public AccessValidationPage(Page page) {
        super(page);
        this.step4HeaderTitle = page.locator(".fioriCardHeaderTitle:has-text('Step 4: Access Validation')").first();
        this.step4ProgressNode = page.locator(".kyraStepNode.kyraStepActive:has-text('4. Access Validation'), .kyraStepNode:has-text('4. Access Validation')").first();

        this.thresholdLimitsTitle = page.locator(".kyraValCardTitle:has-text('Threshold Limits')").first();
        this.restrictedRecordsTitle = page.locator(".kyraValCardTitle:has-text('Restricted Records')").first();
        this.duplicateRolesTitle = page.locator(".kyraValCardTitle:has-text('Duplicate Roles')").first();

        this.thresholdLimitsBadge = page.locator(".kyraPillBadgeSuccessText:has-text('0 Issues'), .kyraValEmptyTitle:has-text('No Threshold Limits Exceeded')").first();
        this.restrictedRecordsBadge = page.locator(".kyraPillBadgeWarningText:has-text('Records'), #restrictedRecordsTable").first();
        this.duplicateRolesBadge = page.locator(".kyraPillBadgeSuccessText:has-text('0 Duplicates'), .kyraValEmptyTitle:has-text('No Duplicate Roles Detected')").first();

        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible, button:has-text('Previous'):visible").first();
        this.cancelBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible, button:has-text('Cancel'):visible").first();
        this.nextBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible, button:has-text('Next'):visible").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(step4HeaderTitle, ConfigReader.getDefaultTimeout());
            return step4HeaderTitle.isVisible() || step4ProgressNode.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationSectionsVisible() {
        try {
            WaitUtils.stabilize(page, 300);
            boolean tVis = thresholdLimitsTitle.isVisible();
            boolean rVis = restrictedRecordsTitle.isVisible();
            boolean dVis = duplicateRolesTitle.isVisible();
            return tVis && rVis && dVis;
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollToBottom() {
        try {
            if (previousBtn.isVisible()) {
                previousBtn.scrollIntoViewIfNeeded();
            } else if (cancelBtn.isVisible()) {
                cancelBtn.scrollIntoViewIfNeeded();
            } else {
                page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
            }
            WaitUtils.stabilize(page, 300);
        } catch (Exception ignored) {
        }
    }

    public void clickPrevious() {
        WaitUtils.waitForElementVisible(previousBtn, ConfigReader.getDefaultTimeout());
        previousBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        previousBtn.click();
        WaitUtils.stabilize(page, 500);
    }
}
