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
| **Requester** | Add Access Card | `#application-app-preview-component---AccessPage--cardAddAccess` | `VBox.fioriCardAdd` | Action card to launch access request wizard | Clickable action card. Text: "Add Access", "Request new access", "Create Request". |
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

---

## 7. Enterprise Scope Selection — Step 1 (`AccessPage.view.xml`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Add Access** | Wizard Container | — | `[id$='addAccessSectionContainer']` | `VBox` | Main container for Add Access wizard | Unique SAPUI5 container ID | Toggled visible by clicking `cardAddAccess`. |
| **Enterprise Scope** | Scope Title | `Enterprise Scope Selection` | `.kyraScopeTitle`, `:text('Enterprise Scope Selection')` | `Title` | Section main heading | Semantic class and text | Confirms Step 1 is rendered. |
| **Enterprise Scope** | Step 1 Progress Node | `1. Business Sector` / `In Progress` | `.kyraStepNode:has-text('1. Business Sector')` | `VBox` | Progress tracker step indicator | Active step indicator | Confirms active wizard stage. |
| **Enterprise Scope** | Business Sector Input | `Select Business Sector...` | `[id$='inPageBusinessSectorSelect-inner']` | `ComboBox input` | Text display and input for sector | Inner input of SAPUI5 ComboBox | Contains selected sector text value. |
| **Enterprise Scope** | Business Sector Arrow | — | `[id$='inPageBusinessSectorSelect-arrow']` | `span` | Dropdown open arrow | Standard SAPUI5 ComboBox arrow suffix | Click opens the sector options list. |
| **Enterprise Scope** | Sector Options | `Finance & Enterprise Performance`, etc. | `.sapMPopover:visible li[role='option']` | `li` | Sector dropdown options | Accessible role + text matching | Target: `Finance & Enterprise Performance`. |
| **Enterprise Scope** | Business Function Input | `Select Business Function...` | `[id$='inPageBusinessFunctionSelect-inner']` | `ComboBox input` | Text display and input for function | Inner input of SAPUI5 ComboBox | Populated after Sector is chosen. |
| **Enterprise Scope** | Business Function Arrow | — | `[id$='inPageBusinessFunctionSelect-arrow']` | `span` | Dropdown open arrow | Standard SAPUI5 ComboBox arrow suffix | Click opens available functions. |
| **Enterprise Scope** | Function Options | `Corporate Accounting`, etc. | `.sapMPopover:visible li[role='option']` | `li` | Function dropdown options | Accessible role + text matching | Target: `Corporate Accounting`. |
| **Enterprise Scope** | Next Button | `Next` | `[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next')` | `button` | Proceed to Region Selection | Emphasized primary button in Step 1 footer | Advances wizard to Step 2 (`addAccessStep=2`). |
| **Enterprise Scope** | Cancel Button | `Cancel` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel')` | `button` | Discard request trigger | Secondary button on bottom-left of Step 1 | Triggers `onCloseAddAccessSector` and opens confirmation dialog. |

---

