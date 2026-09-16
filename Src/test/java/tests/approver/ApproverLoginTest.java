package tests.approver;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ApproverPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.*;

public class ApproverLoginTest extends BaseTest {

    @Test
    @DisplayName("Approver Login, Landing Page Verification, and Logout Flow")
    public void testApproverLoginAndLogout() {
        String persona = ConfigReader.getApproverPersona();
        String employeeId = ConfigReader.getApproverId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Approver");
        LoginPage loginPage = new LoginPage(page);
        ApproverPage approverPage = new ApproverPage(page);

        try {
            // 1. Open login page & wait for availability
            setCurrentStep("Login Page");
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Login page failed to load fully.");
            reporter.pass("Login Page");

            // 2. Select Persona = Approver & verify selection
            setCurrentStep("Persona Selection");
            loginPage.selectPersona(persona);
            String selectedPersona = loginPage.getSelectedPersona();
            assertEquals(persona, selectedPersona, "Selected persona does not match expected.");
            reporter.pass("Persona Selection");

            // 3. Enter Employee ID = emp085 & verify entered value
            setCurrentStep("Employee ID");
            loginPage.enterEmployeeId(employeeId);
            String actualId = loginPage.getEnteredEmployeeId();
            assertEquals(employeeId, actualId, "Entered employee ID does not match expected.");
            reporter.pass("Employee ID");

            // 4. Click Sign In
            setCurrentStep("Sign In");
            loginPage.clickSignIn();
            assertFalse(loginPage.hasError(), "Sign in produced an error: " + loginPage.getErrorMessage());
            reporter.pass("Sign In");

            // 5. Wait for Approver page and verify page-specific evidence
            setCurrentStep("Approver Page");
            assertTrue(approverPage.isLoaded(), "Approver landing page failed to load.");
            assertEquals("Approver", approverPage.getHeaderComponent().getActivePersona(), "Active persona pill is not 'Approver'.");
            assertEquals(employeeId, approverPage.getActiveUserId(), "Active user ID badge is incorrect.");
            assertTrue(approverPage.isUserRequestsTitleDisplayed(), "Approver section title should be 'User Requests'.");
            approverPage.waitForStabilization();
            reporter.pass("Approver Page");

            // 6. Sign Out & confirm dialog
            setCurrentStep("Logout");
            approverPage.signOut();
            reporter.pass("Logout");

            // 7. Wait until login page appears again & verify availability
            setCurrentStep("Return to Login");
            assertTrue(loginPage.isLoaded(), "Failed to return to Login page after logout.");
            reporter.pass("Return to Login");

        } catch (Throwable t) {
            reporter.fail(getCurrentStep());
            throw t;
        } finally {
            reporter.printSummary();
        }
    }
}
