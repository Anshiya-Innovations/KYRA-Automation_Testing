package tests.requester;

import base.BaseTest;
import components.HeaderComponent;
import components.LogoutDialog;
import components.SubmissionDialog;
import components.UnsavedChangesDialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.*;
import utils.ConfigReader;
import utils.ScreenshotUtils;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequesterEndToEndAccessTest extends BaseTest {

    @Test
    @DisplayName("Requester End-to-End Access Request Wizard Flow")
    public void testRequesterEndToEndAccessFlow() {
        StepReporter reporter = new StepReporter("KYRA REQUESTER END-TO-END");
        String currentStep = "Initialization";
        String lastSelector = "N/A";
        final String persona = ConfigReader.getRequesterPersona();
        final String employeeId = ConfigReader.getRequesterId();

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

            currentStep = "Requester Persona";
            setCurrentStep(currentStep);
            lastSelector = "#roleSelect-arrow, .role-selector";
            loginPage.selectPersona(persona);
            reporter.pass("Requester Persona");

            currentStep = "Employee ID";
            setCurrentStep(currentStep);
            lastSelector = "#employeeIdInput-inner, input[placeholder*='Employee ID']";
            loginPage.enterEmployeeId(employeeId);
            reporter.pass("Employee ID");

            currentStep = "Sign In";
            setCurrentStep(currentStep);
            lastSelector = "#signInBtn, button:has-text('Sign In')";
            loginPage.clickSignIn();
            reporter.pass("Sign In");

            currentStep = "Requester Page";
            lastSelector = "#application-app-preview-component---AccessPage--accessPortalPage";
            RequesterPage requesterPage = new RequesterPage(page);
            assertTrue(requesterPage.isLoaded(), "Requester Page failed to load");
            reporter.pass("Requester Page");

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

            // Advance to Slide 2 (Duration & Justification)
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

            // CANCEL TEST
            reporter.section("CANCEL TEST");

            currentStep = "Back to Step 3";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Wizard not on Step 3 Access Configuration");
            reporter.pass("Back to Step 3");

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

            currentStep = "Still Step 3";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Wizard did not remain on Step 3 Access Configuration");
            reporter.pass("Still Step 3");

            // STEP 4 REVISIT
            reporter.section("STEP 4 REVISIT");

            currentStep = "Access Validation";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            configPage.clickNextToValidation();
            assertTrue(validationPage.isLoaded(), "Failed to navigate back to Step 4 Access Validation");
            reporter.pass("Access Validation");

            currentStep = "Next";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')";
            validationPage.advanceToStep5();
            ReviewSummaryPage reviewPage = new ReviewSummaryPage(page);
            assertTrue(reviewPage.isLoaded(), "Failed to navigate to Step 5 Review & Summary");
            reporter.pass("Next");

            // STEP 5
            reporter.section("STEP 5");

            currentStep = "Review & Summary";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 5: Review & Summary')";
            assertTrue(reviewPage.isLoaded(), "Step 5 Review & Summary not active");
            reporter.pass("Review & Summary");

            currentStep = "Summary Verification";
            lastSelector = ".kyraSummaryTopDetailsCard";
            assertTrue(reviewPage.isTopDetailsCardVisible(), "Summary top details card not visible");
            assertEquals(employeeId, reviewPage.getRequesterId(), "Requester ID mismatch in summary");
            assertEquals("Finance & Enterprise Performance", reviewPage.getBusinessSector(), "Business Sector mismatch in summary");
            assertEquals("Corporate Accounting", reviewPage.getBusinessFunction(), "Business Function mismatch in summary");
            assertEquals("30 Days (Temporary)", reviewPage.getAccessDuration(), "Access Duration mismatch in summary");
            assertEquals("TEST", reviewPage.getJustification(), "Business Justification mismatch in summary");
            assertTrue(reviewPage.getTargetSystemCardCount() >= 1, "No target system cards found in summary");
            reporter.pass("Summary Verification");

            currentStep = "Edit";
            lastSelector = "button.kyraSummaryEditBtn:has-text('Edit')";
            reviewPage.clickEditFirstTargetSystem();
            assertTrue(configPage.isStep3Active(), "Failed to return to Step 3 for editing");
            assertTrue(configPage.isOkButtonVisible(), "OK button not visible in edit mode");
            reporter.pass("Edit");

            currentStep = "Additional System";
            lastSelector = "[id$='inPageTeamMultiSelect'], [id$='inPagePersonaMultiSelect']";
            configPage.configureEditEntitlementsForNewRequestOnly();
            reporter.pass("Additional System");

            currentStep = "OK";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('OK')";
            configPage.clickOk();
            assertTrue(reviewPage.isLoaded(), "Failed to return to Step 5 Review & Summary after clicking OK");
            reporter.pass("OK");

            currentStep = "Updated Summary";
            lastSelector = ".kyraSummaryTable .kyraSummaryStatusText";
            assertTrue(reviewPage.getTargetSystemCardCount() >= 1, "Updated Summary does not show system card");
            assertTrue(reviewPage.allRowsHaveNewRequestStatus(), "Expected all configured items to have 'New Request' status");
            assertFalse(reviewPage.hasAlreadyRequestedStatus(), "Found 'Already in Pending' items in updated summary");
            reporter.pass("Updated Summary");

            currentStep = "Submit Access Request";
            lastSelector = "[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Submit Access Request')";
            reviewPage.scrollToBottom();
            reviewPage.clickSubmitAccessRequest();
            SubmissionDialog submissionDialog = new SubmissionDialog(page);
            assertTrue(submissionDialog.isDialogVisible(), "Access Request Submitted dialog failed to appear");
            reporter.pass("Submit Access Request");

            // SUBMISSION
            reporter.section("SUBMISSION");

            currentStep = "Access Request Submitted";
            lastSelector = "#kyra_dialog_overlay h3";
            assertEquals("Access Request Submitted", submissionDialog.getDialogTitle(), "Submission dialog title mismatch");
            reporter.pass("Access Request Submitted");

            currentStep = "Submitted Items";
            lastSelector = "#kyra_dialog_overlay .kyra-dialog-scroll-container";
            assertTrue(submissionDialog.hasSubmittedItems(), "No submitted items found in submission dialog");
            reporter.pass("Submitted Items");

            currentStep = "Done";
            lastSelector = "#kyra_dialog_confirm_btn, button:has-text('Done')";
            submissionDialog.clickDone();
            assertTrue(submissionDialog.waitForClosed(), "Submission dialog remained open after clicking Done");
            assertTrue(requesterPage.isLoaded(), "Failed to return to Requester Page after submission");
            reporter.pass("Done");

            // LOGOUT
            reporter.section("LOGOUT");

            currentStep = "Sign Out";
            lastSelector = "button.kyraSignOutHeaderBtn, button:has-text('Sign Out')";
            HeaderComponent headerComponent = new HeaderComponent(page);
            headerComponent.clickSignOut();
            LogoutDialog logoutDialog = new LogoutDialog(page);
            assertTrue(logoutDialog.isDialogVisible(), "Sign Out confirmation dialog did not appear");
            reporter.pass("Sign Out");

            currentStep = "Yes, Sign Out";
            lastSelector = "#kyra_signout_confirm_btn, button:has-text('Yes, Sign Out')";
            logoutDialog.confirmSignOut();
            reporter.pass("Yes, Sign Out");

            currentStep = "Login Page Returned";
            lastSelector = "#roleSelect-arrow, #signInBtn";
            assertTrue(loginPage.isLoaded(), "Failed to return to Login Page after sign out");
            reporter.pass("Login Page Returned");

            // Final test summary
            reporter.printSummary();

        } catch (Throwable t) {
            reporter.fail(currentStep);
            ScreenshotUtils.captureStepFailure(
                    page,
                    "RequesterEndToEnd",
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
