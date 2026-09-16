package tests.compliance;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ComplianceReviewPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.*;

public class ComplianceReviewLoginTest extends BaseTest {

    @Test
    @DisplayName("Compliance Review Login, Landing Page Verification, and Logout Flow")
    public void testComplianceReviewLoginAndLogout() {
        String persona = ConfigReader.getCompliancePersona();
        String employeeId = ConfigReader.getComplianceId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Compliance Review");
        LoginPage loginPage = new LoginPage(page);
        ComplianceReviewPage compliancePage = new ComplianceReviewPage(page);

        try {
            // 1. Open login page & wait for availability
            setCurrentStep("Login Page");
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Login page failed to load fully.");
            reporter.pass("Login Page");

            // 2. Select Persona = Compliance Review & verify selection
            setCurrentStep("Persona Selection");
            loginPage.selectPersona(persona);
            String selectedPersona = loginPage.getSelectedPersona();
            assertEquals(persona, selectedPersona, "Selected persona does not match expected.");
            reporter.pass("Persona Selection");

            // 3. Enter Employee ID = emp095 & verify entered value
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

            // 5. Wait for Compliance Review page and verify page-specific evidence
            setCurrentStep("Compliance Review Page");
            assertTrue(compliancePage.isLoaded(), "Compliance Review landing page failed to load.");
            assertEquals("Compliance Review", compliancePage.getHeaderComponent().getActivePersona(), "Active persona pill is not 'Compliance Review'.");
            assertEquals(employeeId, compliancePage.getActiveUserId(), "Active user ID badge is incorrect.");
            assertTrue(compliancePage.isComplianceTitleDisplayed(), "Section title must display 'Compliance Review Requests'.");
            compliancePage.waitForStabilization();
            reporter.pass("Compliance Review Page");

            // 6. Sign Out & confirm dialog
            setCurrentStep("Logout");
            compliancePage.signOut();
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
