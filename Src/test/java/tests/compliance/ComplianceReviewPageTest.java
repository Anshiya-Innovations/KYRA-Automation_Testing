package tests.compliance;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ComplianceReviewPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.*;

public class ComplianceReviewPageTest extends BaseTest {

    @Test
    @DisplayName("Compliance Review Dashboard Elements, SoD clearance title, and Tabs Verification")
    public void testComplianceReviewDashboardElements() {
        String persona = ConfigReader.getCompliancePersona();
        String employeeId = ConfigReader.getComplianceId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Compliance Review Page");
        LoginPage loginPage = new LoginPage(page);
        ComplianceReviewPage compliancePage = new ComplianceReviewPage(page);

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

            setCurrentStep("Compliance Review Page");
            assertTrue(compliancePage.isLoaded());
            assertTrue(compliancePage.isComplianceTitleDisplayed(), "Section title must display 'Compliance Review Requests'.");
            assertTrue(compliancePage.getComplianceSubtitle().contains("Segregation of Duties"), "Subtitle must contain SoD compliance description.");
            assertTrue(compliancePage.isPendingQueueBtnVisible(), "Pending Queue button should be visible.");
            assertTrue(compliancePage.isHistoryLogBtnVisible(), "History Log button should be visible.");
            assertTrue(compliancePage.isAccessRequestsTabVisible(), "Access Requests tab should be visible.");
            assertTrue(compliancePage.isRevokeRequestsTabHidden(), "Revoke Requests tab MUST BE HIDDEN for Compliance Review.");
            assertTrue(compliancePage.isApprovalAccessTableVisible(), "Compliance review table should be visible.");
            assertTrue(compliancePage.isFilterBtnVisible(), "Filter button should be visible.");
            assertTrue(compliancePage.isExportBtnVisible(), "Export button should be visible.");
            reporter.pass("Compliance Review Page");

            setCurrentStep("Logout");
            compliancePage.signOut();
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
