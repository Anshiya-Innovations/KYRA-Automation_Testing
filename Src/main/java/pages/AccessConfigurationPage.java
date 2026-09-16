package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class AccessConfigurationPage extends BasePage {

    private final Locator step3HeaderTitle;
    private final Locator step3ProgressNode;
    private final Locator systemsSelectionTitle;
    private final Locator systemsMultiSelect;
    private final Locator previousBtn;
    private final Locator cancelBtn;
    private final Locator nextBtn;

    public AccessConfigurationPage(Page page) {
        super(page);
        this.step3HeaderTitle = page.locator(".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')").first();
        this.step3ProgressNode = page.locator(".kyraStepNode.kyraStepActive:has-text('3. Access Configuration'), .kyraStepNode:has-text('3. Access Configuration')").first();
        this.systemsSelectionTitle = page.locator(":text('Target Systems & Process Selection')").first();
        this.systemsMultiSelect = page.locator("[id$='inPageSystemsMultiSelect'], #application-app-preview-component---AccessPage--inPageSystemsMultiSelect").first();
        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible, button:has-text('Previous'):visible").first();
        this.cancelBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible, button:has-text('Cancel'):visible").first();
        this.nextBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible, button:has-text('Next'):visible").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(step3HeaderTitle, ConfigReader.getDefaultTimeout());
            return step3HeaderTitle.isVisible() || step3ProgressNode.isVisible() || systemsSelectionTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStep3Active() {
        try {
            WaitUtils.stabilize(page, 300);
            return (step3HeaderTitle.isVisible() || step3ProgressNode.isVisible()) &&
                    (systemsSelectionTitle.isVisible() || systemsMultiSelect.isVisible() || previousBtn.isVisible());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSystemsMultiSelectVisible() {
        try {
            return systemsMultiSelect.isVisible();
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
