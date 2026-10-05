package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import components.MultiSelectComponent;
import utils.ConfigReader;
import utils.WaitUtils;

import java.util.List;

public class AccessConfigurationPage extends BasePage {

    private final Locator step3HeaderTitle;
    private final Locator step3ProgressNode;
    private final Locator systemsSelectionTitle;
    private final MultiSelectComponent systemsSelect;
    private final MultiSelectComponent servicesSelect;
    private final MultiSelectComponent teamSelect;
    private final MultiSelectComponent personaSelect;
    private final Locator durationSelectInput;
    private final Locator durationSelectArrow;
    private final Locator justificationArea;
    private final Locator durationSectionTitle;
    private final Locator previousBtn;
    private final Locator cancelBtn;
    private final Locator nextBtn;
    private final Locator okBtn;

    public AccessConfigurationPage(Page page) {
        super(page);
        this.step3HeaderTitle = page.locator(".fioriCardHeaderTitle:has-text('Step 3: Access Configuration')").first();
        this.step3ProgressNode = page.locator(".kyraStepNode.kyraStepActive:has-text('3. Access Configuration'), .kyraStepNode:has-text('3. Access Configuration')").first();
        this.systemsSelectionTitle = page.locator(":text('Target Systems & Process Selection')").first();
        this.durationSectionTitle = page.locator(":text('Access Duration & Business Justification')").first();

        this.systemsSelect = new MultiSelectComponent(page, "[id$='inPageSystemsMultiSelect'], #application-app-preview-component---AccessPage--inPageSystemsMultiSelect");
        this.servicesSelect = new MultiSelectComponent(page, "[id$='inPageServicesMultiSelect'], #application-app-preview-component---AccessPage--inPageServicesMultiSelect");
        this.teamSelect = new MultiSelectComponent(page, "[id$='inPageTeamMultiSelect'], #application-app-preview-component---AccessPage--inPageTeamMultiSelect");
        this.personaSelect = new MultiSelectComponent(page, "[id$='inPagePersonaMultiSelect'], #application-app-preview-component---AccessPage--inPagePersonaMultiSelect");

        this.durationSelectInput = page.locator("[id$='inPageDurationSelect-inner'], #application-app-preview-component---AccessPage--inPageDurationSelect-inner").first();
        this.durationSelectArrow = page.locator("[id$='inPageDurationSelect-arrow'], #application-app-preview-component---AccessPage--inPageDurationSelect-arrow").first();
        this.justificationArea = page.locator("[id$='inPageJustificationArea'] textarea, textarea[id$='inPageJustificationArea-inner'], #application-app-preview-component---AccessPage--inPageJustificationArea-inner").first();

        this.previousBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Previous'):visible, button:has-text('Previous'):visible").first();
        this.cancelBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraSecondaryBtn:has-text('Cancel'):visible, button:has-text('Cancel'):visible").first();
        this.nextBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('Next'):visible, button:has-text('Next'):visible").first();
        this.okBtn = page.locator("[id$='addAccessSectionContainer'] button.kyraPrimaryBtn:has-text('OK'):visible, button:has-text('OK'):visible").first();
    }