## 8. Region Selection — Step 2 (`AccessPage.view.xml`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Region Selection** | Header Title | `Step 2: Region Selection` | `.fioriCardHeaderTitle:has-text('Step 2: Region Selection')` | `Title` | Step 2 section main heading | Semantic header text | Evaluated dynamically when `addAccessStep === 2`. |
| **Region Selection** | World Map Container | — | `#mapWrapper`, `#worldMapImgAccessPage` | `div / img` | Interactive SVG/Image world map | Unique IDs in embedded map HTML | Confirms Step 2 map is displayed. |
| **Region Selection** | Select All Regions Button | `Select All Regions` | `#selectAllBtn`, `.select-all-btn-wrapper:has-text('Select All Regions')` | `div.select-all-btn-wrapper` | Selects all 7 operating regions at once | Unique ID `#selectAllBtn` | Embedded inside `#mapWrapper`. Sets `mapSelectedRegions` in model. |
| **Region Selection** | Selected Regions Header | `SELECTED REGIONS` | `.selected-regions-header`, `:text('SELECTED REGIONS')` | `Text` | Summary card heading | Stable CSS class and visible text | Confirms Step 2 selection container. |
| **Region Selection** | No Regions Selected Text | `No regions selected` | `.no-selection-text`, `:text('No regions selected')` | `Text` | Empty state indicator | Visible when `!hasMapRegionSelection` | Inverted after clicking Select All Regions. |
| **Region Selection** | Selected Region Chips | `North America ✕`, `Europe ✕`, etc. | `#selectedChipsList button.region-chip-btn`, `#selectedChipsList button` | `Button` | Selected region removable chip buttons | Scoped inside `#selectedChipsList` | Populated dynamically with all 7 regions when Select All is clicked. |
| **Region Selection** | Step 2 Progress Node | `2. Region Selection` | `.kyraStepNode.kyraStepActive:has-text('2. Region Selection')` | `VBox` | Progress tracker step indicator | Active step indicator | Confirms active Step 2 stage. |
| **Region Selection** | Previous Button | `Previous` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible` | `button` | Return to Step 1 Enterprise Scope | Stable text and container scoping | Calls `onGoToAddAccessStep1`, restoring Step 1. |
| **Region Selection** | Next Button | `Next` | `[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible` | `button` | Proceed to Step 3 Access Configuration | Primary button in Step 2 footer | Calls `onGoToAddAccessStep3`, validating region selection. |
| **Region Selection** | Cancel Button | `Cancel` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible` | `button` | Cancel wizard / trigger discard dialog | Secondary button in Step 2 footer | Calls `onCloseAddAccessSector`, triggering Unsaved Changes dialog. |

---

## 9. Access Configuration — Step 3 (`AccessPage.view.xml`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Access Configuration** | Header Title | `Step 3: Access Configuration` | `.fioriCardHeaderTitle:has-text('Step 3: Access Configuration')` | `Title` | Step 3 section main heading | Semantic header text | Evaluated dynamically when `addAccessStep === 3`. |
| **Access Configuration** | Step 3 Progress Node | `3. Access Configuration` | `.kyraStepNode.kyraStepActive:has-text('3. Access Configuration')` | `VBox` | Progress tracker step indicator | Active step indicator | Confirms active Step 3 stage. |
| **Access Configuration** | Target Systems Title | `Target Systems & Process Selection` | `:text('Target Systems & Process Selection')` | `core:Title` | Sub-step form title | Distinct visible section title | Confirms Sub-step 3.1 is rendered. |
| **Access Configuration** | Target Systems MultiSelect | `Select target system processes...` | `[id$='inPageSystemsMultiSelect'], #application-app-preview-component---AccessPage--inPageSystemsMultiSelect` | `MultiComboBox` | Select target IT systems & processes | Unique SAPUI5 control ID | Multi-selection dropdown for target systems. |
| **Access Configuration** | Target System Option | `SAP BTP Cloud Platform` | `.sapMPopover:visible li:has-text('SAP BTP Cloud Platform')` | `li` | Target System item selection | Visible text matching in open popover | Populates system slide card upon selection. |
| **Access Configuration** | Service / Topic MultiSelect | `Select service topics for this system...` | `[id$='inPageServicesMultiSelect'], #application-app-preview-component---AccessPage--inPageServicesMultiSelect` | `MultiComboBox` | Select services/topics for system | Unique SAPUI5 control ID | Options: System Administrator, System Owners, Stakeholders. |
| **Access Configuration** | Service / Topic Option | `System Administrator` | `.sapMPopover:visible li:has-text('System Administrator')` | `li` | Service topic item selection | Visible text matching in open popover | Triggers population of Team Roles list. |
| **Access Configuration** | Team Role MultiSelect | `Select team roles for this system...` | `[id$='inPageTeamMultiSelect'], #application-app-preview-component---AccessPage--inPageTeamMultiSelect` | `MultiComboBox` | Select functional team roles | Unique SAPUI5 control ID | Enabled when Service / Topic is selected. |
| **Access Configuration** | Team Role Option | `IT Developers (System Administrator)` | `.sapMPopover:visible li:has-text('IT Developers (System Administrator)')` | `li` | Team Role item selection | Visible text matching in open popover | Triggers population of Assigned Personas list. |
| **Access Configuration** | Assigned Persona MultiSelect | `Select personas for this system...` | `[id$='inPagePersonaMultiSelect'], #application-app-preview-component---AccessPage--inPagePersonaMultiSelect` | `MultiComboBox` | Select assigned persona entitlements | Unique SAPUI5 control ID | Enabled when Team Role is selected. |
| **Access Configuration** | Assigned Persona Option | `Frontend & UI Developer Persona (IT Developers)` | `.sapMPopover:visible li:has-text('Frontend & UI Developer Persona (IT Developers)')` | `li` | Persona item selection | Visible text matching in open popover | Minimum valid persona entitlement for UI. |
| **Access Configuration** | Slide 1 Next Button | `Next` | `[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible` | `button` | Advance from Slide 1 to Sub-step 3.2 | Scoped primary button | Calls `onStep3Slide1Continue`, sets `addAccessConfigSubStep = 2`. |
| **Access Configuration** | Access Duration Input | `Select Access Duration...` | `[id$='inPageDurationSelect-inner']` | `ComboBox input` | Display and choose access duration | Inner input of SAPUI5 ComboBox | Contains selected duration text value. |
| **Access Configuration** | Access Duration Option | `30 Days (Temporary)` | `.sapMPopover:visible li:has-text('30 Days (Temporary)')` | `li` | Temporary duration item selection | Visible text matching in open popover | Options: Permanent (Default), 30 Days (Temporary), 90 Days (Project). |
| **Access Configuration** | Business Justification Textarea | `Provide detailed business justification...` | `[id$='inPageJustificationArea'] textarea` | `TextArea` | Enter justification text | Unique SAPUI5 textarea control | Accepts business rationale string (e.g. `TEST`). |
| **Access Configuration** | Previous Button | `Previous` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible` | `button` | Return to previous slide/step | Scoped secondary button in Step 3 footer | On Slide 1: calls `onStep3Slide1Previous`; on Slide 2: calls `onBackToSystemSlides`. |
| **Access Configuration** | Cancel Button | `Cancel` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible` | `button` | Cancel wizard trigger | Scoped secondary button in Step 3 footer | Calls `onCloseAddAccessSector`, opening Unsaved Changes dialog. |
| **Access Configuration** | Slide 2 Next Button | `Next` | `[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible` | `button` | Advance from Step 3 to Step 4 | Emphasized primary button in Step 3 footer | Calls `onGoToAddAccessStep4`, setting `addAccessStep = 4`. |

---

## 10. Access Validation — Step 4 (`AccessPage.view.xml`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Access Validation** | Header Title | `Step 4: Access Validation` | `.fioriCardHeaderTitle:has-text('Step 4: Access Validation')` | `Title` | Step 4 section main heading | Semantic header text | Evaluated dynamically when `addAccessStep === 4`. |
| **Access Validation** | Step 4 Progress Node | `4. Access Validation` | `.kyraStepNode.kyraStepActive:has-text('4. Access Validation')` | `VBox` | Progress tracker step indicator | Active step indicator | Confirms active Step 4 stage. |
| **Access Validation** | Threshold Limits Card | `Threshold Limits` | `.kyraValCardTitle:has-text('Threshold Limits')` | `Title` | Threshold limits validation section | Semantic card title class | Shows badge `0 Issues` or `No Threshold Limits Exceeded`. |
| **Access Validation** | Restricted Records Card | `Restricted Records` | `.kyraValCardTitle:has-text('Restricted Records')` | `Title` | Restricted records validation section | Semantic card title class | Displays table with restricted access classification details. |
| **Access Validation** | Duplicate Roles Card | `Duplicate Roles` | `.kyraValCardTitle:has-text('Duplicate Roles')` | `Title` | Duplicate roles validation section | Semantic card title class | Shows badge `0 Duplicates` or `No Duplicate Roles Detected`. |
| **Access Validation** | Previous Button | `Previous` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible` | `button` | Return to Step 3 Access Configuration | Scoped secondary button in Step 4 footer | Calls `onGoBackToDurationSlide`, setting `addAccessStep = 3` and `addAccessConfigSubStep = 2`. |

---

## 11. Unsaved Changes Dialog (`KyraDialog.js`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Dialog** | Overlay Container | — | `#kyra_dialog_overlay` | `div` | Fullscreen modal backdrop | Unique ID in `KyraDialog.js` | Appended to `document.body` with `z-index: 2000000`. |
| **Dialog** | Dialog Card | — | `.kyraDialogCard`, `#kyra_dialog_overlay > div` | `div` | Central modal card | Stable CSS class | Contains header, body message, and footer actions. |
| **Dialog** | Dialog Title | `Unsaved Changes` | `#kyra_dialog_overlay h3`, `.kyraDialogCard h3` | `h3` | Warning heading | Semantic header tag inside overlay | Displays "Unsaved Changes" title. |
| **Dialog** | Dialog Message | `If you navigate to another section...` | `#kyra_dialog_overlay div:has-text('in-progress access request')` | `div` | Discard warning message | Scoped text matching | Informs user that in-progress request will be lost. |
| **Dialog** | Stay on Page Button | `Stay on Page` | `#kyra_dialog_cancel_btn`, `button:has-text('Stay on Page')` | `button` | Abort discard and remain on page | Unique ID `#kyra_dialog_cancel_btn` | Dismisses dialog without clearing state. |
| **Dialog** | Proceed & Discard Button | `Proceed & Discard` | `#kyra_dialog_confirm_btn`, `button:has-text('Proceed & Discard')` | `button` | Confirm discard and close wizard | Unique ID `#kyra_dialog_confirm_btn` | Dismisses dialog and resets wizard state. |
| **Dialog** | Close "X" Button | `✕` | `#kyra_dialog_close_btn` | `button` | Top-right close icon | Unique ID `#kyra_dialog_close_btn` | Dismisses dialog. |

---

## 12. Review & Summary — Step 5 (`AccessPage.view.xml`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Review & Summary** | Header Title | `Step 5: Review & Summary` | `.fioriCardHeaderTitle:has-text('Step 5: Review & Summary')` | `Title` | Step 5 section main heading | Semantic header text | Evaluated dynamically when `addAccessStep === 5`. |
| **Review & Summary** | Step 5 Progress Node | `5. Review & Summary` | `.kyraStepNode.kyraStepActive:has-text('5. Review & Summary')` | `VBox` | Progress tracker step indicator | Active step indicator | Confirms active Step 5 stage. |
| **Review & Summary** | Top Details Card | — | `.kyraSummaryTopDetailsCard` | `VBox` | Container for governance metadata | Unique CSS class | Contains requester ID, sector, function, region, duration, justification. |
| **Review & Summary** | Requester ID Value | `emp010` | `.kyraSummaryDetailItem:has-text('REQUESTER ID') .kyraSummaryDetailValue`, `.kyraRequesterIdHighlight` | `Text` | Displayed employee ID | Scoped item value | Matches logged-in user. |
| **Review & Summary** | Business Sector Value | `Finance & Enterprise Performance` | `.kyraSummaryDetailItem:has-text('BUSINESS SECTOR') .kyraSummaryDetailValue` | `Text` | Displayed sector | Scoped item value | Matches Step 1 selection. |
| **Review & Summary** | Business Function Value | `Corporate Accounting` | `.kyraSummaryDetailItem:has-text('BUSINESS FUNCTION') .kyraSummaryDetailValue` | `Text` | Displayed function | Scoped item value | Matches Step 1 selection. |
| **Review & Summary** | Operating Region Value | `All Regions` / `Europe` | `.kyraSummaryDetailItem:has-text('OPERATING REGION') .kyraSummaryDetailValue` | `Text` | Displayed operating region | Scoped item value | Matches Step 2 selection. |
| **Review & Summary** | Access Duration Value | `30 Days (Temporary)` | `.kyraSummaryDetailItem:has-text('ACCESS DURATION') .kyraSummaryDetailValue` | `Text` | Displayed access duration | Scoped item value | Matches Step 3 duration selection. |
| **Review & Summary** | Business Justification Text | `TEST` | `.kyraSummaryDetailJustification` | `Text` | Displayed justification text | Unique CSS class | Matches Step 3 justification textarea. |
| **Review & Summary** | Target System Card Container | — | `.kyraSummaryCardContainer` | `VBox` | Container for each target system's table | Unique CSS class | Iterated via `addAccessSummaryTables`. |
| **Review & Summary** | Target System Card Title | `Target System 1: ...` | `.kyraSummaryCardTitle` | `Title` | Heading for system card | Unique CSS class | Contains system number and system name. |
| **Review & Summary** | Target System Edit Button | `Edit` | `button.kyraSummaryEditBtn:has-text('Edit')` | `button` | Enter inline edit mode for system | Unique CSS class | Sets `isEditingFromSummary=true` and navigates to Step 3. |
| **Review & Summary** | Edit Mode OK Button | `OK` | `[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('OK'):visible` | `button` | Save configuration changes | Scoped primary button | Calls `onSaveAndReturnToSummary`, re-evaluates validation, and returns to Step 5. |
| **Review & Summary** | Previous Button | `Previous` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible` | `button` | Return to Step 4 Access Validation | Scoped secondary button | Calls `onNavBackFromStep5`, sets `addAccessStep = 4`. |
| **Review & Summary** | Cancel Request Button | `Cancel Request` | `[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel Request'):visible` | `button` | Discard request trigger | Scoped secondary button | Calls `onCloseAddAccessSector`, opening Unsaved Changes dialog. |
| **Review & Summary** | Submit Access Request Button | `Submit Access Request` | `[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Submit Access Request'):visible` | `button` | Final access submission action | Scoped primary button | Calls `onFinalSubmitInPageAddAccess` to persist to database. |

---

## 13. Submission Modal Dialog (`KyraDialog.js` / `AccessPage.controller.js`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Submission Dialog** | Overlay Container | — | `#kyra_dialog_overlay` | `div` | Fullscreen modal backdrop | Unique ID in `KyraDialog.js` | Appended to `document.body` with `z-index: 2000000`. |
| **Submission Dialog** | Dialog Card | — | `.kyraDialogCard`, `#kyra_dialog_overlay > div` | `div` | Central modal card container | Stable CSS class | Contains submission summary and actions. |
| **Submission Dialog** | Dialog Title | `Access Request Submitted` | `#kyra_dialog_overlay h3` | `h3` | Confirmation heading | Scoped heading selector | Confirms successful request creation. |
| **Submission Dialog** | Submitted Badge | `Submitted X` | `#kyra_dialog_overlay span:has-text('Submitted')` | `span` | Submitted items count badge | Text matching | Indicates number of persisted requests. |
| **Submission Dialog** | Scrollable Container | — | `.kyra-dialog-scroll-container` | `div` | Scroll container for submitted/excluded items | Unique CSS class | Contains item cards with request IDs. |
| **Submission Dialog** | Done Button | `Done` | `#kyra_dialog_confirm_btn`, `button:has-text('Done')` | `button` | Close dialog and return to dashboard | Unique ID `#kyra_dialog_confirm_btn` | Dismisses modal, resets wizard, and returns to Requester dashboard. |
| **Submission Dialog** | Close "X" Button | `✕` | `#kyra_dialog_close_btn` | `button` | Alternative modal dismiss button | Unique ID `#kyra_dialog_close_btn` | Dismisses modal and resets wizard. |

---

## 14. Approver & Compliance Request Detail (`ApproverDetail.view.xml`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Request Detail** | Detail Page Container | — | `[id$='approverDetailPage']`, `.kyraGovOverviewCard`, `.kyraGovPageContainer` | `Page` / `VBox` | Main container for request governance review | Scoped element ID & CSS | Loaded upon clicking a user request row in `approvalAccessTable`. |
| **Request Detail** | Detail Header Title | `Access Request Governance Review` | `.kyraGovHeaderTitle:has-text('Access Request Governance Review')` | `Title` | Governance review headline | Stable CSS class | Indicates active detail review stage. |
| **Request Detail** | Requester ID Value | `emp029` | `.kyraGovBannerReqNum` | `Title` | Displayed employee ID under User ID | Stable CSS class | Displays target requester ID. |
| **Request Detail** | Business Sector Value | `Finance & Enterprise Performance` | `.kyraGovRequesterCol:nth-child(1) .kyraGovInfoValue` | `Text` | Displayed business sector | Column hierarchy | Verifies sector requested. |
| **Request Detail** | Business Function Value | `Corporate Accounting` | `.kyraGovRequesterCol:nth-child(2) .kyraGovInfoValue` | `Text` | Displayed business function | Column hierarchy | Verifies function requested. |
| **Request Detail** | Duration Value | `30 Days (Temporary)` | `.kyraGovRequesterCol:nth-child(4) .kyraGovInfoValue` | `Text` | Displayed access duration | Column hierarchy | Verifies duration requested. |
| **Request Detail** | Justification Value | `TEST` | `.sapUiMediumMarginTop .kyraGovInfoValue` | `Text` | Displayed justification text | Scoped value | Matches business justification entered by Requester. |
| **Request Detail** | Back Button | `Back` | `button.kyraBackTealBtn`, `button:has-text('Back')` | `Button` | Navigate back to Approver/Compliance Dashboard | Unique CSS class | Calls `onCloseRequestSummaryView`. |
| **Request Detail** | Entitlement Data Row | — | `.kyraGovDataRowWrapper`, `.kyraGovDataRow` | `VBox` / `HBox` | Row container for individual entitlement | Stable CSS class | Contains system, service, role, persona, remarks, decision. |
| **Request Detail** | Individual Approve Button | — | `button.kyraGovApproveBtn`, `button[title*='Approve']` | `Button` | Approve individual entitlement row | Unique CSS class | Sets status to Approved (`Success`). |
| **Request Detail** | Individual Reject Button | — | `button.kyraGovRejectBtn`, `button[title*='Reject']` | `Button` | Reject individual entitlement row | Unique CSS class | Sets status to Rejected (`Error`). |
| **Request Detail** | Row Remark Input | — | `.kyraGovRemarkInput input`, `input[placeholder*='remark']` | `Input` | Comment/remark input field | Scoped input control | Triggers `onRemarkLiveChange` to persist row comment. |
| **Request Detail** | Batch Approve All Button | `Approve All` | `button.kyraBatchApproveBtn`, `button:has-text('Approve All')` | `Button` | Trigger batch approval for all entitlements | Unique CSS class | Opens `BatchDecisionDialog` modal. |
| **Request Detail** | Batch Reject All Button | `Reject All` | `button.kyraBatchRejectBtn`, `button:has-text('Reject All')` | `Button` | Trigger batch rejection for all entitlements | Unique CSS class | Opens `BatchDecisionDialog` modal. |
| **Request Detail** | Submit Decision Button | `Submit Decision` | `.kyraGovBottomRightBtns button:has-text('Submit Decision')`, `button.kyraPrimaryBtn:has-text('Submit Decision')` | `Button` | Finalize decisions and open summary breakdown | Scoped primary button | Calls `onApproveSelectedRequest`. |

---

## 15. Batch Operations & Decision Breakdown Dialogs (`ApproverDetail.controller.js` & `KyraDialog.js`)

| Page | Element | Visible Text | Selector | Element Type | Purpose | Why Selected | SAPUI5 Notes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Batch Dialog** | Dialog Container | — | `.kyraBatchDecisionDialog`, `.sapMDialog:has(.kyraBatchDialogTitle)` | `Dialog` | Modal dialog for batch approval/rejection | Unique CSS class | Opened via `_showBatchDecisionRemarkDialog`. |
| **Batch Dialog** | Title | `Approve All Entitlements` / `Reject All Entitlements` | `.kyraBatchDialogTitle` | `Title` | Batch modal heading | Stable CSS class | Indicates operation mode (Approve All or Reject All). |
| **Batch Dialog** | Remark Textarea | — | `.kyraBatchRemarkTextArea textarea`, `.kyraBatchDecisionDialog textarea` | `TextArea` | Input field for batch decision justification | Unique CSS class | Mandatory remark before submission. |
| **Batch Dialog** | Approve All Confirm Button | `Approve All` | `.kyraBatchDialogConfirmApproveBtn`, `button:has-text('Approve All')` | `Button` | Confirm batch approval across all entitlements | Unique CSS class | Applies approval status to all rows. |
| **Batch Dialog** | Reject All Confirm Button | `Reject All` | `.kyraBatchDialogConfirmRejectBtn`, `button:has-text('Reject All')` | `Button` | Confirm batch rejection across all entitlements | Unique CSS class | Applies rejection status to all rows. |
| **Batch Dialog** | Cancel Button | `Cancel` | `.kyraBatchDialogCancelBtn`, `.kyraBatchDecisionDialog button:has-text('Cancel')` | `Button` | Dismiss batch dialog | Unique CSS class | Closes dialog without changes. |
| **Breakdown Dialog** | Overlay Container | — | `#kyra_dialog_overlay` | `div` | Fullscreen backdrop for Decision Breakdown Summary | Unique ID `#kyra_dialog_overlay` | Generated via `KyraDialog.show`. |
| **Breakdown Dialog** | Title | `Decision Breakdown Summary` | `#kyra_dialog_overlay h3:has-text('Decision Breakdown Summary')` | `h3` | Modal summary heading | Scoped heading selector | Confirms final decision review stage. |
| **Breakdown Dialog** | Confirm & Submit Button | `Confirm & Submit` | `#kyra_dialog_confirm_btn`, `button:has-text('Confirm & Submit')` | `button` | Final action to persist approval/rejection decisions | Unique ID `#kyra_dialog_confirm_btn` | Executes `_executeFinalSubmission` to update database and audit log. |
| **Breakdown Dialog** | Back Button | `Back` | `#kyra_dialog_cancel_btn`, `button:has-text('Back')` | `button` | Return to request detail without persisting | Unique ID `#kyra_dialog_cancel_btn` | Closes breakdown dialog. |

---

## 16. Multi-Tab Same-Browser Architecture & Isolation

| Session | Role | User ID | Tab Index | Session Isolation Strategy | Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Tab 1** | Requester | `emp029` | 0 (`context.pages().get(0)`) | `sessionStorage` per-tab storage | Remains open on Requester Dashboard after access request submission. |
| **Tab 2** | Approver | `emp085` | 1 (`context.pages().get(1)`) | `context.clearCookies()` before tab navigation | New tab in same browser window; logs in as `emp085`; processes request; remains on dashboard. |
| **Tab 3** | Compliance Review | `emp095` | 2 (`context.pages().get(2)`) | `context.clearCookies()` before tab navigation | New tab in same browser window; logs in as `emp095`; processes request; remains on dashboard. |
| **All Tabs** | Multi-Session | — | 3 Tabs Total | No application logout (`signOut()` avoided) | All 3 sessions remain active simultaneously in the same browser window. |


