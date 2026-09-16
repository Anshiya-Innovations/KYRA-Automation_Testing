# KYRA Enterprise Test Automation Framework

A modular, robust, enterprise-grade UI test automation framework built using **Playwright Java**, **JUnit 5**, and the **Page Object Model (POM)** pattern to automate end-to-end user flows for the **KYRA Enterprise Access Management Portal**.

---

## 1. Technology Stack

- **Language**: Java 17+
- **Build & Dependency Management**: Apache Maven
- **Browser Automation**: Microsoft Playwright Java (1.55.0)
- **Test Framework & Assertions**: JUnit 5 (JUnit Jupiter 5.11.0)
- **Test Runner**: Maven Surefire Plugin (3.5.0)
- **Browser**: Chromium (headed mode for visual observation by default, configurable to headless)
- **Pattern**: Page Object Model (POM) with isolated reusable components

---

## 2. Supported Personas & Test Data

The framework models and automates independent user journeys for all three KYRA enterprise personas:

| Persona | Employee ID | Landing Page Evidence | Role-Specific Distinctions |
| :--- | :--- | :--- | :--- |
| **Requester** | `emp010` | Portal ShellBar + Welcome Banner (`.kyraHeaderRolePill` = "Requester") | Active Entitlements Table (`myAccessTable`), Action Cards (Pending, Add Access, Remove Access), Approver section is **hidden**. |
| **Approver** | `emp085` | Portal ShellBar + Embedded Approver View (`approverSectionView`) | Section Title = **"User Requests"**, Queue buttons (Pending / History), both **Access** and **Revoke** tabs visible, `approvalAccessTable` visible. |
| **Compliance Review** | `emp095` | Portal ShellBar + Embedded Approver View (`approverSectionView`) | Section Title = **"Compliance Review Requests"**, SoD clearance subtitle, **Access** tab visible while **Revoke** tab is **hidden**, `approvalAccessTable` visible. |

---

## 3. Framework Architecture & Structure

```
KYRA-Automation_Testing/
├── pom.xml                                   # Maven project configuration
├── README.md                                 # Framework guide & application analysis notes
├── docs/
│   └── UI_SELECTOR_REFERENCE.md             # Complete UI selector documentation
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── components/
│   │   │   │   ├── HeaderComponent.java      # ShellBar, user badges, persona pills, sign out
│   │   │   │   ├── LogoutDialog.java         # Sign out confirmation modal dialog
│   │   │   │   └── PersonaSelector.java      # SAPUI5 roleSelect control & option selection
│   │   │   │
│   │   │   ├── pages/
│   │   │   │   ├── BasePage.java             # Base Page Object with common waits & helpers
│   │   │   │   ├── LoginPage.java            # Login page interactions & error checks
│   │   │   │   ├── RequesterPage.java        # Requester dashboard, entitlements, action cards
│   │   │   │   ├── ApproverPage.java         # Approver dashboard, queue pills, requests table
│   │   │   │   └── ComplianceReviewPage.java # Compliance Review dashboard, SoD title, table
│   │   │   │
│   │   │   └── utils/
│   │   │       ├── ConfigReader.java         # Centralized configuration reader
│   │   │       ├── ScreenshotUtils.java      # Automatic failure screenshot capture
│   │   │       ├── StepReporter.java         # Formatted console summary output reporter
│   │   │       └── WaitUtils.java            # SAPUI5 synchronization & wait helpers
│   │   │
│   │   └── resources/
│   │       └── config.properties             # Environment URLs, test IDs, and timeouts
│   │
│   └── test/
│       └── java/
│           ├── base/
│           │   ├── BaseTest.java             # JUnit 5 lifecycle, browser context isolation
│           │   └── TestFailureWatcher.java   # Exception handler for failure screenshots
│           │
│           └── tests/
│               ├── requester/
│               │   ├── RequesterLoginTest.java
│               │   └── RequesterPageTest.java
│               │
│               ├── approver/
│               │   ├── ApproverLoginTest.java
│               │   └── ApproverPageTest.java
│               │
│               └── compliance/
│                   ├── ComplianceReviewLoginTest.java
│                   └── ComplianceReviewPageTest.java
│
└── test-results/
    └── screenshots/                          # Failure screenshots directory
```

---

## 4. Test Execution Instructions

### Prerequisites
- JDK 17 or higher
- Apache Maven 3.8+
- Active KYRA frontend running at `http://localhost:8080`
- Active KYRA backend service running at `http://localhost:4004`

### Compiling the Project
```bash
mvn clean test-compile
```

### Running Specific Persona Tests

**1. Requester Tests:**
```bash
mvn test -Dtest=RequesterLoginTest
mvn test -Dtest=RequesterPageTest
mvn test -Dtest=RequesterAddAccessTest
```

**2. Approver Tests:**
```bash
mvn test -Dtest=ApproverLoginTest
mvn test -Dtest=ApproverPageTest
```

