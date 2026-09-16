# KYRA Automation Testing — UI Selector Reference Guide

This document catalogs all verified UI selectors, components, and interactive elements discovered from the KYRA application source and rendered DOM.

---

## 1. Login Page (`Login.view.xml`)

| Page | Element | Selector | Element Type | Purpose | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Login** | Persona Select Container | `#application-app-preview-component---Login--roleSelect` | `div.sapMSlt` | Visible SAPUI5 Select control | Click to open the persona dropdown menu. |
| **Login** | Persona Label Display | `#application-app-preview-component---Login--roleSelect-label` | `label.sapMSltLabel` | Visible text label of the selected persona | Used to verify currently selected persona (e.g. "Requester", "Approver", "Compliance Review"). |
| **Login** | Persona Arrow Button | `#application-app-preview-component---Login--roleSelect-arrow` | `span.sapMSltArrow` | Dropdown arrow indicator | Visual toggle indicator. |
| **Login** | Persona Hidden Select (Avoid) | `#application-app-preview-component---Login--roleSelect-hiddenSelect` | `select` | Hidden accessibility element | **DO NOT CLICK** — internal SAPUI5 element. |
| **Login** | Persona Popup Option | `li[role='option']` | `li.sapMSelectItem` | Individual selectable persona option in popup | Filter by text: "Requester", "Approver", "Compliance Review". |
| **Login** | Employee ID Input Wrapper | `#application-app-preview-component---Login--idInput` | `div.sapMInput` | SAPUI5 Input control wrapper | Outer container for user ID field. |
| **Login** | Employee ID Input Inner | `#application-app-preview-component---Login--idInput-inner` | `input.sapMInputBaseInner` | Interactive native input field | Enter employee ID (e.g. `emp010`, `emp085`, `emp095`). |
| **Login** | Sign In Button | `#application-app-preview-component---Login--signInButton` | `button.sapMBtn` | Submission action button | Type `Emphasized`. Click to authenticate. |
| **Login** | Error Banner Container | `#application-app-preview-component---Login--kyraLoginErrorBanner` | `div.kyraCustomErrorBanner` | Error message display box | Visible when authentication fails. |
| **Login** | Error Close Button | `button.kyraErrorCloseBtn` | `button` | Error banner dismiss button | Dismisses error banner. |

---

## 2. ShellBar & Common Portal Header (`AccessPage.view.xml`)

| Page | Element | Selector | Element Type | Purpose | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Portal Header** | ShellBar Container | `header.sapFShellBar` | `f:ShellBar` | Main enterprise top navigation bar | Displays title "KYRA Enterprise", subtitle "User Access Management Portal". |
| **Portal Header** | Profile Avatar | `#application-app-preview-component---AccessPage--kyraHeaderProfileAvatar` | `span.sapFAvatar` | Current user profile icon | Displays active user ID tooltip. |
| **Portal Header** | Notification Bell Button | `#application-app-preview-component---AccessPage--kyraHeaderBellBtn` | `button.kyraHeaderBellBtn` | Notification toggle button | Displays unread notification count badge. |
| **Portal Header** | Sign Out Button | `button.kyraSignOutHeaderBtn` | `button.sapMBtn` | Session termination trigger | Visible in header additional content. Triggers Sign Out dialog. |
| **Welcome Banner**| Banner Title | `h2.fioriWelcomeTitle` | `Title` | Dashboard main welcome headline | Text: "Enterprise Identity & Access Governance". |
| **Welcome Banner**| Active Persona Pill | `.kyraBannerPersonaRow .kyraHeaderRolePill` | `span.kyraHeaderRolePill` | Persona verification badge | Displays current active role ("Requester", "Approver", or "Compliance Review"). |
| **Welcome Banner**| Active User ID Value | `.kyraHeaderUserIdPill .kyraUserIdValue` | `span.kyraUserIdValue` | User identity badge | Displays logged-in employee ID. |
| **Welcome Banner**| Refresh Button | `#application-app-preview-component---AccessPage--fioriHeaderRefreshBtn` | `button.kyraBannerRefreshBtn` | Data refresh button | Triggers re-fetch of entitlements/requests. |

---

## 3. Requester Page Specific Elements (`AccessPage.view.xml`)

