package pages;

import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public abstract class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public void waitForStabilization() {
        WaitUtils.stabilize(page, 500);
    }

    public abstract boolean isLoaded();
}
