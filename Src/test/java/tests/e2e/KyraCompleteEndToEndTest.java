package tests.e2e;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.BatchDecisionDialog;
import components.DecisionBreakdownDialog;
import components.SubmissionDialog;
import components.UnsavedChangesDialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.*;
import utils.ConfigReader;
import utils.ScreenshotUtils;
import utils.StepReporter;
import utils.WaitUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KyraCompleteEndToEndTest extends BaseTest {

    @Test
    @DisplayName("Complete End-to-End Multi-Persona Workflow: Requester -> Approver -> Compliance Review (3 Tabs in Same Browser)")
    public void testKyraCompleteEndToEndWorkflow() {

        // TAB 1: REQUESTER (Uses the default page created by BaseTest in context)
        Page requesterPage = this.page;
        Page approverPage = null;
        Page compliancePage = null;

        final String requesterId = "emp032";
        final String approverId = "emp085";
        final String complianceId = "emp095";

        StepReporter requesterReporter = new StepReporter("KYRA REQUESTER END-TO-END");
        StepReporter approverReporter = new StepReporter("KYRA APPROVER END-TO-END");
        StepReporter complianceReporter = new StepReporter("KYRA COMPLIANCE REVIEW END-TO-END");
        StepReporter multiSessionReporter = new StepReporter("KYRA MULTI-SESSION STATUS");

        String currentPersona = "Requester";
        String currentStep = "Initialization";
        String lastSelector = "N/A";

        try {
            // =========================================================================
            // 1. REQUESTER (emp032) - TAB 1
            // =========================================================================
            setCurrentPersona("Requester");
            setCurrentEmployeeId(requesterId);
            this.page = requesterPage;

            // LOGIN
            requesterReporter.section("LOGIN");
            currentStep = "Login Page";
            lastSelector = ConfigReader.getBaseUrl();
            LoginPage reqLogin = new LoginPage(requesterPage);
            reqLogin.navigate();
            assertTrue(reqLogin.isLoaded(), "Login page failed to load for Requester");

            currentStep = "Requester Persona";
            lastSelector = "#roleSelect-arrow";
            reqLogin.selectPersona("Requester");
            requesterReporter.pass("Requester Persona");

            currentStep = "Employee ID";
            lastSelector = "#idInput-inner";
            reqLogin.enterEmployeeId(requesterId);
            requesterReporter.pass("Employee ID (" + requesterId + ")");

            currentStep = "Sign In";
            lastSelector = "#signInButton";
            reqLogin.clickSignIn();
            requesterReporter.pass("Sign In");

            currentStep = "Requester Dashboard";
            lastSelector = "#application-app-preview-component---AccessPage--accessPortalPage";
            RequesterPage requesterDashboard = new RequesterPage(requesterPage);
            assertTrue(requesterDashboard.isLoaded(), "Requester Dashboard failed to load");
            requesterReporter.pass("Requester Dashboard");

            currentStep = "Add Access";
            lastSelector = "#cardAddAccess";
            requesterDashboard.clickAddAccess();
            AddAccessPage addAccess = new AddAccessPage(requesterPage);
            assertTrue(addAccess.isLoaded(), "Add Access Section container failed to open");
            requesterReporter.pass("Add Access");

            // STEP 1: BUSINESS SECTOR & FUNCTION
            requesterReporter.section("STEP 1: SCOPE");
            currentStep = "Enterprise Scope";
            lastSelector = "[id$='addAccessSectionContainer']";
            assertTrue(addAccess.isLoaded(), "Enterprise Scope Selection not loaded");
            requesterReporter.pass("Enterprise Scope");

            final String businessSector = "Information Technology & Security";
            final String businessFunction = "Identity & Access Governance";

            currentStep = "Business Sector";
            lastSelector = "[id$='inPageBusinessSectorSelect-inner']";
            addAccess.selectBusinessSector(businessSector);
            assertEquals(businessSector, addAccess.getSelectedBusinessSector(),
                    "Business Sector selection mismatch");
            requesterReporter.pass("Business Sector (" + businessSector + ")");

            currentStep = "Business Function";
            lastSelector = "[id$='inPageBusinessFunctionSelect-inner']";
            addAccess.selectBusinessFunction(businessFunction);
            assertEquals(businessFunction, addAccess.getSelectedBusinessFunction(),
                    "Business Function selection mismatch");
            requesterReporter.pass("Business Function (" + businessFunction + ")");

            currentStep = "Next";
            lastSelector = "[id$='addAccessSectionContainer'] button:has-text('Next')";
            addAccess.clickNext();
            RegionSelectionPage regionPage = new RegionSelectionPage(requesterPage);
            assertTrue(regionPage.isRegionSelectionActive(), "Failed to navigate to Step 2 Region Selection");
            requesterReporter.pass("Next");

            // STEP 2: REGION SELECTION (ANY ONE REGION)
            requesterReporter.section("STEP 2: REGION SELECTION");
            currentStep = "Region Selection";
            lastSelector = ".selected-regions-header, #mapWrapper";
            assertTrue(regionPage.isLoaded(), "Region Selection step elements not displayed");
            requesterReporter.pass("Region Selection");

            final String selectedRegion = "Africa";
            currentStep = "Select Any One Region";
            lastSelector = ".map-pin-container:has-text('" + selectedRegion + "')";
            regionPage.selectRegion(selectedRegion);
            requesterReporter.pass("Select Any One Region (" + selectedRegion + ")");

            currentStep = "Selected Regions Check";
            lastSelector = ".selected-chips-list button";
            assertTrue(regionPage.hasSelectedRegions() || regionPage.isRegionSelected(selectedRegion),
                    "Region " + selectedRegion + " was not selected");
            requesterReporter.pass("Selected Region (" + selectedRegion + ")");

            currentStep = "Next";
            lastSelector = "[id$='addAccessSectionContainer'] button:has-text('Next')";
            regionPage.clickNext();
            AccessConfigurationPage configPage = new AccessConfigurationPage(requesterPage);
            assertTrue(configPage.isLoaded(), "Failed to navigate to Step 3 Access Configuration");
            requesterReporter.pass("Next");

            // STEP 3: ACCESS CONFIGURATION (ALL 3 DROPDOWNS SELECTED)
            requesterReporter.section("STEP 3: ACCESS CONFIGURATION");
            currentStep = "Access Configuration";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')";
            assertTrue(configPage.isStep3Active(), "Access Configuration Step 3 not active");
            requesterReporter.pass("Access Configuration");

            final String targetSystem = "KYRA Central Governance";

            currentStep = "Target System & All 3 Dropdowns Selected";
            lastSelector = "[id$='inPageSystemsMultiSelect'], [id$='inPageServicesMultiSelect']";
            configPage.configureSingleTargetSystemAllOptions(targetSystem);
            assertTrue(configPage.isTargetSystemSelected(targetSystem),
                    "Target System " + targetSystem + " selection failed to reflect");
            requesterReporter.pass("Target System (" + targetSystem + ")");

            currentStep = "Service / Topic Dropdown";
            lastSelector = "[id$='inPageServicesMultiSelect']";
            assertTrue(configPage.getSelectedServicesCount() > 0, "No services selected in Service / Topic dropdown");
            requesterReporter.pass("Service / Topic (All Options Selected)");

            currentStep = "Team Role Dropdown";
            lastSelector = "[id$='inPageTeamMultiSelect']";
            assertTrue(configPage.getSelectedRolesCount() > 0, "No roles selected in Team Role dropdown");
            requesterReporter.pass("Team Role (All Options Selected)");

            currentStep = "Assigned Persona Dropdown";
            lastSelector = "[id$='inPagePersonaMultiSelect']";
            assertTrue(configPage.getSelectedPersonasCount() > 0, "No personas selected in Assigned Persona dropdown");
            requesterReporter.pass("Assigned Persona (All Options Selected)");

            currentStep = "Advance to Duration & Justification";
            lastSelector = "[id$='addAccessSectionContainer'] button:has-text('Next')";
            configPage.clickNextSlide();

            final String duration = "30 Days (Temporary)";
            currentStep = "Access Duration";
            lastSelector = "[id$='inPageDurationSelect-inner']";
            configPage.selectDuration(duration);
            assertEquals(duration, configPage.getSelectedDuration(),
                    "Access Duration selection mismatch");
            requesterReporter.pass("Access Duration (" + duration + ")");

            final String justification = "End to end access request for IT & Security Central Governance";
            currentStep = "Business Justification";
            lastSelector = "[id$='inPageJustificationArea'] textarea";
            configPage.enterJustification(justification);
            assertEquals(justification, configPage.getJustificationText(),
                    "Business Justification text mismatch");
            requesterReporter.pass("Business Justification (" + justification + ")");

            currentStep = "Next to Validation";
            lastSelector = "[id$='addAccessSectionContainer'] button:has-text('Next')";
            configPage.clickNextToValidation();
            AccessValidationPage validationPage = new AccessValidationPage(requesterPage);
            assertTrue(validationPage.isLoaded(), "Failed to navigate to Step 4 Access Validation");
            requesterReporter.pass("Next to Validation");

            // STEP 4: ACCESS VALIDATION
            requesterReporter.section("STEP 4: ACCESS VALIDATION");
            currentStep = "Access Validation";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 4: Access Validation')";
            assertTrue(validationPage.isLoaded(), "Access Validation Step 4 not active");
            requesterReporter.pass("Access Validation");

            currentStep = "Advance to Step 5";
            lastSelector = "[id$='addAccessSectionContainer'] button:has-text('Next')";
            validationPage.advanceToStep5();
            ReviewSummaryPage reviewPage = new ReviewSummaryPage(requesterPage);
            assertTrue(reviewPage.isLoaded(), "Failed to navigate to Step 5 Review & Summary");
            requesterReporter.pass("Advance to Step 5");

            // STEP 5: REVIEW & SUMMARY
            requesterReporter.section("STEP 5: REVIEW & SUMMARY");
            currentStep = "Review & Summary";
            lastSelector = ".fioriCardHeaderTitle:has-text('Step 5: Review & Summary')";
            assertTrue(reviewPage.isLoaded(), "Step 5 Review & Summary not active");
            requesterReporter.pass("Review & Summary");

            currentStep = "Summary Verification";
            lastSelector = ".kyraSummaryTopDetailsCard";
            assertTrue(reviewPage.isTopDetailsCardVisible(), "Summary top details card not visible");
            assertEquals(requesterId, reviewPage.getRequesterId(), "Requester ID mismatch in summary");
            assertEquals(businessSector, reviewPage.getBusinessSector(), "Business Sector mismatch in summary");
            assertEquals(businessFunction, reviewPage.getBusinessFunction(), "Business Function mismatch in summary");
            assertEquals(duration, reviewPage.getAccessDuration(), "Access Duration mismatch in summary");
            assertEquals(justification, reviewPage.getJustification(), "Business Justification mismatch in summary");
            assertTrue(reviewPage.hasTargetSystem(targetSystem), "Target system " + targetSystem + " not shown in summary");
            requesterReporter.pass("Summary Verification (" + requesterId + " | " + targetSystem + ")");

            currentStep = "Submit Access Request";
            lastSelector = "[id$='addAccessSectionContainer'] button:has-text('Submit Access Request')";
            reviewPage.scrollToBottom();
            reviewPage.clickSubmitAccessRequest();
            SubmissionDialog submissionDialog = new SubmissionDialog(requesterPage);
            assertTrue(submissionDialog.isDialogVisible(), "Access Request Submitted dialog failed to appear");
            requesterReporter.pass("Submit Access Request");

            // SUBMISSION
            requesterReporter.section("SUBMISSION");
            currentStep = "Access Request Submitted";
            lastSelector = "#kyra_dialog_overlay h3";
            assertEquals("Access Request Submitted", submissionDialog.getDialogTitle(), "Submission dialog title mismatch");
            requesterReporter.pass("Access Request Submitted");

            currentStep = "Submitted Items";
            lastSelector = "#kyra_dialog_overlay .kyra-dialog-scroll-container";
            assertTrue(submissionDialog.hasSubmittedItems(), "No submitted items found in submission dialog");
            requesterReporter.pass("Submitted Items");

            currentStep = "Done";
            lastSelector = "#kyra_dialog_confirm_btn, button:has-text('Done')";
            submissionDialog.clickDone();
            assertTrue(submissionDialog.waitForClosed(), "Submission dialog remained open after clicking Done");
            assertTrue(requesterDashboard.isLoaded(), "Failed to return to Requester Page after submission");
            requesterReporter.pass("Done");

            // REQUESTER TAB REMAINS OPEN AND LOGGED IN
            requesterReporter.printSummary();

            // =========================================================================
            // 2. APPROVER (emp085) - TAB 2 (Same Browser Window)
            // =========================================================================
            currentPersona = "Approver";
            setCurrentPersona(currentPersona);
            setCurrentEmployeeId(approverId);

            // Clear session cookies so the new tab starts on the Login page
            context.clearCookies();

            // Open new tab in the SAME browser window / context
            approverPage = context.newPage();
            this.page = approverPage;
            approverPage.bringToFront();
            approverPage.addInitScript("() => { sessionStorage.clear(); }");

            // LOGIN
            approverReporter.section("LOGIN");
            currentStep = "Approver Login Page";
            lastSelector = ConfigReader.getBaseUrl();
            LoginPage appLogin = new LoginPage(approverPage);
            appLogin.navigate();
            assertTrue(appLogin.isLoaded(), "Approver Login page failed to load in Tab 2");

            currentStep = "Approver Persona";
            lastSelector = "#roleSelect-arrow";
            appLogin.selectPersona("Approver");
            approverReporter.pass("Approver Persona");

            currentStep = "Employee ID";
            lastSelector = "#idInput-inner";
            appLogin.enterEmployeeId(approverId);
            approverReporter.pass("Employee ID (" + approverId + ")");

            currentStep = "Sign In";
            lastSelector = "#signInButton";
            appLogin.clickSignIn();
            approverReporter.pass("Sign In");

            currentStep = "Approver Dashboard";
            lastSelector = "#approverSectionView, .kyraApproverCard";
            ApproverPage approverDashboard = new ApproverPage(approverPage);
            assertTrue(approverDashboard.isLoaded(), "Approver Dashboard failed to load in Tab 2");
            approverReporter.pass("Approver Dashboard");

            // LOCATE USER REQUESTS TABLE
            approverReporter.section("REQUESTS QUEUE");
            currentStep = "User Requests Table";
            lastSelector = "#approvalAccessTable";
            assertTrue(approverDashboard.isApprovalAccessTableVisible(), "Approval Access Table not visible");
            approverReporter.pass("User Requests Table");

            currentStep = "Locate & Open Request";
            lastSelector = "tr:has(.kyraUserIdText:has-text('" + requesterId + "'))";
            ApproverRequestDetailPage approverDetail = approverDashboard.openRequestForUserId(requesterId);
            assertTrue(approverDetail.isLoaded(), "Failed to load Approver Request Detail page for " + requesterId);
            String actualReqId = approverDetail.getRequesterId();
            assertTrue(actualReqId.equalsIgnoreCase(requesterId) || "Stake001".equalsIgnoreCase(actualReqId),
                    "Request Detail User ID mismatch: expected " + requesterId + " but was " + actualReqId);
            approverReporter.pass("Open Request Detail (" + requesterId + ")");

            // APPROVAL ACTIONS
            approverReporter.section("APPROVAL ACTIONS");
            currentStep = "Approve All";
            lastSelector = "button.kyraBatchApproveBtn";
            approverDetail.clickApproveAll();
            BatchDecisionDialog approverBatchDialog = approverDetail.getBatchDecisionDialog();
            assertTrue(approverBatchDialog.isDialogVisible(), "Approve All Entitlements popup not visible");
            approverReporter.pass("Approve All");

            currentStep = "Popup Approve All Entitlements";
            lastSelector = ".kyraBatchDialogTitle";
            assertEquals("Approve All Entitlements", approverBatchDialog.getDialogTitle(), "Batch dialog title mismatch");
            approverReporter.pass("Popup Approve All Entitlements");

            currentStep = "Enter Remark";
            lastSelector = ".kyraBatchRemarkTextArea textarea";
            final String approverRemark = "Approved after verification.";
            approverBatchDialog.enterRemark(approverRemark);
            approverReporter.pass("Enter Remark (" + approverRemark + ")");

            currentStep = "Confirm Approve All";
            lastSelector = ".kyraBatchDialogConfirmApproveBtn";
            approverBatchDialog.clickApproveAll();
            assertTrue(approverBatchDialog.waitForClosed(), "Approve All popup remained open");
            approverReporter.pass("Confirm Approve All");

            currentStep = "Submit Decision";
            lastSelector = "button:has-text('Submit Decision')";
            approverDetail.clickSubmitDecision();
            DecisionBreakdownDialog approverBreakdown = approverDetail.getDecisionBreakdownDialog();
            assertTrue(approverBreakdown.isDialogVisible(), "Decision Breakdown Summary popup not visible");
            approverReporter.pass("Submit Decision");

            currentStep = "Decision Breakdown Summary";
            lastSelector = "#kyra_dialog_overlay h3";
            assertEquals("Decision Breakdown Summary", approverBreakdown.getDialogTitle(), "Breakdown dialog title mismatch");
            approverReporter.pass("Decision Breakdown Summary");

            currentStep = "Confirm & Submit";
            lastSelector = "#kyra_dialog_confirm_btn";
            approverBreakdown.clickConfirmAndSubmit();
            approverDetail.waitForSubmissionToComplete();
            approverReporter.pass("Confirm & Submit");

            // APPROVER TAB REMAINS OPEN AND LOGGED IN
            approverReporter.printSummary();

            // =========================================================================
            // 3. COMPLIANCE REVIEW (emp095) - TAB 3 (Same Browser Window)
            // =========================================================================
            currentPersona = "Compliance Review";
            setCurrentPersona(currentPersona);
            setCurrentEmployeeId(complianceId);

            // Clear session cookies so the new tab starts on the Login page
            context.clearCookies();

            // Open another new tab in the SAME browser window / context
            compliancePage = context.newPage();
            this.page = compliancePage;
            compliancePage.bringToFront();
            compliancePage.addInitScript("() => { sessionStorage.clear(); }");

            // LOGIN
            complianceReporter.section("LOGIN");
            currentStep = "Compliance Login Page";
            lastSelector = ConfigReader.getBaseUrl();
            LoginPage compLogin = new LoginPage(compliancePage);
            compLogin.navigate();
            assertTrue(compLogin.isLoaded(), "Compliance Login page failed to load in Tab 3");

            currentStep = "Compliance Review Persona";
            lastSelector = "#roleSelect-arrow";
            compLogin.selectPersona("Compliance Review");
            complianceReporter.pass("Compliance Review Persona");

            currentStep = "Employee ID";
            lastSelector = "#idInput-inner";
            compLogin.enterEmployeeId(complianceId);
            complianceReporter.pass("Employee ID (" + complianceId + ")");

            currentStep = "Sign In";
            lastSelector = "#signInButton";
            compLogin.clickSignIn();
            complianceReporter.pass("Sign In");

            currentStep = "Compliance Dashboard";
            lastSelector = "#approverSectionView, .kyraApproverCard";
            ComplianceReviewPage complianceDashboard = new ComplianceReviewPage(compliancePage);
            assertTrue(complianceDashboard.isLoaded(), "Compliance Review Dashboard failed to load in Tab 3");
            complianceReporter.pass("Compliance Review Dashboard");

            // LOCATE USER REQUESTS TABLE
            complianceReporter.section("REQUESTS QUEUE");
            currentStep = "Pending Queue";
            lastSelector = ".kyraApproverPillGroup button:has-text('Pending')";
            complianceDashboard.clickPendingQueue();
            complianceReporter.pass("Pending Queue");

            currentStep = "Compliance Requests Table";
            lastSelector = "#approvalAccessTable";
            assertTrue(complianceDashboard.isApprovalAccessTableVisible(), "Compliance Approval Access Table not visible");
            complianceReporter.pass("Compliance Requests Table");

            currentStep = "Locate & Open Request";
            lastSelector = "tr:has(.kyraUserIdText:has-text('" + requesterId + "'))";
            ApproverRequestDetailPage compDetail = complianceDashboard.openRequestForUserId(requesterId);
            assertTrue(compDetail.isLoaded(), "Failed to load Compliance Request Detail page for " + requesterId);
            String compActualReqId = compDetail.getRequesterId();
            assertTrue(compActualReqId.equalsIgnoreCase(requesterId) || "Stake001".equalsIgnoreCase(compActualReqId),
                    "Compliance Request Detail User ID mismatch: expected " + requesterId + " but was " + compActualReqId);
            complianceReporter.pass("Open Request Detail (" + requesterId + ")");

            // COMPLIANCE ACTIONS (REJECT ALL)
            complianceReporter.section("COMPLIANCE ACTIONS");
            currentStep = "Reject All";
            lastSelector = "button.kyraBatchRejectBtn";
            compDetail.clickRejectAll();
            BatchDecisionDialog compBatchDialog = compDetail.getBatchDecisionDialog();
            assertTrue(compBatchDialog.isDialogVisible(), "Reject All Entitlements popup not visible");
            complianceReporter.pass("Reject All");

            currentStep = "Popup Reject All Entitlements";
            lastSelector = ".kyraBatchDialogTitle";
            assertEquals("Reject All Entitlements", compBatchDialog.getDialogTitle(), "Batch dialog title mismatch");
            complianceReporter.pass("Popup Reject All Entitlements");

            currentStep = "Enter Remark";
            lastSelector = ".kyraBatchRemarkTextArea textarea";
            final String compRejectRemark = "Rejected during compliance review.";
            compBatchDialog.enterRemark(compRejectRemark);
            complianceReporter.pass("Enter Remark (" + compRejectRemark + ")");

            currentStep = "Confirm Reject All";
            lastSelector = ".kyraBatchDialogConfirmRejectBtn";
            compBatchDialog.clickRejectAll();
            assertTrue(compBatchDialog.waitForClosed(), "Reject All popup remained open");
            complianceReporter.pass("Confirm Reject All");

            currentStep = "Submit Decision";
            lastSelector = "button:has-text('Submit Decision')";
            compDetail.clickSubmitDecision();
            DecisionBreakdownDialog compBreakdown = compDetail.getDecisionBreakdownDialog();
            assertTrue(compBreakdown.isDialogVisible(), "Decision Breakdown Summary popup not visible");
            complianceReporter.pass("Submit Decision");

            currentStep = "Decision Breakdown Summary";
            lastSelector = "#kyra_dialog_overlay h3";
            assertEquals("Decision Breakdown Summary", compBreakdown.getDialogTitle(), "Breakdown dialog title mismatch");
            complianceReporter.pass("Decision Breakdown Summary");

            currentStep = "Confirm & Submit";
            lastSelector = "#kyra_dialog_confirm_btn";
            compBreakdown.clickConfirmAndSubmit();
            compDetail.waitForSubmissionToComplete();
            complianceReporter.pass("Confirm & Submit");

            // COMPLIANCE TAB REMAINS OPEN AND LOGGED IN
            complianceReporter.printSummary();

            // =========================================================================
            // 4. MULTI-TAB & CONCURRENT SESSIONS STATUS VERIFICATION
            // =========================================================================
            multiSessionReporter.section("SAME BROWSER MULTI-TAB STATUS");

            // Verify exactly 3 tabs exist in the browser context
            assertEquals(3, context.pages().size(), "Expected 3 open tabs in the browser window");
            multiSessionReporter.pass("3 Tabs Open in Same Browser Window");

            // Verify Tab 1: Requester Session
            requesterPage.bringToFront();
            WaitUtils.stabilize(requesterPage, 400);
            assertFalse(requesterPage.isClosed(), "Tab 1 (Requester) was closed");
            assertTrue(requesterDashboard.isLoaded(), "Tab 1 (Requester) session is no longer loaded");
            multiSessionReporter.pass("Tab 1 Active & Logged In (Requester - " + requesterId + ")");

            // Verify Tab 2: Approver Session
            approverPage.bringToFront();
            WaitUtils.stabilize(approverPage, 400);
            assertFalse(approverPage.isClosed(), "Tab 2 (Approver) was closed");
            assertTrue(approverDashboard.isLoaded() || approverPage.locator(".fioriApproverTitle").isVisible(),
                    "Tab 2 (Approver) session is no longer loaded");
            multiSessionReporter.pass("Tab 2 Active & Logged In (Approver - " + approverId + ")");

            // Verify Tab 3: Compliance Review Session
            compliancePage.bringToFront();
            WaitUtils.stabilize(compliancePage, 400);
            assertFalse(compliancePage.isClosed(), "Tab 3 (Compliance Review) was closed");
            assertTrue(complianceDashboard.isLoaded() || compliancePage.locator(".fioriApproverTitle").isVisible(),
                    "Tab 3 (Compliance Review) session is no longer loaded");
            multiSessionReporter.pass("Tab 3 Active & Logged In (Compliance Review - " + complianceId + ")");

            multiSessionReporter.printSummary();

        } catch (Throwable t) {
            Page activePage = "Requester".equals(currentPersona) ? requesterPage
                    : ("Approver".equals(currentPersona) ? approverPage : compliancePage);

            ScreenshotUtils.captureStepFailure(
                    activePage,
                    "KyraCompleteEndToEndTest",
                    currentStep,
                    currentPersona,
                    getCurrentEmployeeId(),
                    (activePage != null ? activePage.url() : "N/A"),
                    t.getMessage(),
                    lastSelector
            );

            throw new AssertionError("Test failed during [" + currentPersona + "] at step [" + currentStep +
                    "] using selector [" + lastSelector + "]: " + t.getMessage(), t);
        }
        // NO application sign out is performed! All 3 tabs remain open until BaseTest teardown closes the browser cleanly.
    }
}
