package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class DecisionBreakdownDialog {

    private final Page page;
    private final Locator dialogOverlay;
    private final Locator dialogCard;
    private final Locator dialogTitle;
    private final Locator confirmSubmitBtn;
    private final Locator backBtn;
    private final Locator closeBtn;

    public DecisionBreakdownDialog(Page page) {
        this.page = page;
        this.dialogOverlay = page.locator("#kyra_dialog_overlay").first();
        this.dialogCard = page.locator(".kyraDialogCard").first();
        this.dialogTitle = page.locator("#kyra_dialog_overlay h3").first();
        this.confirmSubmitBtn = page.locator("#kyra_dialog_confirm_btn, button:has-text('Confirm & Submit')").first();
        this.backBtn = page.locator("#kyra_dialog_cancel_btn, button:has-text('Back')").first();
        this.closeBtn = page.locator("#kyra_dialog_close_btn").first();
    }

    public boolean isDialogVisible() {
        try {
            WaitUtils.waitForElementVisible(dialogOverlay, ConfigReader.getDefaultTimeout());
            return dialogOverlay.isVisible() && dialogTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDialogTitle() {
        WaitUtils.waitForElementVisible(dialogTitle, 5000);
        return dialogTitle.innerText().trim();
    }

    public void clickConfirmAndSubmit() {
        WaitUtils.waitForElementVisible(confirmSubmitBtn, ConfigReader.getDefaultTimeout());
        confirmSubmitBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        confirmSubmitBtn.click();
        WaitUtils.stabilize(page, 600);
    }

    public void clickBack() {
        WaitUtils.waitForElementVisible(backBtn, ConfigReader.getDefaultTimeout());
        backBtn.click();
        WaitUtils.stabilize(page, 300);
    }

    public boolean waitForClosed() {
        try {
            dialogOverlay.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.DETACHED).setTimeout(15000));
            return true;
        } catch (Exception e) {
            return !dialogOverlay.isVisible();
        }
    }
}
