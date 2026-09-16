package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.MultiSelectComponent;
import utils.ConfigReader;
import utils.WaitUtils;

public class AccessConfigurationPage extends BasePage {

    private final Locator step3HeaderTitle;
    private final Locator step3ProgressNode;
    private final Locator systemsSelectionTitle;
    private final MultiSelectComponent systemsSelect;
    private final MultiSelectComponent servicesSelect;
    private final MultiSelectComponent teamSelect;
    private final MultiSelectComponent personaSelect;
    private final Locator durationSelectInput;
    private final Locator durationSelectArrow;
    private final Locator justificationArea;
    private final Locator durationSectionTitle;
    private final Locator previousBtn;
    private final Locator cancelBtn;
    private final Locator nextBtn;

    public AccessConfigurationPage(Page page) {
        super(page);
        this.step3HeaderTitle = page.locator(".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')").first();
        this.step3ProgressNode = page.locator(".kyraStepNode.kyraStepActive:has-text('3. Access Configuration'), .kyraStepNode:has-text('3. Access Configuration')").first();
        this.systemsSelectionTitle = page.locator(":text('Target Systems & Process Selection')").first();
        this.durationSectionTitle = page.locator(":text('Access Duration & Business Justification')").first();

        this.systemsSelect = new MultiSelectComponent(page, "[id$='inPageSystemsMultiSelect'], #application-app-preview-component---AccessPage--inPageSystemsMultiSelect");
        this.servicesSelect = new MultiSelectComponent(page, "[id$='inPageServicesMultiSelect'], #application-app-preview-component---AccessPage--inPageServicesMultiSelect");
        this.teamSelect = new MultiSelectComponent(page, "[id$='inPageTeamMultiSelect'], #application-app-preview-component---AccessPage--inPageTeamMultiSelect");
        this.personaSelect = new MultiSelectComponent(page, "[id$='inPagePersonaMultiSelect'], #application-app-preview-component---AccessPage--inPagePersonaMultiSelect");

        this.durationSelectInput = page.locator("[id$='inPageDurationSelect-inner'], #application-app-preview-component---AccessPage--inPageDurationSelect-inner").first();
        this.durationSelectArrow = page.locator("[id$='inPageDurationSelect-arrow'], #application-app-preview-component---AccessPage--inPageDurationSelect-arrow").first();
        this.justificationArea = page.locator("[id$='inPageJustificationArea'] textarea, textarea[id$='inPageJustificationArea-inner'], #application-app-preview-component---AccessPage--inPageJustificationArea-inner").first();

        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible, button:has-text('Previous'):visible").first();
        this.cancelBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible, button:has-text('Cancel'):visible").first();
        this.nextBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible, button:has-text('Next'):visible").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(step3HeaderTitle, ConfigReader.getDefaultTimeout());
            return step3HeaderTitle.isVisible() || step3ProgressNode.isVisible() || systemsSelectionTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStep3Active() {
        try {
            WaitUtils.stabilize(page, 300);
            return (step3HeaderTitle.isVisible() || step3ProgressNode.isVisible()) &&
                    (systemsSelectionTitle.isVisible() || durationSectionTitle.isVisible() || previousBtn.isVisible());
        } catch (Exception e) {
            return false;
        }
    }

    public void selectTargetSystem(String systemName) {
        systemsSelect.selectOption(systemName);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isTargetSystemSelected(String systemName) {
        return systemsSelect.isOptionSelected(systemName);
    }

    public void selectServiceTopic(String serviceTopic) {
        WaitUtils.waitForElementVisible(servicesSelect.getContainer(), ConfigReader.getDefaultTimeout());
        servicesSelect.selectOption(serviceTopic);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isServiceTopicSelected(String serviceTopic) {
        return servicesSelect.isOptionSelected(serviceTopic);
    }

    public void selectTeamRole(String teamRole) {
        WaitUtils.waitForElementVisible(teamSelect.getContainer(), ConfigReader.getDefaultTimeout());
        teamSelect.selectOption(teamRole);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isTeamRoleSelected(String teamRole) {
        return teamSelect.isOptionSelected(teamRole);
    }

    public void selectAssignedPersona(String persona) {
        WaitUtils.waitForElementVisible(personaSelect.getContainer(), ConfigReader.getDefaultTimeout());
        personaSelect.selectOption(persona);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isAssignedPersonaSelected(String persona) {
        return personaSelect.isOptionSelected(persona);
    }

    public void clickNextSlide() {
        WaitUtils.waitForElementVisible(nextBtn, ConfigReader.getDefaultTimeout());
        nextBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        nextBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void selectDuration(String duration) {
        WaitUtils.waitForElementVisible(durationSelectInput, ConfigReader.getDefaultTimeout());
        durationSelectInput.scrollIntoViewIfNeeded();

        if (durationSelectArrow.isVisible()) {
            durationSelectArrow.click();
        } else {
            durationSelectInput.click();
        }
        WaitUtils.stabilize(page, 300);

        Locator option = page.locator(".sapMPopover:visible li:has-text('" + duration + "'), " +
                ".sapMComboBoxBasePicker:visible li:has-text('" + duration + "'), " +
                ".sapMSelectListItemBase:has-text('" + duration + "'):visible, " +
                "li[role='option']:has-text('" + duration + "'):visible, " +
                "li:has-text('" + duration + "'):visible").first();

        WaitUtils.waitForElementVisible(option, 5000);
        option.scrollIntoViewIfNeeded();
        option.click();
        WaitUtils.stabilize(page, 400);
    }

    public String getSelectedDuration() {
        WaitUtils.waitForElementVisible(durationSelectInput, 4000);
        return durationSelectInput.inputValue().trim();
    }

    public void enterJustification(String justification) {
        WaitUtils.waitForElementVisible(justificationArea, ConfigReader.getDefaultTimeout());
        justificationArea.scrollIntoViewIfNeeded();
        justificationArea.fill(justification);
        WaitUtils.stabilize(page, 300);
    }

    public String getJustificationText() {
        WaitUtils.waitForElementVisible(justificationArea, 4000);
        return justificationArea.inputValue().trim();
    }

    public void clickNextToValidation() {
        WaitUtils.waitForElementVisible(nextBtn, ConfigReader.getDefaultTimeout());
        nextBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        nextBtn.click();
        WaitUtils.stabilize(page, 600);
    }

    public void scrollToBottom() {
        try {
            if (cancelBtn.isVisible()) {
                cancelBtn.scrollIntoViewIfNeeded();
            } else if (previousBtn.isVisible()) {
                previousBtn.scrollIntoViewIfNeeded();
            } else {
                page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
            }
            WaitUtils.stabilize(page, 300);
        } catch (Exception ignored) {
        }
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
}
