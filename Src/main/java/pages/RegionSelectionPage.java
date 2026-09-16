package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class RegionSelectionPage extends BasePage {

    private final Locator mapContainer;
    private final Locator selectedRegionsHeader;
    private final Locator step2ProgressNode;
    private final Locator previousBtn;
    private final Locator selectAllRegionsBtn;

    public RegionSelectionPage(Page page) {
        super(page);
        this.mapContainer = page.locator("#mapWrapper, #worldMapImgAccessPage, .fioriMapCardOuter").first();
        this.selectedRegionsHeader = page.locator(".selected-regions-header, :text('SELECTED REGIONS')").first();
        this.step2ProgressNode = page.locator(".kyraStepNode:has-text('2. Region Selection')").first();
        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button:has-text('Previous'), button.kyraSecondaryBtn:has-text('Previous')").first();
        this.selectAllRegionsBtn = page.locator("#selectAllBtn, :text('Select All Regions')").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(selectedRegionsHeader, ConfigReader.getDefaultTimeout());
            return selectedRegionsHeader.isVisible() && (mapContainer.isVisible() || previousBtn.isVisible());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMapVisible() {
        return mapContainer.isVisible();
    }

    public void clickPrevious() {
        WaitUtils.waitForElementVisible(previousBtn, ConfigReader.getDefaultTimeout());
        previousBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        previousBtn.click();
        WaitUtils.stabilize(page, 500);
    }
}
