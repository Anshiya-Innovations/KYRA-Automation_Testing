package tests.requester;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.RequesterPage;
import utils.ConfigReader;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.*;

public class RequesterPageTest extends BaseTest {

    @Test
    @DisplayName("Requester Dashboard Components and Action Cards Verification")
    public void testRequesterDashboardElements() {
        String persona = ConfigReader.getRequesterPersona();
        String employeeId = ConfigReader.getRequesterId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("Requester Page");
        LoginPage loginPage = new LoginPage(page);
        RequesterPage requesterPage = new RequesterPage(page);

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

            setCurrentStep("Requester Page");
            assertTrue(requesterPage.isLoaded());
            assertTrue(requesterPage.isMyAccessTabVisible(), "My Access tab button should be visible.");
            assertTrue(requesterPage.isMyHistoryTabVisible(), "My History tab button should be visible.");
            assertTrue(requesterPage.isActiveEntitlementsTitleVisible(), "Active Entitlements title should be visible.");
            assertTrue(requesterPage.isEntitlementsSearchVisible(), "Entitlements search field should be visible.");
            assertTrue(requesterPage.isMyAccessTableVisible(), "My Access entitlements table should be visible.");
            assertTrue(requesterPage.isCardPendingRequestsVisible(), "Pending requests action card should be visible.");
            assertTrue(requesterPage.isCardAddAccessVisible(), "Add access action card should be visible.");
            assertTrue(requesterPage.isCardRemoveAccessVisible(), "Remove access action card should be visible.");
            reporter.pass("Requester Page");

            setCurrentStep("Logout");
            requesterPage.signOut();
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
