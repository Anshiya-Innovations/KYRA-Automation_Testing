package tests.approver;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ApproverPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.*;

public class ApproverPageTest extends BaseTest {

    @Test
    @DisplayName("Approver Dashboard Elements, Tabs, and Table Verification")
    public void testApproverDashboardElements() {
        String persona = ConfigReader.getApproverPersona();
        String employeeId = ConfigReader.getApproverId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Approver Page");
        LoginPage loginPage = new LoginPage(page);
        ApproverPage approverPage = new ApproverPage(page);

        try {
            setCurrentStep("Login Page");
            loginPage.navigate();
            assertTrue(loginPage.isLoaded());
            reporter.pass("Login Page");

            setCurrentStep("Persona Selection");
            loginPage.selectPersona(persona);
            reporter.pass("Persona Selection");

            setCurrentStep("Employee ID");
            loginPage.enterEmployeeId(employeeId);
            reporter.pass("Employee ID");

            setCurrentStep("Sign In");
            loginPage.clickSignIn();
            reporter.pass("Sign In");

            setCurrentStep("Approver Page");
            assertTrue(approverPage.isLoaded());
            assertTrue(approverPage.isUserRequestsTitleDisplayed(), "Section title must display 'User Requests'.");
            assertTrue(approverPage.isPendingQueueBtnVisible(), "Pending Queue pill button should be visible.");
            assertTrue(approverPage.isHistoryLogBtnVisible(), "History Log pill button should be visible.");
            assertTrue(approverPage.isAccessRequestsTabVisible(), "Access Requests tab should be visible.");
            assertTrue(approverPage.isRevokeRequestsTabVisible(), "Revoke Requests tab MUST be visible for Approver.");
            assertTrue(approverPage.isApprovalAccessTableVisible(), "Approval access requests table should be visible.");
            assertTrue(approverPage.isFilterBtnVisible(), "Filter button should be visible.");
            assertTrue(approverPage.isExportBtnVisible(), "Export button should be visible.");
            reporter.pass("Approver Page");

            setCurrentStep("Logout");
            approverPage.signOut();
            reporter.pass("Logout");

            setCurrentStep("Return to Login");
            assertTrue(loginPage.isLoaded());
            reporter.pass("Return to Login");

        } catch (Throwable t) {
            reporter.fail(getCurrentStep());
            throw t;
        } finally {
            reporter.printSummary();
        }
    }
}
