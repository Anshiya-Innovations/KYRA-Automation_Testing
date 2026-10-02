package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class BatchDecisionDialog {

    private final Page page;
    private final Locator dialog;
    private final Locator dialogTitle;
    private final Locator dialogSubtitle;
    private final Locator remarkTextArea;
    private final Locator approveAllConfirmBtn;
    private final Locator rejectAllConfirmBtn;
    private final Locator cancelBtn;
    private final Locator closeBtn;

    public BatchDecisionDialog(Page page) {
        this.page = page;
        this.dialog = page.locator(".kyraBatchDecisionDialog, .sapMDialog:has(.kyraBatchDialogTitle)").first();
        this.dialogTitle = page.locator(".kyraBatchDialogTitle").first();
        this.dialogSubtitle = page.locator(".kyraBatchDialogSubtitle").first();
        this.remarkTextArea = page.locator(".kyraBatchRemarkTextArea textarea, .kyraBatchDecisionDialog textarea").first();
        this.approveAllConfirmBtn = page.locator(".kyraBatchDialogConfirmApproveBtn, .kyraBatchDecisionDialog button:has-text('Approve All')").first();
        this.rejectAllConfirmBtn = page.locator(".kyraBatchDialogConfirmRejectBtn, .kyraBatchDecisionDialog button:has-text('Reject All')").first();
        this.cancelBtn = page.locator(".kyraBatchDialogCancelBtn, .kyraBatchDecisionDialog button:has-text('Cancel')").first();
        this.closeBtn = page.locator(".kyraBatchDialogCloseBtn").first();
    }

    public boolean isDialogVisible() {
        try {
            WaitUtils.waitForElementVisible(dialog, ConfigReader.getDefaultTimeout());
            return dialog.isVisible() && dialogTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDialogTitle() {
        WaitUtils.waitForElementVisible(dialogTitle, 5000);
        return dialogTitle.innerText().trim();
    }

    public void enterRemark(String remark) {
        WaitUtils.waitForElementVisible(remarkTextArea, ConfigReader.getDefaultTimeout());
        remarkTextArea.scrollIntoViewIfNeeded();
        remarkTextArea.fill(remark);
        WaitUtils.stabilize(page, 300);
    }

    public void clickApproveAll() {
        WaitUtils.waitForElementVisible(approveAllConfirmBtn, ConfigReader.getDefaultTimeout());
        approveAllConfirmBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        approveAllConfirmBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void clickRejectAll() {
        WaitUtils.waitForElementVisible(rejectAllConfirmBtn, ConfigReader.getDefaultTimeout());
        rejectAllConfirmBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        rejectAllConfirmBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void clickCancel() {
        WaitUtils.waitForElementVisible(cancelBtn, ConfigReader.getDefaultTimeout());
        cancelBtn.click();
        WaitUtils.stabilize(page, 300);
    }

    public boolean waitForClosed() {
        try {
            dialog.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.DETACHED).setTimeout(6000));
            return true;
        } catch (Exception e) {
            return !dialog.isVisible();
        }
    }
}
