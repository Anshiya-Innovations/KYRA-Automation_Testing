package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class SubmissionDialog {

    private final Page page;
    private final Locator dialogOverlay;
    private final Locator dialogCard;
    private final Locator dialogTitle;
    private final Locator confirmBtn;
    private final Locator closeBtn;
    private final Locator submittedBadge;
    private final Locator submittedItemsContainer;

    public SubmissionDialog(Page page) {
        this.page = page;
        this.dialogOverlay = page.locator("#kyra_dialog_overlay").first();
        this.dialogCard = page.locator(".kyraDialogCard").first();
        this.dialogTitle = page.locator("#kyra_dialog_overlay h3").first();
        this.confirmBtn = page.locator("#kyra_dialog_confirm_btn, button:has-text('Done')").first();
        this.closeBtn = page.locator("#kyra_dialog_close_btn").first();
        this.submittedBadge = page.locator("#kyra_dialog_overlay span:has-text('Submitted')").first();
        this.submittedItemsContainer = page.locator(".kyra-dialog-scroll-container, #kyra_dialog_overlay div:has-text('Submitted Access Items')").first();
    }

    public boolean isDialogVisible() {
        try {
            WaitUtils.waitForElementVisible(dialogOverlay, 15000);
            return dialogOverlay.isVisible() && dialogTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDialogTitle() {
        WaitUtils.waitForElementVisible(dialogTitle, 5000);
        return dialogTitle.innerText().trim();
    }

    public boolean hasSubmittedItems() {
        try {
            WaitUtils.stabilize(page, 300);
            return submittedBadge.isVisible() || submittedItemsContainer.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDone() {
        WaitUtils.waitForElementVisible(confirmBtn, ConfigReader.getDefaultTimeout());
        confirmBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        confirmBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public boolean waitForClosed() {
        try {
            dialogOverlay.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.DETACHED).setTimeout(6000));
            return true;
        } catch (Exception e) {
            return !dialogOverlay.isVisible();
        }
    }
}