| Page | Element | Selector | Element Type | Purpose | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Requester** | Section Toggle Bar | `div.kyraSectionToggleBar` | `HBox` | Navigation tabs between Access and History | Contains "My Access" and "My History" buttons. |
| **Requester** | My Access Tab Button | `div.kyraSectionToggleBar button:has-text('My Access')` | `Button` | Switch to active entitlements tab | Emphasized type when selected. |
| **Requester** | My History Tab Button | `div.kyraSectionToggleBar button:has-text('My History')` | `Button` | Switch to request history tab | Emphasized type when selected. |
| **Requester** | Active Entitlements Title | `h4.fioriCardHeaderTitle:has-text('Active Entitlements')` | `Title` | Entitlements section title | Displays "Active Entitlements (X)". |
| **Requester** | Entitlements Search Field | `input.kyraEntitlementsSearchField` / `input[placeholder*='Search entitlements']` | `SearchField` | Quick filter for user entitlements | Filters table items live. |
| **Requester** | View All Button | `button.kyraViewAllBtn` | `Button` | Full master view toggle | Opens master entitlements table. |
| **Requester** | Active Entitlements Table | `#application-app-preview-component---AccessPage--myAccessTable` | `Table` | Table listing user's granted roles | Columns: SYSTEM NAME, SERVICES, TEAM, PERSONA, EXPIRY DATE, STATUS, ACTION. |
| **Requester** | Pending Requests Card | `#application-app-preview-component---AccessPage--cardPendingRequests` | `VBox.fioriCardPending` | Action card to view pending requests | Clickable KPI card. |
| **Requester** | Add Access Card | `#application-app-preview-component---AccessPage--cardAddAccess` | `VBox.fioriCardAdd` | Action card to launch access request wizard | Clickable action card. |
| **Requester** | Remove Access Card | `#application-app-preview-component---AccessPage--cardRemoveAccess` | `VBox.fioriCardRemove` | Action card to launch revocation flow | Clickable action card. |

---

## 4. Approver Page Specific Elements (`Approver.view.xml`)

| Page | Element | Selector | Element Type | Purpose | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Approver** | Approver Card Container | `#application-app-preview-component---AccessPage--approverSectionView` | `mvc:XMLView` | Embedded Approver section container | Visible only for Approver / Compliance Review personas. |
| **Approver** | Approver Section Title | `.fioriApproverTitle` | `Title` | Section main heading | Displays "User Requests" for Approver persona. |
| **Approver** | Pending Queue Button | `.kyraApproverPillGroup button:has-text('Pending')` | `Button` | Filter for pending approval requests | Emphasized when pending queue is active. |
| **Approver** | History Log Button | `.kyraApproverPillGroup button:has-text('History')` | `Button` | Filter for processed approval history | Emphasized when history view is active. |
| **Approver** | Access Requests Tab | `.kyraAccessRevokeBar button:has-text('Access')` | `Button` | Switch to pending access requests table | Displays count `Access (X)`. |
| **Approver** | Revoke Requests Tab | `.kyraAccessRevokeBar button:has-text('Revoke')` | `Button` | Switch to pending revocation requests table | Visible for Approver; displays count `Revoke (X)`. |
| **Approver** | Approval Requests Table | `#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable` | `Table` | Table of pending access approval requests | Columns: USER ID, BUSINESS SECTOR, BUSINESS FUNCTION, DURATION, SUBMISSION DATE, APPROVAL STATUS. |
| **Approver** | Approval Filter Button | `button.kyraApproverFilterBtn` | `Button` | Filter dialog trigger | Opens filter modal. |
| **Approver** | Approval Export Button | `button.kyraApproverExportBtn` | `Button` | Excel export trigger | Exports table rows. |

---

## 5. Compliance Review Page Specific Elements (`Approver.view.xml`)

| Page | Element | Selector | Element Type | Purpose | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Compliance** | Section Title | `.fioriApproverTitle` | `Title` | Section main heading | Specifically displays "Compliance Review Requests" (distinct from Approver). |
| **Compliance** | Subtitle Text | `.kyraSubtitleWithTealBar` | `Text` | Description of SoD compliance queue | Text: "Review access requests requiring Segregation of Duties (SoD) compliance clearance.". |
| **Compliance** | Access Tab | `.kyraAccessRevokeBar button:has-text('Access')` | `Button` | Switch to access requests queue | Displays pending compliance requests count. |
| **Compliance** | Revoke Tab (Hidden) | `.kyraAccessRevokeBar button:has-text('Revoke')` | `Button` | Revocation queue toggle | **Hidden** for Compliance persona (`visible="{= !${accessModel>/isCompliance} }"`). |
| **Compliance** | Compliance Table | `#application-app-preview-component---AccessPage--approverSectionView--approvalAccessTable` | `Table` | Table of compliance clearance requests | Lists requests requiring SoD verification. |

---

## 6. Sign Out Modal Dialog (`AccessPage.controller.js`)

| Page | Element | Selector | Element Type | Purpose | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Logout Dialog** | Dialog Container | `div.kyraSignOutModernDialog` / `div.kyra-signout-card` | `Dialog` | Modal dialog container | Opened on clicking header "Sign Out". |
| **Logout Dialog** | Security Notice Badge | `div.kyra-signout-badge` | `div` | Notice indicator | Text: "SECURITY NOTICE". |
| **Logout Dialog** | Dialog Main Title | `h3.kyra-signout-main-title` | `h3` | Confirmation title | Text: "Sign Out of KYRA Portal?". |
| **Logout Dialog** | Cancel Button | `#kyra_signout_cancel_btn` | `button` | Dismiss sign-out and stay logged in | Text: "Stay Signed In". |
| **Logout Dialog** | Confirm Sign Out Button | `#kyra_signout_confirm_btn` | `button` | Confirm session termination | Text: "Yes, Sign Out". Clears session and routes back to Login. |
| **Logout Dialog** | Close "X" Button | `#kyra_signout_cancel_x` | `button` | Alternative modal dismiss button | Top-right close icon. |
