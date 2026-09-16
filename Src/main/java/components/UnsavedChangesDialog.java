package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.WaitUtils;

public class UnsavedChangesDialog {

    private final Page page;
    private final Locator dialogOverlay;
    private final Locator dialogCard;
    private final Locator dialogTitle;
    private final Locator dialogMessage;
    private final Locator stayOnPageBtn;
    private final Locator proceedDiscardBtn;

    public UnsavedChangesDialog(Page page) {
        this.page = page;
        this.dialogOverlay = page.locator("#kyra_dialog_overlay").first();
        this.dialogCard = page.locator(".kyraDialogCard, #kyra_dialog_overlay > div").first();
        this.dialogTitle = page.locator("#kyra_dialog_overlay h3, .kyraDialogCard h3, :text('Unsaved Changes')").first();
        this.dialogMessage = page.locator("#kyra_dialog_overlay div:has-text('in-progress access request'), .kyraDialogCard div:has-text('in-progress access request')").first();
        this.stayOnPageBtn = page.locator("#kyra_dialog_cancel_btn, button:has-text('Stay on Page')").first();
        this.proceedDiscardBtn = page.locator("#kyra_dialog_confirm_btn, button:has-text('Proceed & Discard')").first();
    }

    public boolean isDialogVisible() {
        try {
            WaitUtils.waitForElementVisible(stayOnPageBtn, 8000);
            return stayOnPageBtn.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDialogTitle() {
        try {
            WaitUtils.waitForElementVisible(dialogTitle, 4000);
            return dialogTitle.innerText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDialogMessage() {
        try {
            WaitUtils.waitForElementVisible(dialogMessage, 4000);
            return dialogMessage.innerText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickStayOnPage() {
        WaitUtils.waitForElementVisible(stayOnPageBtn, 5000);
        stayOnPageBtn.click();
        WaitUtils.stabilize(page, 400);
    }

    public void clickProceedAndDiscard() {
        WaitUtils.waitForElementVisible(proceedDiscardBtn, 5000);
        proceedDiscardBtn.click();
        WaitUtils.stabilize(page, 400);
    }

    public boolean waitForClosed() {
        try {
            dialogOverlay.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(5000));
            return true;
        } catch (Exception e) {
            return !dialogOverlay.isVisible();
        }
    }
}