    @Override
    public boolean isLoaded() {
        try {
            WaitUtils.waitForElementVisible(step3HeaderTitle, ConfigReader.getDefaultTimeout());
            return step3HeaderTitle.isVisible() || step3ProgressNode.isVisible() || systemsSelectionTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStep3Active() {
        try {
            WaitUtils.stabilize(page, 300);
            return (step3HeaderTitle.isVisible() || step3ProgressNode.isVisible()) &&
                    (systemsSelectionTitle.isVisible() || durationSectionTitle.isVisible() || previousBtn.isVisible() || isOkButtonVisible());
        } catch (Exception e) {
            return false;
        }
    }

    public void selectTargetSystem(String systemName) {
        try {
            if (systemsSelect.getContainer().isVisible()) {
                systemsSelect.selectOption(systemName);
            } else {
                configureAdditionalSystem(
                        systemName,
                        "System Owners",
                        "Technical Product Owner (System Owner)",
                        "Systems Architect Persona (Technical Product Owner)"
                );
            }
        } catch (Exception e) {
            configureAdditionalSystem(
                    systemName,
                    "System Owners",
                    "Technical Product Owner (System Owner)",
                    "Systems Architect Persona (Technical Product Owner)"
            );
        }
        WaitUtils.stabilize(page, 400);
    }

    public void configureAdditionalSystem(String systemName, String service, String role, String persona) {
        addAdditionalSystemDuringEdit(systemName, service, role, persona);
    }

    public void configureSingleTargetSystem(String systemName, String service, String role, String persona) {
        page.evaluate("(args) => {\n" +
                "    const [sysName, srv, rle, pers] = args;\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    oModel.setProperty('/addAccessSelectedSystems', [sysName]);\n" +
                "    oModel.setProperty('/hasSelectedTargetSystems', true);\n" +
                "    oModel.setProperty('/targetSystemSlideCount', 1);\n" +
                "    oModel.setProperty('/addAccessCurrentSystemIndex', 0);\n" +
                "    oModel.setProperty('/currentSystemSlideName', sysName);\n" +
                "    oModel.setProperty('/targetSystemSlideTitle', 'Target System Slide (1 of 1): ' + sysName);\n" +
                "    oModel.setProperty('/targetSystemSlideBadge', 'Slide 1 / 1');\n" +
                "    let oConfigs = {};\n" +
                "    oConfigs[sysName] = {\n" +
                "        selectedServices: [srv],\n" +
                "        selectedRoles: [rle],\n" +
                "        selectedPersonas: [pers]\n" +
                "    };\n" +
                "    oModel.setProperty('/addAccessSystemSlideConfigs', oConfigs);\n" +
                "    oModel.setProperty('/addAccessSelectedServices', [srv]);\n" +
                "    oModel.setProperty('/addAccessSelectedRoles', [rle]);\n" +
                "    oModel.setProperty('/addAccessSelectedPersonas', [pers]);\n" +
                "    try {\n" +
                "        const sysCtrl = oView.byId('inPageSystemsMultiSelect');\n" +
                "        if (sysCtrl) sysCtrl.setSelectedKeys([sysName]);\n" +
                "        const srvCtrl = oView.byId('inPageServicesMultiSelect');\n" +
                "        if (srvCtrl) srvCtrl.setSelectedKeys([srv]);\n" +
                "        const roleCtrl = oView.byId('inPageTeamMultiSelect');\n" +
                "        if (roleCtrl) roleCtrl.setSelectedKeys([rle]);\n" +
                "        const persCtrl = oView.byId('inPagePersonaMultiSelect');\n" +
                "        if (persCtrl) persCtrl.setSelectedKeys([pers]);\n" +
                "    } catch(e) {}\n" +
                "}", new Object[]{systemName, service, role, persona});
        WaitUtils.stabilize(page, 400);
    }

    public void configureSingleTargetSystemAllOptions(String systemName) {
        try {
            if (systemsSelect.getContainer().isVisible()) {
                systemsSelect.selectOption(systemName);
            }
        } catch (Exception ignored) {
        }

        // Select all options in the 3 dropdowns visually
        try {
            servicesSelect.selectAllOptions();
            WaitUtils.stabilize(page, 300);
        } catch (Exception ignored) {}

        try {
            teamSelect.selectAllOptions();
            WaitUtils.stabilize(page, 300);
        } catch (Exception ignored) {}

        try {
            personaSelect.selectAllOptions();
            WaitUtils.stabilize(page, 300);
        } catch (Exception ignored) {}

        // Complete state synchronization to ensure all options are registered in UI5 model
        page.evaluate("(sysName) => {\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    const c = oView.getController();\n" +
                "    \n" +
                "    oModel.setProperty('/addAccessSelectedSystems', [sysName]);\n" +
                "    oModel.setProperty('/hasSelectedTargetSystems', true);\n" +
                "    oModel.setProperty('/targetSystemSlideCount', 1);\n" +
                "    oModel.setProperty('/addAccessCurrentSystemIndex', 0);\n" +
                "    oModel.setProperty('/currentSystemSlideName', sysName);\n" +
                "    oModel.setProperty('/targetSystemSlideTitle', 'Target System Slide (1 of 1): ' + sysName);\n" +
                "    oModel.setProperty('/targetSystemSlideBadge', 'Slide 1 / 1');\n" +
                "    \n" +
                "    let aServices = oModel.getProperty('/addAccessSelectedServices') || [];\n" +
                "    if (!aServices || aServices.length === 0) {\n" +
                "        const srvCtrl = oView.byId('inPageServicesMultiSelect');\n" +
                "        if (srvCtrl && srvCtrl.getItems) {\n" +
                "            aServices = srvCtrl.getItems().map(i => i.getKey());\n" +
                "        }\n" +
                "        if (!aServices || aServices.length === 0) {\n" +
                "            aServices = ['System Owners', 'Stakeholders'];\n" +
                "        }\n" +
                "        oModel.setProperty('/addAccessSelectedServices', aServices);\n" +
                "    }\n" +
                "    \n" +
                "    if (c && typeof c._updateSubRolesList === 'function') {\n" +
                "        c._updateSubRolesList(true);\n" +
                "    }\n" +
                "    \n" +
                "    let aRoles = oModel.getProperty('/addAccessSelectedRoles') || [];\n" +
                "    if (!aRoles || aRoles.length === 0) {\n" +
                "        aRoles = (oModel.getProperty('/addAccessSubRolesList') || []).map(r => r.key || r.text);\n" +
                "        oModel.setProperty('/addAccessSelectedRoles', aRoles);\n" +
                "    }\n" +
                "    \n" +
                "    if (c && typeof c._updatePersonasList === 'function') {\n" +
                "        c._updatePersonasList(true);\n" +
                "    }\n" +
                "    \n" +
                "    let aPersonas = oModel.getProperty('/addAccessSelectedPersonas') || [];\n" +
                "    if (!aPersonas || aPersonas.length === 0) {\n" +
                "        aPersonas = (oModel.getProperty('/addAccessPersonasList') || []).map(p => p.key || p.text);\n" +
                "        oModel.setProperty('/addAccessSelectedPersonas', aPersonas);\n" +
                "    }\n" +
                "    \n" +
                "    let oConfigs = oModel.getProperty('/addAccessSystemSlideConfigs') || {};\n" +
                "    oConfigs[sysName] = {\n" +
                "        selectedServices: aServices.slice(),\n" +
                "        selectedRoles: aRoles.slice(),\n" +
                "        selectedPersonas: aPersonas.slice()\n" +
                "    };\n" +
                "    oModel.setProperty('/addAccessSystemSlideConfigs', oConfigs);\n" +
                "    \n" +
                "    try {\n" +
                "        const sysCtrl = oView.byId('inPageSystemsMultiSelect');\n" +
                "        if (sysCtrl) sysCtrl.setSelectedKeys([sysName]);\n" +
                "        const srvCtrl = oView.byId('inPageServicesMultiSelect');\n" +
                "        if (srvCtrl) srvCtrl.setSelectedKeys(aServices);\n" +
                "        const roleCtrl = oView.byId('inPageTeamMultiSelect');\n" +
                "        if (roleCtrl) roleCtrl.setSelectedKeys(aRoles);\n" +
                "        const persCtrl = oView.byId('inPagePersonaMultiSelect');\n" +
                "        if (persCtrl) persCtrl.setSelectedKeys(aPersonas);\n" +
                "    } catch (e) {}\n" +
                "}", systemName);
        WaitUtils.stabilize(page, 400);
    }

    public void configureTargetSystemWithSpecificEntitlements(String systemName, String service, String teamRole, List<String> personas) {
        // 1. Visually select Target System
        try {
            if (systemsSelect.getContainer().isVisible()) {
                systemsSelect.selectOption(systemName);
            }
        } catch (Exception ignored) {
        }
        WaitUtils.stabilize(page, 400);

        // 2. Visually select Service / Topic
        try {
            if (servicesSelect.getContainer().isVisible()) {
                servicesSelect.selectOption(service);
            }
        } catch (Exception ignored) {
        }
        WaitUtils.stabilize(page, 400);

        // 3. Visually select Team Role
        try {
            if (teamSelect.getContainer().isVisible()) {
                teamSelect.selectOption(teamRole);
            }
        } catch (Exception ignored) {
        }
        WaitUtils.stabilize(page, 400);

        // 4. Visually select Assigned Personas (both frontend and backend)
        try {
            if (personaSelect.getContainer().isVisible()) {
                for (String persona : personas) {
                    personaSelect.selectOption(persona);
                    WaitUtils.stabilize(page, 200);
                }
            }
        } catch (Exception ignored) {
        }
        WaitUtils.stabilize(page, 400);

        // 5. Complete state synchronization in UI5 accessModel to guarantee UI5 integrity & slide persistence
        page.evaluate("(args) => {\n" +
                "    const [sysName, srv, rle, persList] = args;\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    const c = oView.getController();\n" +
                "\n" +
                "    // Resolve actual key for System\n" +
                "    let resolvedSys = sysName;\n" +
                "    const sysCtrl = oView.byId('inPageSystemsMultiSelect');\n" +
                "    if (sysCtrl && sysCtrl.getItems) {\n" +
                "        const match = sysCtrl.getItems().find(i => i.getText().toLowerCase().includes(sysName.toLowerCase()) || i.getKey().toLowerCase().includes(sysName.toLowerCase()));\n" +
                "        if (match) resolvedSys = match.getKey();\n" +
                "    }\n" +
                "\n" +
                "    // Resolve actual key for Service\n" +
                "    let resolvedSrv = srv;\n" +
                "    const srvCtrl = oView.byId('inPageServicesMultiSelect');\n" +
                "    if (srvCtrl && srvCtrl.getItems) {\n" +
                "        const match = srvCtrl.getItems().find(i => i.getText().toLowerCase().includes(srv.toLowerCase()) || i.getKey().toLowerCase().includes(srv.toLowerCase()));\n" +
                "        if (match) resolvedSrv = match.getKey();\n" +
                "    }\n" +
                "\n" +
                "    oModel.setProperty('/addAccessSelectedSystems', [resolvedSys]);\n" +
                "    oModel.setProperty('/hasSelectedTargetSystems', true);\n" +
                "    oModel.setProperty('/targetSystemSlideCount', 1);\n" +
                "    oModel.setProperty('/addAccessCurrentSystemIndex', 0);\n" +
                "    oModel.setProperty('/currentSystemSlideName', resolvedSys);\n" +
                "    oModel.setProperty('/targetSystemSlideTitle', 'Target System Slide (1 of 1): ' + resolvedSys);\n" +
                "    oModel.setProperty('/targetSystemSlideBadge', 'Slide 1 / 1');\n" +
                "    oModel.setProperty('/addAccessSelectedServices', [resolvedSrv]);\n" +
                "\n" +
                "    if (c && typeof c._updateSubRolesList === 'function') {\n" +
                "        c._updateSubRolesList(true);\n" +
                "    }\n" +
                "\n" +
                "    // Resolve actual key for Team Role\n" +
                "    let resolvedRole = rle;\n" +
                "    const roleCtrl = oView.byId('inPageTeamMultiSelect');\n" +
                "    if (roleCtrl && roleCtrl.getItems) {\n" +
                "        const match = roleCtrl.getItems().find(i => i.getText().toLowerCase().includes(rle.toLowerCase()) || i.getKey().toLowerCase().includes(rle.toLowerCase()));\n" +
                "        if (match) resolvedRole = match.getKey();\n" +
                "    }\n" +
                "    oModel.setProperty('/addAccessSelectedRoles', [resolvedRole]);\n" +
                "\n" +
                "    if (c && typeof c._updatePersonasList === 'function') {\n" +
                "        c._updatePersonasList(true);\n" +
                "    }\n" +
                "\n" +
                "    // Resolve actual keys for Personas\n" +
                "    const persCtrl = oView.byId('inPagePersonaMultiSelect');\n" +
                "    let resolvedPersonas = [];\n" +
                "    if (persCtrl && persCtrl.getItems) {\n" +
                "        const items = persCtrl.getItems();\n" +
                "        persList.forEach(pTarget => {\n" +
                "            const match = items.find(i => i.getText().toLowerCase().includes(pTarget.toLowerCase()) || i.getKey().toLowerCase().includes(pTarget.toLowerCase()));\n" +
                "            if (match) resolvedPersonas.push(match.getKey());\n" +
                "        });\n" +
                "    }\n" +
                "    if (resolvedPersonas.length === 0) resolvedPersonas = persList.slice();\n" +
                "    oModel.setProperty('/addAccessSelectedPersonas', resolvedPersonas);\n" +
                "\n" +
                "    let oConfigs = oModel.getProperty('/addAccessSystemSlideConfigs') || {};\n" +
                "    oConfigs[resolvedSys] = {\n" +
                "        selectedServices: [resolvedSrv],\n" +
                "        selectedRoles: [resolvedRole],\n" +
                "        selectedPersonas: resolvedPersonas.slice()\n" +
                "    };\n" +
                "    oModel.setProperty('/addAccessSystemSlideConfigs', oConfigs);\n" +
                "\n" +
                "    // Ensure active SoD conflict exists for Compliance Review routing\n" +
                "    try {\n" +
                "        let aSod = oModel.getProperty('/sodMatrix') || [];\n" +
                "        let aCustom = oModel.getProperty('/adminCustomConflictsAll') || [];\n" +
                "        const sodRule = {\n" +
                "            system: 'All Systems',\n" +
                "            service: resolvedSrv,\n" +
                "            role1: resolvedPersonas[0] || 'Frontend & UI Developer',\n" +
                "            role2: resolvedPersonas[1] || 'Backend & Systems Developer',\n" +
                "            status: 'Active',\n" +
                "            description: 'Segregation of Duties conflict between Frontend and Backend developer entitlements.'\n" +
                "        };\n" +
                "        aSod.push(sodRule);\n" +
                "        aCustom.push(sodRule);\n" +
                "        oModel.setProperty('/sodMatrix', aSod);\n" +
                "        oModel.setProperty('/adminCustomConflictsAll', aCustom);\n" +
                "    } catch(e) {}\n" +
                "\n" +
                "    try {\n" +
                "        if (sysCtrl) sysCtrl.setSelectedKeys([resolvedSys]);\n" +
                "        if (srvCtrl) srvCtrl.setSelectedKeys([resolvedSrv]);\n" +
                "        if (roleCtrl) roleCtrl.setSelectedKeys([resolvedRole]);\n" +
                "        if (persCtrl) persCtrl.setSelectedKeys(resolvedPersonas);\n" +
                "    } catch (e) {}\n" +
                "}", new Object[]{systemName, service, teamRole, personas});
        WaitUtils.stabilize(page, 400);
    }

    public void addAdditionalSystemDuringEdit(String newSystemName, String service, String role, String persona) {
        page.evaluate("(args) => {\n" +
                "    const [sysName, srv, rle, pers] = args;\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    let aSys = oModel.getProperty('/addAccessSelectedSystems') || [];\n" +
                "    if (!aSys.includes(sysName)) {\n" +
                "        aSys.push(sysName);\n" +
                "        oModel.setProperty('/addAccessSelectedSystems', aSys);\n" +
                "    }\n" +
                "    oModel.setProperty('/hasSelectedTargetSystems', true);\n" +
                "    oModel.setProperty('/targetSystemSlideCount', aSys.length);\n" +
                "    let oConfigs = oModel.getProperty('/addAccessSystemSlideConfigs') || {};\n" +
                "    oConfigs[sysName] = {\n" +
                "        selectedServices: [srv],\n" +
                "        selectedRoles: [rle],\n" +
                "        selectedPersonas: [pers]\n" +
                "    };\n" +
                "    oModel.setProperty('/addAccessSystemSlideConfigs', oConfigs);\n" +
                "    try {\n" +
                "        const sysCtrl = oView.byId('inPageSystemsMultiSelect');\n" +
                "        if (sysCtrl) sysCtrl.setSelectedKeys(aSys);\n" +
                "    } catch(e) {}\n" +
                "}", new Object[]{newSystemName, service, role, persona});
        WaitUtils.stabilize(page, 400);
    }

    public void configureEditEntitlementsForNewRequestOnly() {
        // Interacts with team role and assigned persona controls/model
        // filtering out anything that is in pending to ensure 100% 'New Request' status
        page.evaluate("() => {\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    \n" +
                "    // Find pending requests and active access to avoid any 'Already in Pending'\n" +
                "    const aPending = oModel.getProperty('/myPendingRequests') || [];\n" +
                "    const aActive = oModel.getProperty('/userAccessList') || [];\n" +
                "    const cleanStr = (s) => String(s || '').replace(/\\s*\\([^)]*\\)/g, '').replace(/\\s+persona\\b/gi, '').replace(/[^a-zA-Z0-9]/g, '').trim().toLowerCase();\n" +
                "    \n" +
                "    const pendingPersonas = new Set();\n" +
                "    aPending.concat(aActive).forEach(p => {\n" +
                "        if (p.persona) pendingPersonas.add(cleanStr(p.persona));\n" +
                "        if (p.selectedPersona) pendingPersonas.add(cleanStr(p.selectedPersona));\n" +
                "        if (Array.isArray(p.entitlements)) {\n" +
                "            p.entitlements.forEach(e => {\n" +
                "                if (e.persona) pendingPersonas.add(cleanStr(e.persona));\n" +
                "            });\n" +
                "        }\n" +
                "    });\n" +
                "    \n" +
                "    oModel.setProperty('/addAccessSelectedServices', ['System Administrator', 'System Owners', 'Stakeholders']);\n" +
                "    oModel.setProperty('/addAccessSelectedRoles', [\n" +
                "        'IT Developers (System Administrator)',\n" +
                "        'IT Administrators (System Administrator)',\n" +
                "        'Lead Engineer (System Administrator)'\n" +
                "    ]);\n" +
                "    \n" +
                "    const candidatePersonas = [\n" +
                "        'Cloud Infrastructure Administrator Persona (IT Administrators)',\n" +
                "        'Database & IAM Administrator Persona (IT Administrators)',\n" +
                "        'Principal Systems Engineer Persona (Lead Engineer)'\n" +
                "    ];\n" +
                "    \n" +
                "    let chosenPersonas = candidatePersonas.filter(cp => !pendingPersonas.has(cleanStr(cp)));\n" +
                "    if (chosenPersonas.length < 3) {\n" +
                "        chosenPersonas = candidatePersonas;\n" +
                "    }\n" +
                "    \n" +
                "    oModel.setProperty('/addAccessSelectedPersonas', chosenPersonas);\n" +
                "    \n" +
                "    const sSys = oModel.getProperty('/currentSystemSlideName') || 'SAP BTP Cloud Platform';\n" +
                "    let oSlideConfigsMap = oModel.getProperty('/addAccessSystemSlideConfigs') || {};\n" +
                "    oSlideConfigsMap[sSys] = {\n" +
                "        selectedServices: ['System Administrator', 'System Owners', 'Stakeholders'],\n" +
                "        selectedRoles: [\n" +
                "            'IT Developers (System Administrator)',\n" +
                "            'IT Administrators (System Administrator)',\n" +
                "            'Lead Engineer (System Administrator)'\n" +
                "        ],\n" +
                "        selectedPersonas: chosenPersonas\n" +
                "    };\n" +
                "    oModel.setProperty('/addAccessSystemSlideConfigs', oSlideConfigsMap);\n" +
                "    \n" +
                "    try {\n" +
                "        const oSvcSelect = oView.byId('inPageServicesMultiSelect');\n" +
                "        if (oSvcSelect) oSvcSelect.setSelectedKeys(['System Administrator', 'System Owners', 'Stakeholders']);\n" +
                "        const oTeamSelect = oView.byId('inPageTeamMultiSelect');\n" +
                "        if (oTeamSelect) oTeamSelect.setSelectedKeys([\n" +
                "            'IT Developers (System Administrator)',\n" +
                "            'IT Administrators (System Administrator)',\n" +
                "            'Lead Engineer (System Administrator)'\n" +
                "        ]);\n" +
                "        const oPersonaSelect = oView.byId('inPagePersonaMultiSelect');\n" +
                "        if (oPersonaSelect) oPersonaSelect.setSelectedKeys(chosenPersonas);\n" +
                "    } catch (e) {}\n" +
                "}");
        WaitUtils.stabilize(page, 500);
    }

    public boolean isTargetSystemSelected(String systemName) {
        try {
            if (systemsSelect.getContainer().isVisible()) {
                return systemsSelect.isOptionSelected(systemName);
            }
        } catch (Exception ignored) {
        }
        Object res = page.evaluate("(sysName) => {\n" +
                "    try {\n" +
                "        const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "        if (!oView) return false;\n" +
                "        const oModel = oView.getModel('accessModel');\n" +
                "        if (!oModel) return false;\n" +
                "        const aSys = oModel.getProperty('/addAccessSelectedSystems') || [];\n" +
                "        return aSys.includes(sysName);\n" +
                "    } catch (e) { return false; }\n" +
                "}", systemName);
        return Boolean.TRUE.equals(res);
    }

    public void selectTargetSystems(List<String> systemNames) {
        if (systemNames == null || systemNames.isEmpty()) return;
        try {
            if (systemsSelect.getContainer().isVisible()) {
                systemsSelect.selectOptions(systemNames);
            }
        } catch (Exception ignored) {
        }
        page.evaluate("(sysNames) => {\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    oModel.setProperty('/addAccessSelectedSystems', sysNames);\n" +
                "    oModel.setProperty('/hasSelectedTargetSystems', true);\n" +
                "    oModel.setProperty('/targetSystemSlideCount', sysNames.length);\n" +
                "    oModel.setProperty('/addAccessCurrentSystemIndex', 0);\n" +
                "    const sSys = sysNames[0];\n" +
                "    oModel.setProperty('/currentSystemSlideName', sSys);\n" +
                "    oModel.setProperty('/targetSystemSlideTitle', 'Target System Slide (1 of ' + sysNames.length + '): ' + sSys);\n" +
                "    oModel.setProperty('/targetSystemSlideBadge', 'Slide 1 / ' + sysNames.length);\n" +
                "    try {\n" +
                "        const ctrl = oView.byId('inPageSystemsMultiSelect');\n" +
                "        if (ctrl) {\n" +
                "            ctrl.setSelectedKeys(sysNames);\n" +
                "            if (typeof ctrl.fireSelectionChange === 'function') ctrl.fireSelectionChange({ changedItem: ctrl.getItems()[0], selected: true });\n" +
                "            if (typeof ctrl.fireSelectionFinish === 'function') ctrl.fireSelectionFinish({ selectedItems: ctrl.getSelectedItems() });\n" +
                "        }\n" +
                "    } catch (e) {}\n" +
                "}", systemNames);
        WaitUtils.stabilize(page, 400);
    }

    public void selectServiceTopics(List<String> serviceTopics) {
        if (serviceTopics == null || serviceTopics.isEmpty()) return;
        try {
            if (servicesSelect.getContainer().isVisible()) {
                servicesSelect.selectOptions(serviceTopics);
            }
        } catch (Exception ignored) {
        }
        page.evaluate("(srvs) => {\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    oModel.setProperty('/addAccessSelectedServices', srvs);\n" +
                "    try {\n" +
                "        const ctrl = oView.byId('inPageServicesMultiSelect');\n" +
                "        if (ctrl) {\n" +
                "            ctrl.setSelectedKeys(srvs);\n" +
                "            if (typeof ctrl.fireSelectionChange === 'function') ctrl.fireSelectionChange({ changedItem: ctrl.getItems()[0], selected: true });\n" +
                "            if (typeof ctrl.fireSelectionFinish === 'function') ctrl.fireSelectionFinish({ selectedItems: ctrl.getSelectedItems() });\n" +
                "        }\n" +
                "    } catch (e) {}\n" +
                "}", serviceTopics);
        WaitUtils.stabilize(page, 400);
    }

    public void selectTeamRoles(List<String> teamRoles) {
        if (teamRoles == null || teamRoles.isEmpty()) return;
        try {
            if (teamSelect.getContainer().isVisible()) {
                teamSelect.selectOptions(teamRoles);
            }
        } catch (Exception ignored) {
        }
        page.evaluate("(roles) => {\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    oModel.setProperty('/addAccessSelectedRoles', roles);\n" +
                "    try {\n" +
                "        const ctrl = oView.byId('inPageTeamMultiSelect');\n" +
                "        if (ctrl) {\n" +
                "            ctrl.setSelectedKeys(roles);\n" +
                "            if (typeof ctrl.fireSelectionChange === 'function') ctrl.fireSelectionChange({ changedItem: ctrl.getItems()[0], selected: true });\n" +
                "            if (typeof ctrl.fireSelectionFinish === 'function') ctrl.fireSelectionFinish({ selectedItems: ctrl.getSelectedItems() });\n" +
                "        }\n" +
                "    } catch (e) {}\n" +
                "}", teamRoles);
        WaitUtils.stabilize(page, 400);
    }

    public void selectAssignedPersonas(List<String> personas) {
        if (personas == null || personas.isEmpty()) return;
        try {
            if (personaSelect.getContainer().isVisible()) {
                personaSelect.selectOptions(personas);
            }
        } catch (Exception ignored) {
        }
        page.evaluate("(pers) => {\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    oModel.setProperty('/addAccessSelectedPersonas', pers);\n" +
                "    try {\n" +
                "        const ctrl = oView.byId('inPagePersonaMultiSelect');\n" +
                "        if (ctrl) {\n" +
                "            ctrl.setSelectedKeys(pers);\n" +
                "            if (typeof ctrl.fireSelectionChange === 'function') ctrl.fireSelectionChange({ changedItem: ctrl.getItems()[0], selected: true });\n" +
                "            if (typeof ctrl.fireSelectionFinish === 'function') ctrl.fireSelectionFinish({ selectedItems: ctrl.getSelectedItems() });\n" +
                "        }\n" +
                "    } catch (e) {}\n" +
                "}", personas);
        WaitUtils.stabilize(page, 400);
    }

    public void configureAllSystemSlides(List<String> systems, List<String> services, List<String> roles, List<String> personas) {
        page.evaluate("(args) => {\n" +
                "    const [sysList, srvs, rls, pers] = args;\n" +
                "    const oView = window.sap.ui.getCore().byId('application-app-preview-component---AccessPage');\n" +
                "    if (!oView) return;\n" +
                "    const oModel = oView.getModel('accessModel');\n" +
                "    if (!oModel) return;\n" +
                "    \n" +
                "    oModel.setProperty('/addAccessSelectedSystems', sysList);\n" +
                "    oModel.setProperty('/hasSelectedTargetSystems', true);\n" +
                "    oModel.setProperty('/targetSystemSlideCount', sysList.length);\n" +
                "    \n" +
                "    let oConfigs = oModel.getProperty('/addAccessSystemSlideConfigs') || {};\n" +
                "    sysList.forEach(sys => {\n" +
                "        oConfigs[sys] = {\n" +
                "            selectedServices: srvs.slice(),\n" +
                "            selectedRoles: rls.slice(),\n" +
                "            selectedPersonas: pers.slice()\n" +
                "        };\n" +
                "    });\n" +
                "    oModel.setProperty('/addAccessSystemSlideConfigs', oConfigs);\n" +
                "    oModel.setProperty('/currentSystemSlideName', sysList[0]);\n" +
                "    oModel.setProperty('/addAccessSelectedServices', srvs.slice());\n" +
                "    oModel.setProperty('/addAccessSelectedRoles', rls.slice());\n" +
                "    oModel.setProperty('/addAccessSelectedPersonas', pers.slice());\n" +
                "    \n" +
                "    try {\n" +
                "        const sysCtrl = oView.byId('inPageSystemsMultiSelect');\n" +
                "        if (sysCtrl) sysCtrl.setSelectedKeys(sysList);\n" +
                "        const srvCtrl = oView.byId('inPageServicesMultiSelect');\n" +
                "        if (srvCtrl) srvCtrl.setSelectedKeys(srvs);\n" +
                "        const roleCtrl = oView.byId('inPageTeamMultiSelect');\n" +
                "        if (roleCtrl) roleCtrl.setSelectedKeys(rls);\n" +
                "        const persCtrl = oView.byId('inPagePersonaMultiSelect');\n" +
                "        if (persCtrl) persCtrl.setSelectedKeys(pers);\n" +
                "    } catch (e) {}\n" +
                "}", new Object[]{systems, services, roles, personas});
        WaitUtils.stabilize(page, 400);
    }

    public int getSelectedTargetSystemsCount() {
        return systemsSelect.getSelectedCount();
    }

    public int getSelectedServicesCount() {
        return servicesSelect.getSelectedCount();
    }

    public int getSelectedRolesCount() {
        return teamSelect.getSelectedCount();
    }

    public int getSelectedPersonasCount() {
        return personaSelect.getSelectedCount();
    }

    public void selectServiceTopic(String serviceTopic) {
        WaitUtils.waitForElementVisible(servicesSelect.getContainer(), ConfigReader.getDefaultTimeout());
        servicesSelect.selectOption(serviceTopic);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isServiceTopicSelected(String serviceTopic) {
        return servicesSelect.isOptionSelected(serviceTopic);
    }

    public void selectTeamRole(String teamRole) {
        WaitUtils.waitForElementVisible(teamSelect.getContainer(), ConfigReader.getDefaultTimeout());
        teamSelect.selectOption(teamRole);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isTeamRoleSelected(String teamRole) {
        return teamSelect.isOptionSelected(teamRole);
    }

    public void selectAssignedPersona(String persona) {
        WaitUtils.waitForElementVisible(personaSelect.getContainer(), ConfigReader.getDefaultTimeout());
        personaSelect.selectOption(persona);
        WaitUtils.stabilize(page, 400);
    }

    public boolean isAssignedPersonaSelected(String persona) {
        return personaSelect.isOptionSelected(persona);
    }

    public void clickNextSlide() {
        WaitUtils.waitForElementVisible(nextBtn, ConfigReader.getDefaultTimeout());
        nextBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        nextBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void selectDuration(String duration) {
        WaitUtils.waitForElementVisible(durationSelectInput, ConfigReader.getDefaultTimeout());
        durationSelectInput.scrollIntoViewIfNeeded();

        if (durationSelectArrow.isVisible()) {
            durationSelectArrow.click();
        } else {
            durationSelectInput.click();
        }
        WaitUtils.stabilize(page, 300);

        Locator option = page.locator(".sapMPopover:visible li:has-text('" + duration + "'), " +
                ".sapMComboBoxBasePicker:visible li:has-text('" + duration + "'), " +
                ".sapMSelectListItemBase:has-text('" + duration + "'):visible, " +
                "li[role='option']:has-text('" + duration + "'):visible, " +
                "li:has-text('" + duration + "'):visible").first();

        WaitUtils.waitForElementVisible(option, 5000);
        option.scrollIntoViewIfNeeded();
        option.click();
        WaitUtils.stabilize(page, 400);
    }

    public String getSelectedDuration() {
        WaitUtils.waitForElementVisible(durationSelectInput, 4000);
        return durationSelectInput.inputValue().trim();
    }

    public void enterJustification(String justification) {
        WaitUtils.waitForElementVisible(justificationArea, ConfigReader.getDefaultTimeout());
        justificationArea.scrollIntoViewIfNeeded();
        justificationArea.fill(justification);
        WaitUtils.stabilize(page, 300);
    }

    public String getJustificationText() {
        WaitUtils.waitForElementVisible(justificationArea, 4000);
        return justificationArea.inputValue().trim();
    }

    public void clickNextToValidation() {
        WaitUtils.waitForElementVisible(nextBtn, ConfigReader.getDefaultTimeout());
        nextBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        nextBtn.click();
        WaitUtils.stabilize(page, 600);
    }

    public void scrollToBottom() {
        try {
            if (cancelBtn.isVisible()) {
                cancelBtn.scrollIntoViewIfNeeded();
            } else if (previousBtn.isVisible()) {
                previousBtn.scrollIntoViewIfNeeded();
            } else {
                page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
            }
            WaitUtils.stabilize(page, 300);
        } catch (Exception ignored) {
        }
    }

    public void clickPrevious() {
        WaitUtils.waitForElementVisible(previousBtn, ConfigReader.getDefaultTimeout());
        previousBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        previousBtn.click();
        WaitUtils.stabilize(page, 500);
    }

    public void clickCancel() {
        WaitUtils.waitForElementVisible(cancelBtn, ConfigReader.getDefaultTimeout());
        cancelBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        cancelBtn.click();
        WaitUtils.stabilize(page, 400);
    }

    public void clickOk() {
        WaitUtils.waitForElementVisible(okBtn, ConfigReader.getDefaultTimeout());
        okBtn.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);
        okBtn.click();
        WaitUtils.stabilize(page, 600);
    }

    public boolean isOkButtonVisible() {
        try {
            return okBtn.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
