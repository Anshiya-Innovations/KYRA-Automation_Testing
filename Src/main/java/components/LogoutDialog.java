package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.WaitUtils;

public class LogoutDialog {

    private final Page page;
    private final Locator dialogContainer;
    private final Locator confirmBtn;
    private final Locator cancelBtn;
    private final Locator dialogTitle;

    public LogoutDialog(Page page) {
        this.page = page;
        this.dialogContainer = page.locator(".kyraSignOutModernDialog, .kyra-signout-card").first();
        this.confirmBtn = page.locator("#kyra_signout_confirm_btn").first();
        this.cancelBtn = page.locator("#kyra_signout_cancel_btn").first();
        this.dialogTitle = page.locator(".kyra-signout-main-title").first();
    }

    public boolean isDialogVisible() {
        try {
            WaitUtils.waitForElementVisible(confirmBtn, 8000);
            return confirmBtn.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmSignOut() {
        WaitUtils.waitForElementVisible(confirmBtn);
        confirmBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void cancelSignOut() {
        WaitUtils.waitForElementVisible(cancelBtn);
        cancelBtn.click();
        WaitUtils.stabilize(page, 300);
    }

    public String getDialogTitle() {
        WaitUtils.waitForElementVisible(dialogTitle);
        return dialogTitle.innerText().trim();
    }
}
