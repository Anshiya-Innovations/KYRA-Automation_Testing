package tests.compliance;

import base.BaseTest;
import components.BatchDecisionDialog;
import components.DecisionBreakdownDialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ApproverRequestDetailPage;
import pages.ComplianceReviewPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ScreenshotUtils;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplianceReviewProcessRequestTest extends BaseTest {

    @Test
    @DisplayName("Compliance Review: Login (emp095), Locate Request (emp032), Reject All, and Submit Decision")
    public void testComplianceReviewProcessRequest() {
        String persona = ConfigReader.getCompliancePersona();
        String employeeId = ConfigReader.getComplianceId();
        String requesterId = ConfigReader.getRequesterId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Compliance Review Process");
        LoginPage loginPage = new LoginPage(page);
        ComplianceReviewPage complianceDashboard = new ComplianceReviewPage(page);

        String currentStep = "Login Page";
        String lastSelector = ConfigReader.getBaseUrl();

        try {
            // 1. LOGIN
            reporter.section("LOGIN");
            currentStep = "Compliance Login Page";
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Compliance Login page failed to load");
            reporter.pass("Compliance Login Page");

            currentStep = "Select Compliance Review Persona";
            lastSelector = "#roleSelect-arrow";
            loginPage.selectPersona(persona);
            reporter.pass("Compliance Review Persona");

            currentStep = "Enter Employee ID";
            lastSelector = "#idInput-inner";
            loginPage.enterEmployeeId(employeeId);
            reporter.pass("Employee ID (" + employeeId + ")");

            currentStep = "Sign In";
            lastSelector = "#signInButton";
            loginPage.clickSignIn();
            reporter.pass("Sign In");

            currentStep = "Compliance Dashboard";
            lastSelector = "#approverSectionView, .kyraApproverCard";
            assertTrue(complianceDashboard.isLoaded(), "Compliance Review Dashboard failed to load");
            reporter.pass("Compliance Review Dashboard");

            // 2. LOCATE REQUEST
            reporter.section("REQUESTS QUEUE");
            currentStep = "Pending Queue";
            lastSelector = ".kyraApproverPillGroup button:has-text('Pending')";
            complianceDashboard.clickPendingQueue();
            reporter.pass("Pending Queue");

            currentStep = "Compliance Requests Table";
            lastSelector = "#approvalAccessTable";
            assertTrue(complianceDashboard.isApprovalAccessTableVisible(), "Compliance Approval Access Table not visible");
            reporter.pass("Compliance Requests Table");

            currentStep = "Locate & Open Request";
            lastSelector = "tr:has(.kyraUserIdText:has-text('" + requesterId + "'))";
            ApproverRequestDetailPage compDetail = complianceDashboard.openRequestForUserId(requesterId);
            assertTrue(compDetail.isLoaded(), "Failed to load Compliance Request Detail page for " + requesterId);
            reporter.pass("Open Request Detail (" + requesterId + ")");

            // 3. REJECT ALL ACTIONS
            reporter.section("COMPLIANCE ACTIONS");
            currentStep = "Reject All";
            lastSelector = "button.kyraBatchRejectBtn";
            compDetail.clickRejectAll();
            BatchDecisionDialog compBatchDialog = compDetail.getBatchDecisionDialog();
            assertTrue(compBatchDialog.isDialogVisible(), "Reject All Entitlements popup not visible");
            reporter.pass("Reject All");

            currentStep = "Popup Reject All Entitlements";
            lastSelector = ".kyraBatchDialogTitle";
            assertEquals("Reject All Entitlements", compBatchDialog.getDialogTitle(), "Batch dialog title mismatch");
            reporter.pass("Popup Reject All Entitlements");

            currentStep = "Enter Remark";
            lastSelector = ".kyraBatchRemarkTextArea textarea";
            final String compRejectRemark = "Rejected during compliance review.";
            compBatchDialog.enterRemark(compRejectRemark);
            reporter.pass("Enter Remark (" + compRejectRemark + ")");

            currentStep = "Confirm Reject All";
            lastSelector = ".kyraBatchDialogConfirmRejectBtn";
            compBatchDialog.clickRejectAll();
            assertTrue(compBatchDialog.waitForClosed(), "Reject All popup remained open");
            reporter.pass("Confirm Reject All");

            currentStep = "Submit Decision";
            lastSelector = "button:has-text('Submit Decision')";
            compDetail.clickSubmitDecision();
            DecisionBreakdownDialog compBreakdown = compDetail.getDecisionBreakdownDialog();
            assertTrue(compBreakdown.isDialogVisible(), "Decision Breakdown Summary popup not visible");
            reporter.pass("Submit Decision");

            currentStep = "Decision Breakdown Summary";
            lastSelector = "#kyra_dialog_overlay h3";
            assertEquals("Decision Breakdown Summary", compBreakdown.getDialogTitle(), "Breakdown dialog title mismatch");
            reporter.pass("Decision Breakdown Summary");

            currentStep = "Confirm & Submit";
            lastSelector = "#kyra_dialog_confirm_btn";
            compBreakdown.clickConfirmAndSubmit();
            compDetail.waitForSubmissionToComplete();
            reporter.pass("Confirm & Submit");

            reporter.printSummary();

        } catch (Throwable t) {
            ScreenshotUtils.captureStepFailure(
                    page,
                    "ComplianceReviewProcessRequestTest",
                    currentStep,
                    persona,
                    employeeId,
                    page.url(),
                    t.getMessage(),
                    lastSelector
            );
            throw new AssertionError("Test failed during [" + persona + "] at step [" + currentStep +
                    "] using selector [" + lastSelector + "]: " + t.getMessage(), t);
        }
    }
}
