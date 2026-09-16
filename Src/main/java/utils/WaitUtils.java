package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class WaitUtils {

    public static void waitForElementVisible(Locator locator, double timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutMs));
    }

    public static void waitForElementVisible(Locator locator) {
        waitForElementVisible(locator, ConfigReader.getDefaultTimeout());
    }

    public static void waitForElementHidden(Locator locator, double timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeoutMs));
    }

    public static void stabilize(Page page, long ms) {
        try {
            page.waitForTimeout(ms);
        } catch (Exception ignored) {
        }
    }

    public static void stabilize(Page page) {
        stabilize(page, 500);
    }

    public static void waitForNetworkIdleOrStabilized(Page page) {
        try {
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
        } catch (Exception ignored) {
        }
        stabilize(page, 400);
    }
}
