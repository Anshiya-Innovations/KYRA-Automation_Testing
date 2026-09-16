package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

public class AddAccessPage extends BasePage {

    private final Locator addAccessContainer;
    private final Locator enterpriseScopeTitle;
    private final Locator step1ProgressNode;

    private final Locator businessSectorInput;
    private final Locator businessSectorArrow;
    private final Locator businessFunctionInput;
    private final Locator businessFunctionArrow;

    private final Locator step1NextBtn;
    private final Locator step1CancelBtn;

    public AddAccessPage(Page page) {
        super(page);
        this.addAccessContainer = page.locator("#application-app-preview-component---AccessPage--addAccessSectionContainer, [id$='addAccessSectionContainer']").first();
        this.enterpriseScopeTitle = page.locator(".kyraScopeTitle, :text('Enterprise Scope Selection')").first();
        this.step1ProgressNode = page.locator(".kyraStepNode:has-text('1. Business Sector')").first();

        this.businessSectorInput = page.locator("#application-app-preview-component---AccessPage--inPageBusinessSectorSelect-inner, [id$='inPageBusinessSectorSelect-inner']").first();
        this.businessSectorArrow = page.locator("#application-app-preview-component---AccessPage--inPageBusinessSectorSelect-arrow, [id$='inPageBusinessSectorSelect-arrow']").first();

        this.businessFunctionInput = page.locator("#application-app-preview-component---AccessPage--inPageBusinessFunctionSelect-inner, [id$='inPageBusinessFunctionSelect-inner']").first();
        this.businessFunctionArrow = page.locator("#application-app-preview-component---AccessPage--inPageBusinessFunctionSelect-arrow, [id$='inPageBusinessFunctionSelect-arrow']").first();

        this.step1NextBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'), [id$='addAccessSectionContainer'] button:has-text('Next')").first();
        this.step1CancelBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'), [id$='addAccessSectionContainer'] button:has-text('Cancel')").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(addAccessContainer, ConfigReader.getDefaultTimeout());
            WaitUtils.waitForElementVisible(enterpriseScopeTitle, ConfigReader.getDefaultTimeout());
            return addAccessContainer.isVisible() && enterpriseScopeTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnterpriseScopeVisible() {
        try {
            WaitUtils.waitForElementVisible(enterpriseScopeTitle, ConfigReader.getDefaultTimeout());
            return enterpriseScopeTitle.isVisible() && businessSectorInput.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectBusinessSector(String sectorName) {
        WaitUtils.waitForElementVisible(businessSectorInput, ConfigReader.getDefaultTimeout());
        businessSectorInput.scrollIntoViewIfNeeded();

        // Click arrow or input to open dropdown
        if (businessSectorArrow.isVisible()) {
            businessSectorArrow.click();
        } else {
            businessSectorInput.click();
        }
        WaitUtils.stabilize(page, 300);

        // Find visible option matching sectorName
        Locator option = page.locator(".sapMPopover:visible li[role='option'], .sapMComboBoxBaseItem:visible, li[role='option']:visible")
                .filter(new Locator.FilterOptions().setHasText(sectorName))
                .first();

        WaitUtils.waitForElementVisible(option, 5000);
        option.click();
        WaitUtils.stabilize(page, 500);
    }

    public String getSelectedBusinessSector() {
        WaitUtils.waitForElementVisible(businessSectorInput, 4000);
        return businessSectorInput.inputValue().trim();
    }

    public void selectBusinessFunction(String functionName) {
        WaitUtils.waitForElementVisible(businessFunctionInput, ConfigReader.getDefaultTimeout());
        businessFunctionInput.scrollIntoViewIfNeeded();

        // Click arrow or input to open dropdown
        if (businessFunctionArrow.isVisible()) {
            businessFunctionArrow.click();
        } else {
            businessFunctionInput.click();
        }
        WaitUtils.stabilize(page, 300);

        // Find visible option matching functionName
        Locator option = page.locator(".sapMPopover:visible li[role='option'], .sapMComboBoxBaseItem:visible, li[role='option']:visible")
                .filter(new Locator.FilterOptions().setHasText(functionName))
                .first();

        WaitUtils.waitForElementVisible(option, 5000);
        option.click();
        WaitUtils.stabilize(page, 500);
    }

    public String getSelectedBusinessFunction() {
        WaitUtils.waitForElementVisible(businessFunctionInput, 4000);
        return businessFunctionInput.inputValue().trim();
    }

    public void clickNext() {
        WaitUtils.waitForElementVisible(step1NextBtn, ConfigReader.getDefaultTimeout());
        step1NextBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        step1NextBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void scrollToBottom() {
        step1CancelBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
    }

    public void clickCancel() {
        scrollToBottom();
        WaitUtils.waitForElementVisible(step1CancelBtn, ConfigReader.getDefaultTimeout());
        step1CancelBtn.click();
        WaitUtils.stabilize(page, 500);
    }
}