**3. Compliance Review Tests:**
```bash
mvn test -Dtest=ComplianceReviewLoginTest
mvn test -Dtest=ComplianceReviewPageTest
```

### Running All Tests in Headless Mode (CI / Fast Execution)
```bash
mvn test -Dbrowser.headless=true
```

---

## 5. Console Reporting Output Format

Each test suite logs detailed progress and generates a clean summary matrix:

```
========================================
REQUESTER TEST
========================================
Login Page        : PASS
Persona Selection : PASS
Employee ID       : PASS
Sign In           : PASS
Requester Page    : PASS
Logout            : PASS
Return to Login   : PASS
========================================

========================================
APPROVER TEST
========================================
Login Page        : PASS
Persona Selection : PASS
Employee ID       : PASS
Sign In           : PASS
Approver Page     : PASS
Logout            : PASS
Return to Login   : PASS
========================================

========================================
COMPLIANCE REVIEW TEST
========================================
Login Page        : PASS
Persona Selection : PASS
Employee ID       : PASS
Sign In           : PASS
Compliance Review Page: PASS
Logout            : PASS
Return to Login   : PASS
========================================
```

---

## 6. Failure Screenshot Mechanism

In the event of any assertion failure or timeout:
1. `TestFailureWatcher` intercepts the exception prior to browser context teardown.
2. A full-page screenshot is automatically saved in `test-results/screenshots/`.
3. Standard naming format:
   - `Requester_emp010_failure.png`
   - `Approver_emp085_failure.png`
   - `ComplianceReview_emp095_failure.png`
4. Failure details, including URL, failed step, persona, and employee ID, are logged to standard output.

---

## 7. Discovered Application Analysis Notes & Quirks

During the read-only analysis of the existing frontend and backend implementations, the following key application behaviors were identified:

1. **SAPUI5 `Select` Control Internals (`#roleSelect`)**:
   - The dropdown container `#application-app-preview-component---Login--roleSelect` contains an accessibility element `#roleSelect-hiddenSelect` that must not be directly clicked.
   - When the dropdown opens, SAPUI5 creates a popover containing items. The initial placeholder item `Select persona` remains in the DOM with `visible: false`. Waiting for `li[role='option'].first()` will hang because the first item is hidden. Automation must target visible options: `.sapMPopover li[role='option']` filtered by text.

2. **Dynamic Section Rendering inside `AccessPage`**:
   - Rather than routing to completely distinct HTML URLs, all personas route to `AccessPage` (`#application-app-preview-component---AccessPage`).
   - The embedded view `approverSectionView` (`kyra001.pages.Approver.Approver`) is conditionally mounted and displayed only when `activeRole === 'Approver'` or `activeRole === 'Compliance Review'`.
   - For `Requester`, `approverSectionView` is completely hidden, exposing the `myAccessTable` entitlements table and action cards.

3. **Title and Tab Conditional Logic between Approver and Compliance Review**:
   - When logged in as `Approver`, the embedded approver card title displays `"User Requests"`, and both `"Access"` and `"Revoke"` queue buttons are visible.
   - When logged in as `Compliance Review`, the card title dynamically updates to `"Compliance Review Requests"`, and the `"Revoke"` tab is hidden (`visible="{= !${accessModel>/isCompliance} }"`).

4. **Sign Out Modal Dialog**:
   - Clicking "Sign Out" in the header opens a custom HTML card inside a `sap.m.Dialog`.
   - The confirm button is `#kyra_signout_confirm_btn` ("Yes, Sign Out").
   - Upon confirmation, `_performLogout()` clears session data and navigates back to the Login view (`TargetLogin`), resetting the form fields.

5. **In-Page Add Access Wizard (`addAccessSectionContainer`)**:
   - Clicking the "Add Access" card (`#application-app-preview-component---AccessPage--cardAddAccess`) reveals the multi-step access wizard directly within `AccessPage`.
   - **Step 1: Enterprise Scope Selection**: Contains two `sap.m.ComboBox` controls: Business Sector (`inPageBusinessSectorSelect`) and Business Function (`inPageBusinessFunctionSelect`). Functions are dynamically loaded based on sector selection.
   - **Step 2: Region Selection**: Contains the embedded SVG/Image world map (`#mapWrapper`), region pin layer, and selected regions summary chips. Navigating between Step 1 and Step 2 is controlled by the "Next" (`onGoToAddAccessStep2`) and "Previous" (`onGoToAddAccessStep1`) buttons.

6. **Unsaved Changes Dialog (`KyraDialog.js`)**:
   - When an access request is in progress and the user clicks "Cancel", `_confirmDiscardAddAccess()` intercepts the event and displays the global `window.KyraDialog` modal dialog (`#kyra_dialog_overlay`).
   - Clicking "Stay on Page" (`#kyra_dialog_cancel_btn`) aborts the cancellation and preserves the in-progress wizard state on the Enterprise Scope Selection screen.
   - Clicking "Proceed & Discard" (`#kyra_dialog_confirm_btn`) clears the model state and closes the wizard.