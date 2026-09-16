package tests.requester;

import base.BaseTest;
import components.UnsavedChangesDialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AccessConfigurationPage;
import pages.AccessValidationPage;
import pages.AddAccessPage;
import pages.LoginPage;
import pages.RegionSelectionPage;
import pages.RequesterPage;
import utils.ConfigReader;
import utils.ScreenshotUtils;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequesterAccessWizardTest extends BaseTest {

    @Test
    @DisplayName("Requester Add Access Wizard through Step 4 and Cancel Flow")
    public void testRequesterAddAccessWizard() {
        StepReporter reporter = new StepReporter("KYRA REQUESTER ACCESS WIZARD");
        String currentStep = "Initialization";
        String lastSelector = "N/A";
        final String persona = "Requester";
        final String employeeId = "emp010";

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        try {
            // LOGIN
            reporter.section("LOGIN");

            currentStep = "Login Page";
            setCurrentStep(currentStep);
            lastSelector = ConfigReader.getBaseUrl();
            LoginPage loginPage = new LoginPage(page);
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Login page failed to load");

            // 2. Requester Persona
            currentStep = "Requester Persona";
            setCurrentStep(currentStep);
            lastSelector = "#roleSelect-arrow, .role-selector";
            loginPage.selectPersona(persona);
            reporter.pass("Requester Persona");

            // 3. Employee ID
            currentStep = "Employee ID";
            setCurrentStep(currentStep);
            lastSelector = "#employeeIdInput-inner, input[placeholder*='Employee ID']";
            loginPage.enterEmployeeId(employeeId);
            reporter.pass("Employee ID");

            // 4. Sign In
            currentStep = "Sign In";
            setCurrentStep(currentStep);
            lastSelector = "#signInBtn, button:has-text('Sign In')";
            loginPage.clickSignIn();
            reporter.pass("Sign In");

            // 5. Requester Page
            currentStep = "Requester Page";
            lastSelector = "#application-app-preview-component---AccessPage--accessPortalPage";
            RequesterPage requesterPage = new RequesterPage(page);
            assertTrue(requesterPage.isLoaded(), "Requester Page failed to load");
            reporter.pass("Requester Page");

            // 6. Add Access
            currentStep = "Add Access";
            lastSelector = "#application-app-preview-component---AccessPage--cardAddAccess";
            requesterPage.clickAddAccess();
            AddAccessPage addAccessPage = new AddAccessPage(page);
            assertTrue(addAccessPage.isLoaded(), "Add Access Section container failed to open");
            reporter.pass("Add Access");

            // STEP 1
            reporter.section("STEP 1");

            currentStep = "Enterprise Scope";
            lastSelector = "[id$='addAccessSectionContainer'], .kyraEnterpriseScope";
            assertTrue(addAccessPage.isLoaded(), "Enterprise Scope Selection not loaded");
            reporter.pass("Enterprise Scope");

            currentStep = "Business Sector";
            lastSelector = "#application-app-preview-component---AccessPage--inPageBusinessSectorSelect";
            addAccessPage.selectBusinessSector("Finance & Enterprise Performance");
            assertEquals("Finance & Enterprise Performance", addAccessPage.getSelectedBusinessSector(),
                    "Business Sector selection mismatch");
            reporter.pass("Business Sector");

            currentStep = "Business Function";
            lastSelector = "#application-app-preview-component---AccessPage--inPageBusinessFunctionSelect";
            addAccessPage.selectBusinessFunction("Corporate Accounting");
            assertEquals("Corporate Accounting", addAccessPage.getSelectedBusinessFunction(),
                    "Business Function selection mismatch");
            reporter.pass("Business Function");

            currentStep = "Next";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            addAccessPage.clickNext();
            RegionSelectionPage regionPage = new RegionSelectionPage(page);
            assertTrue(regionPage.isRegionSelectionActive(), "Failed to navigate to Step 2 Region Selection");
            reporter.pass("Next");

            // STEP 2
            reporter.section("STEP 2");

            currentStep = "Region Selection";
            lastSelector = ".selected-regions-header, #mapWrapper";
            assertTrue(regionPage.isLoaded(), "Region Selection step elements not displayed");
            reporter.pass("Region Selection");

            currentStep = "Select All Regions";
            lastSelector = "#selectAllBtn, .select-all-btn-wrapper:has-text('Select All Regions')";
            regionPage.selectAllRegions();
            reporter.pass("Select All Regions");

            currentStep = "Selected Regions";
            lastSelector = "#selectedChipsList button.region-chip-btn";
            assertTrue(regionPage.hasSelectedRegions(), "Selected regions state did not update as expected");
            reporter.pass("Selected Regions");

            currentStep = "Next";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            regionPage.clickNext();
            AccessConfigurationPage configPage = new AccessConfigurationPage(page);
            assertTrue(configPage.isLoaded(), "Failed to navigate to Step 3 Access Configuration");
            reporter.pass("Next");

            // STEP 3
            reporter.section("STEP 3");

            currentStep = "Access Configuration";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Access Configuration Step 3 not active");
            reporter.pass("Access Configuration");

            currentStep = "Target System";
            lastSelector = "[id$='inPageSystemsMultiSelect']";
            configPage.selectTargetSystem("SAP BTP Cloud Platform");
            assertTrue(configPage.isTargetSystemSelected("SAP BTP Cloud Platform"),
                    "Target System selection failed to reflect");
            reporter.pass("Target System");

            currentStep = "Service / Topic";
            lastSelector = "[id$='inPageServicesMultiSelect']";
            configPage.selectServiceTopic("System Administrator");
            assertTrue(configPage.isServiceTopicSelected("System Administrator"),
                    "Service / Topic selection failed to reflect");
            reporter.pass("Service / Topic");

            // In the UI, Team Role is selected before Assigned Persona to enable the persona dropdown
            currentStep = "Team Role";
            lastSelector = "[id$='inPageTeamMultiSelect']";
            configPage.selectTeamRole("IT Developers (System Administrator)");
            assertTrue(configPage.isTeamRoleSelected("IT Developers (System Administrator)"),
                    "Team Role selection failed to reflect");

            currentStep = "Assigned Persona";
            lastSelector = "[id$='inPagePersonaMultiSelect']";
            configPage.selectAssignedPersona("Frontend & UI Developer Persona (IT Developers)");
            assertTrue(configPage.isAssignedPersonaSelected("Frontend & UI Developer Persona (IT Developers)"),
                    "Assigned Persona selection failed to reflect");

            // Report in requested order: Assigned Persona then Team Role
            reporter.pass("Assigned Persona");
            reporter.pass("Team Role");

            // Complete Sub-step 3.1: advance to Sub-step 3.2
            currentStep = "Advance to Duration & Justification";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            configPage.clickNextSlide();

            currentStep = "Access Duration";
            lastSelector = "[id$='inPageDurationSelect-inner']";
            configPage.selectDuration("30 Days (Temporary)");
            assertEquals("30 Days (Temporary)", configPage.getSelectedDuration(),
                    "Access Duration selection mismatch");
            reporter.pass("Access Duration");

            currentStep = "Business Justification";
            lastSelector = "[id$='inPageJustificationArea'] textarea";
            configPage.enterJustification("TEST");
            assertEquals("TEST", configPage.getJustificationText(),
                    "Business Justification text mismatch");
            reporter.pass("Business Justification");

            currentStep = "Next";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            configPage.clickNextToValidation();
            AccessValidationPage validationPage = new AccessValidationPage(page);
            assertTrue(validationPage.isLoaded(), "Failed to navigate to Step 4 Access Validation");
            reporter.pass("Next");

            // STEP 4
            reporter.section("STEP 4");

            currentStep = "Access Validation";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 4: Access Validation')";
            assertTrue(validationPage.isLoaded(), "Access Validation Step 4 not active");
            reporter.pass("Access Validation");

            currentStep = "Validation Sections";
            lastSelector = ".kyraValCardTitle:has-text('Threshold Limits')";
            assertTrue(validationPage.isValidationSectionsVisible(), "Validation sections not visible");
            reporter.pass("Validation Sections");

            currentStep = "Previous";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous')";
            validationPage.scrollToBottom();
            validationPage.clickPrevious();
            assertTrue(configPage.isStep3Active(), "Failed to return to Step 3 Access Configuration");
            reporter.pass("Previous");

            // RETURN TO STEP 3
            reporter.section("RETURN TO STEP 3");

            currentStep = "Access Configuration";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Access Configuration Step 3 not active after return");
            reporter.pass("Access Configuration");

            // CANCEL FLOW
            reporter.section("CANCEL FLOW");

            currentStep = "Cancel";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel')";
            configPage.scrollToBottom();
            configPage.clickCancel();
            UnsavedChangesDialog dialog = new UnsavedChangesDialog(page);
            assertTrue(dialog.isDialogVisible(), "Unsaved Changes Dialog did not appear after Cancel");
            reporter.pass("Cancel");

            currentStep = "Unsaved Changes Dialog";
            lastSelector = "#kyra_dialog_overlay h3";
            assertEquals("Unsaved Changes", dialog.getDialogTitle(), "Dialog title mismatch");
            reporter.pass("Unsaved Changes Dialog");

            currentStep = "Stay on Page";
            lastSelector = "#kyra_dialog_cancel_btn, button:has-text('Stay on Page')";
            dialog.clickStayOnPage();
            assertTrue(dialog.waitForClosed(), "Unsaved Changes dialog remained visible after Stay on Page");
            reporter.pass("Stay on Page");

            currentStep = "Still on Step 3";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Wizard did not remain on Step 3 Access Configuration");
            reporter.pass("Still on Step 3");

            // Final test summary
            reporter.printSummary();

        } catch (Throwable t) {
            reporter.fail(currentStep);
            ScreenshotUtils.captureStepFailure(
                    page,
                    "RequesterAccessWizard",
                    currentStep,
                    persona,
                    employeeId,
                    (page != null ? page.url() : "N/A"),
                    t.getMessage(),
                    lastSelector
            );
            reporter.printSummary();
            throw new AssertionError("Test failed at step [" + currentStep + "] using selector [" + lastSelector + "]: " + t.getMessage(), t);
        }
    }
}
