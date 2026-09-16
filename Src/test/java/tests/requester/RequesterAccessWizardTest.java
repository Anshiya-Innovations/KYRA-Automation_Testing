package tests.requester;

import base.BaseTest;
import components.UnsavedChangesDialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AccessConfigurationPage;
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
    @DisplayName("Requester Add Access Wizard through Step 3 and Return Flow")
    public void testRequesterAddAccessWizardThroughStep3() {
        StepReporter reporter = new StepReporter("KYRA REQUESTER ACCESS WIZARD");
        String currentStep = "Initialization";
        String lastSelector = "N/A";
        final String persona = "Requester";
        final String employeeId = "emp010";

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        try {
            // 1. Login Page
            currentStep = "Login Page";
            setCurrentStep(currentStep);
            lastSelector = ConfigReader.getBaseUrl();
            LoginPage loginPage = new LoginPage(page);
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Login page failed to load");
            reporter.pass("Login Page");

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

            // 5. Requester Access Page
            currentStep = "Requester Access Page";
            lastSelector = "#application-app-preview-component---AccessPage--accessPortalPage";
            RequesterPage requesterPage = new RequesterPage(page);
            assertTrue(requesterPage.isLoaded(), "Requester Access Page failed to load");
            reporter.pass("Requester Access Page");

            // 6. Add Access
            currentStep = "Add Access";
            lastSelector = "#application-app-preview-component---AccessPage--cardAddAccess";
            requesterPage.clickAddAccess();
            AddAccessPage addAccessPage = new AddAccessPage(page);
            assertTrue(addAccessPage.isLoaded(), "Add Access Section container failed to open");
            reporter.pass("Add Access");

            // STEP 1 - ENTERPRISE SCOPE
            reporter.section("STEP 1 - ENTERPRISE SCOPE");

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

            currentStep = "Step 1 Next";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            addAccessPage.clickNext();
            RegionSelectionPage regionPage = new RegionSelectionPage(page);
            assertTrue(regionPage.isRegionSelectionActive(), "Failed to navigate to Step 2 Region Selection");
            reporter.pass("Step 1 Next");

            // STEP 2 - REGION SELECTION
            reporter.section("STEP 2 - REGION SELECTION");

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

            currentStep = "Step 2 Next";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            regionPage.clickNext();
            AccessConfigurationPage configPage = new AccessConfigurationPage(page);
            assertTrue(configPage.isLoaded(), "Failed to navigate to Step 3 Access Configuration");
            reporter.pass("Step 2 Next");

            // STEP 3 - ACCESS CONFIGURATION
            reporter.section("STEP 3 - ACCESS CONFIGURATION");

            currentStep = "Access Configuration";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Access Configuration Step 3 not active");
            reporter.pass("Access Configuration");

            currentStep = "Previous";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous')";
            configPage.scrollToBottom();
            configPage.clickPrevious();
            reporter.pass("Previous");

            // STEP 2 - RETURN
            reporter.section("STEP 2 - RETURN");

            currentStep = "Region Selection Returned";
            lastSelector = ".selected-regions-header, #mapWrapper";
            assertTrue(regionPage.isRegionSelectionActive(), "Failed to return to Step 2 Region Selection");
            reporter.pass("Region Selection Returned");

            currentStep = "Cancel";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel')";
            regionPage.scrollToBottom();
            regionPage.clickCancel();
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

            currentStep = "Still on Region Selection";
            lastSelector = ".selected-regions-header, #mapWrapper";
            assertTrue(regionPage.isRegionSelectionActive(), "Wizard did not remain on Step 2 Region Selection");
            reporter.pass("Still on Region Selection");

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
