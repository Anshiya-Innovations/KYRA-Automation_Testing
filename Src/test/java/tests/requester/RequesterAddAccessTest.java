package tests.requester;

import base.BaseTest;
import components.UnsavedChangesDialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.AddAccessPage;
import pages.LoginPage;
import pages.RegionSelectionPage;
import pages.RequesterPage;
import utils.ConfigReader;
import utils.StepReporter;

import static org.junit.jupiter.api.Assertions.*;

public class RequesterAddAccessTest extends BaseTest {

    @Test
    @DisplayName("Requester Add Access Flow: Enterprise Scope, Region Selection, and Unsaved Changes Dialog")
    public void testRequesterAddAccessFlow() {
        String persona = ConfigReader.getRequesterPersona();
        String employeeId = ConfigReader.getRequesterId();

        setCurrentPersona(persona);
        setCurrentEmployeeId(employeeId);

        StepReporter reporter = new StepReporter("REQUESTER ADD ACCESS");
        LoginPage loginPage = new LoginPage(page);
        RequesterPage requesterPage = new RequesterPage(page);
        AddAccessPage addAccessPage = new AddAccessPage(page);
        RegionSelectionPage regionSelectionPage = new RegionSelectionPage(page);
        UnsavedChangesDialog unsavedChangesDialog = new UnsavedChangesDialog(page);

        try {
            // 1. Open login page
            setCurrentStep("Login Page");
            loginPage.navigate();
            assertTrue(loginPage.isLoaded(), "Login page failed to load fully.");
            reporter.pass("Login Page");

            // 2. Select Requester Persona
            setCurrentStep("Requester Persona");
            loginPage.selectPersona(persona);
            assertEquals(persona, loginPage.getSelectedPersona(), "Selected persona does not match expected.");
            reporter.pass("Requester Persona");

            // 3. Enter Employee ID emp010
            setCurrentStep("Employee ID emp010");
            loginPage.enterEmployeeId(employeeId);
            assertEquals(employeeId, loginPage.getEnteredEmployeeId(), "Entered employee ID does not match expected.");
            reporter.pass("Employee ID emp010");

            // 4. Click Sign In
            setCurrentStep("Sign In");
            loginPage.clickSignIn();
            assertFalse(loginPage.hasError(), "Sign in produced an unexpected error: " + loginPage.getErrorMessage());
            reporter.pass("Sign In");

            // 5. Wait and verify Requester Page
            setCurrentStep("Requester Page");
            assertTrue(requesterPage.isLoaded(), "Requester page failed to load.");
            assertEquals("Requester", requesterPage.getHeaderComponent().getActivePersona(), "Active persona is not Requester.");
            assertEquals(employeeId, requesterPage.getActiveUserId(), "Active user ID does not match " + employeeId);
            reporter.pass("Requester Page");

            // 6. Locate and click Add Access card
            setCurrentStep("Add Access");
            assertTrue(requesterPage.isCardAddAccessVisible(), "Add Access card is not visible on Requester page.");
            requesterPage.clickAddAccess();
            reporter.pass("Add Access");

            // 7. Verify Enterprise Scope Selection is displayed
            setCurrentStep("Enterprise Scope");
            assertTrue(addAccessPage.isLoaded(), "Add Access Enterprise Scope Selection container did not load.");
            reporter.pass("Enterprise Scope");

            // 8. Select Business Sector = Finance & Enterprise Performance
            setCurrentStep("Business Sector");
            String targetSector = "Finance & Enterprise Performance";
            addAccessPage.selectBusinessSector(targetSector);
            String selectedSector = addAccessPage.getSelectedBusinessSector();
            assertTrue(selectedSector.contains("Finance"), "Selected sector does not contain 'Finance'. Actual: " + selectedSector);
            reporter.pass("Business Sector");

            // 9. Select Business Function = Corporate Accounting
            setCurrentStep("Business Function");
            String targetFunction = "Corporate Accounting";
            addAccessPage.selectBusinessFunction(targetFunction);
            String selectedFunction = addAccessPage.getSelectedBusinessFunction();
            assertTrue(selectedFunction.contains("Corporate Accounting"), "Selected function does not contain 'Corporate Accounting'. Actual: " + selectedFunction);
            reporter.pass("Business Function");

            // 10. Click Next Button
            setCurrentStep("Next Button");
            addAccessPage.clickNext();
            reporter.pass("Next Button");

            // 11. Verify Region Selection Page
            setCurrentStep("Region Selection");
            assertTrue(regionSelectionPage.isLoaded(), "Region Selection page did not load after clicking Next.");
            reporter.pass("Region Selection");

            // 12. Click Previous Button
            setCurrentStep("Previous");
            regionSelectionPage.clickPrevious();
            reporter.pass("Previous");

            // 13. Verify Enterprise Scope Returned
            setCurrentStep("Enterprise Scope Returned");
            assertTrue(addAccessPage.isEnterpriseScopeVisible(), "Enterprise Scope Selection is not visible after clicking Previous.");
            reporter.pass("Enterprise Scope Returned");

            // 14. Scroll to bottom and click Cancel
            setCurrentStep("Cancel");
            addAccessPage.clickCancel();
            reporter.pass("Cancel");

            // 15. Verify Unsaved Changes Dialog appears
            setCurrentStep("Unsaved Changes Dialog");
            assertTrue(unsavedChangesDialog.isDialogVisible(), "Unsaved Changes dialog did not appear after clicking Cancel.");
            String dialogTitle = unsavedChangesDialog.getDialogTitle();
            assertTrue(dialogTitle.contains("Unsaved Changes"), "Dialog title does not contain 'Unsaved Changes'. Actual: '" + dialogTitle + "'");
            reporter.pass("Unsaved Changes Dialog");

            // 16. Click Stay on Page
            setCurrentStep("Stay on Page");
            unsavedChangesDialog.clickStayOnPage();
            reporter.pass("Stay on Page");

            // 17. Verify dialog closed and still on Enterprise Scope Selection
            setCurrentStep("Still on Current Page");
            assertTrue(unsavedChangesDialog.waitForClosed(), "Unsaved Changes dialog failed to close after clicking Stay on Page.");
            assertTrue(addAccessPage.isEnterpriseScopeVisible(), "User is no longer on Enterprise Scope Selection page.");
            reporter.pass("Still on Current Page");

        } catch (Throwable t) {
            reporter.fail(getCurrentStep());
            throw t;
        } finally {
            reporter.printSummary();
        }
    }
}
