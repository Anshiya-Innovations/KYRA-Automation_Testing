package tests.requester;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RequesterPage;
import utils.ConfigReader;
import utils.StepReporter;
import utils.WaitUtils;

import static org.junit.jupiter.api.Assertions.*;

public class RequesterLoginTest extends BaseTest {

    @Test
    @DisplayName("Requester Login, Landing Page Verification, and Logout Flow")
    public void testRequesterLoginAndLogout() {
        String persona = ConfigReader.getRequesterPersona();
        String employeeId = ConfigReader.getRequesterId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Requester");
        LoginPage loginPage = new LoginPage(page);
        RequesterPage requesterPage = new RequesterPage(page);

        try {
            // 1. Open login page & wait for availability
            setCurrentStep("Login Page");
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Login page failed to load fully.");
            reporter.pass("Login Page");

            // 2. Select Persona = Requester & verify selection
            setCurrentStep("Persona Selection");
            loginPage.selectPersona(persona);
            String selectedPersona = loginPage.getSelectedPersona();
            assertEquals(persona, selectedPersona, "Selected persona does not match expected.");
            reporter.pass("Persona Selection");

            // 3. Enter Employee ID = emp010 & verify entered value
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

            // 5. Wait for Requester page and verify page-specific evidence
            setCurrentStep("Requester Page");
            assertTrue(requesterPage.isLoaded(), "Requester landing page failed to load.");
            assertEquals("Requester", requesterPage.getHeaderComponent().getActivePersona(), "Active persona pill is not 'Requester'.");
            assertEquals(employeeId, requesterPage.getActiveUserId(), "Active user ID badge is incorrect.");
            assertTrue(requesterPage.isApproverSectionHidden(), "Approver section must NOT be visible on Requester page.");
            requesterPage.waitForStabilization();
            reporter.pass("Requester Page");

            // 6. Sign Out & confirm dialog
            setCurrentStep("Logout");
            requesterPage.signOut();
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
