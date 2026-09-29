package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class RegionSelectionPage extends BasePage {

    private final Locator mapContainer;
    private final Locator selectedRegionsHeader;
    private final Locator step2ProgressNode;
    private final Locator step2HeaderTitle;
    private final Locator previousBtn;
    private final Locator selectAllRegionsBtn;
    private final Locator selectedChips;
    private final Locator noRegionsSelectedText;
    private final Locator nextBtn;
    private final Locator cancelBtn;

    public RegionSelectionPage(Page page) {
        super(page);
        this.mapContainer = page.locator("#mapWrapper, #worldMapImgAccessPage, .fioriMapCardOuter").first();
        this.selectedRegionsHeader = page.locator(".selected-regions-header, :text('SELECTED REGIONS')").first();
        this.step2ProgressNode = page.locator(".kyraStepNode.kyraStepActive:has-text('2. Region Selection'), .kyraStepNode:has-text('2. Region Selection')").first();
        this.step2HeaderTitle = page.locator(".fioriCardHeaderTitle:has-text('Step 2: Region Selection')").first();
        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible, button:has-text('Previous'):visible").first();
        this.selectAllRegionsBtn = page.locator("#selectAllBtn, .select-all-btn-wrapper:has-text('Select All Regions')").first();
        this.selectedChips = page.locator(".selected-chips-list button, button.region-chip-btn, [id$='selectedChipsList'] button");
        this.noRegionsSelectedText = page.locator(".no-selection-text, [id$='addAccessSectionContainer'] :text('No regions selected')").first();
        this.nextBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible, button:has-text('Next'):visible").first();
        this.cancelBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible, button:has-text('Cancel'):visible").first();
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

    public boolean isRegionSelectionActive() {
        try {
            WaitUtils.waitForElementVisible(selectedRegionsHeader, ConfigReader.getDefaultTimeout());
            return selectedRegionsHeader.isVisible() &&
                    (step2HeaderTitle.isVisible() || step2ProgressNode.isVisible() || mapContainer.isVisible());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMapVisible() {
        return mapContainer.isVisible();
    }

    public boolean isNoRegionsSelectedVisible() {
        try {
            return noRegionsSelectedText.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectAllRegions() {
        WaitUtils.waitForElementVisible(selectAllRegionsBtn, ConfigReader.getDefaultTimeout());
        selectAllRegionsBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        selectAllRegionsBtn.click();
        WaitUtils.stabilize(page, 500);

        try {
            page.waitForSelector("#selectAllBtn.active, .select-all-btn-wrapper.active, .selected-chips-list button, button.region-chip-btn",
                    new Page.WaitForSelectorOptions().setTimeout(ConfigReader.getDefaultTimeout()));
        } catch (Exception ignored) {
        }
    }

    public void selectRegion(String regionName) {
        WaitUtils.waitForElementVisible(mapContainer, ConfigReader.getDefaultTimeout());
        WaitUtils.stabilize(page, 300);
        try {
            Locator pin = page.locator(".map-pin-container:has-text('" + regionName + "')").first();
            if (pin.isVisible()) {
                pin.scrollIntoViewIfNeeded();
                pin.click();
            } else {
                page.evaluate("(name) => {\n" +
                        "    const pins = document.querySelectorAll('.map-pin-container');\n" +
                        "    for (const p of pins) {\n" +
                        "        if (p.innerText && p.innerText.includes(name)) {\n" +
                        "            p.click();\n" +
                        "            break;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}", regionName);
            }
        } catch (Exception e) {
            page.evaluate("(name) => {\n" +
                    "    const pins = document.querySelectorAll('.map-pin-container');\n" +
                    "    for (const p of pins) {\n" +
                    "        if (p.innerText && p.innerText.includes(name)) {\n" +
                    "            p.click();\n" +
                    "            break;\n" +
                    "        }\n" +
                    "    }\n" +
                    "}", regionName);
        }
        WaitUtils.stabilize(page, 400);
    }

    public boolean isRegionSelected(String regionName) {
        try {
            WaitUtils.stabilize(page, 200);
            Locator chip = page.locator(".selected-chips-list button:has-text('" + regionName + "'), button.region-chip-btn:has-text('" + regionName + "')").first();
            if (chip.isVisible()) return true;
            Locator pin = page.locator(".map-pin-container.active:has-text('" + regionName + "')").first();
            if (pin.isVisible()) return true;
            Object res = page.evaluate("(name) => {\n" +
                    "    try {\n" +
                    "        const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                    "        if (!oView) return false;\n" +
                    "        const c = oView.getController();\n" +
                    "        return (c && c._aSelectedRegionIds && c._aSelectedRegionIds.some(id => id.toLowerCase().includes(name.toLowerCase())));\n" +
                    "    } catch(e) { return false; }\n" +
                    "}", regionName);
            return Boolean.TRUE.equals(res);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasSelectedRegions() {
        try {
            WaitUtils.stabilize(page, 300);
            int count = selectedChips.count();
            boolean noRegionHidden = !noRegionsSelectedText.isVisible();
            return count > 0 && noRegionHidden;
        } catch (Exception e) {
            return false;
        }
    }

    public int getSelectedRegionsCount() {
        try {
            return selectedChips.count();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickNext() {
        WaitUtils.waitForElementVisible(nextBtn, ConfigReader.getDefaultTimeout());
        nextBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        nextBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void clickPrevious() {
        WaitUtils.waitForElementVisible(previousBtn, ConfigReader.getDefaultTimeout());
        previousBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        previousBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void clickCancel() {
        WaitUtils.waitForElementVisible(cancelBtn, ConfigReader.getDefaultTimeout());
        cancelBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        cancelBtn.click();
        WaitUtils.stabilize(page, 400);
    }

    public void scrollToBottom() {
        if (cancelBtn.isVisible()) {
            cancelBtn.scrollIntoViewIfNeeded();
        } else if (previousBtn.isVisible()) {
            previousBtn.scrollIntoViewIfNeeded();
        }
        WaitUtils.stabilize(page, 200);
    }
}
