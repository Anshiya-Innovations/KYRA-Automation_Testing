package components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.ConfigReader;
import utils.WaitUtils;

import java.util.List;

public class MultiSelectComponent {

    private final Page page;
    private final Locator container;
    private final Locator arrowBtn;
    private final Locator innerInput;

    public MultiSelectComponent(Page page, String containerSelector) {
        this(page, page.locator(containerSelector).first());
    }

    public MultiSelectComponent(Page page, Locator container) {
        this.page = page;
        this.container = container;
        this.arrowBtn = container.locator(".sapMComboBoxBaseArrow, [id$='-arrow'], .sapMInputBaseIcon, .sapMSltArrow").first();
        this.innerInput = container.locator("input, [id$='-inner']").first();
    }

    public boolean isVisible() {
        try {
            return container.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled() {
        try {
            String ariaDisabled = container.getAttribute("aria-disabled");
            return !"true".equalsIgnoreCase(ariaDisabled) && container.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void open() {
        WaitUtils.waitForElementVisible(container, ConfigReader.getDefaultTimeout());
        container.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 200);

        Locator picker = page.locator(".sapMPopover:visible, .sapMComboBoxBasePicker:visible, .sapMMultiComboBoxList:visible");
        if (picker.isVisible()) {
            return;
        }

        if (arrowBtn.isVisible()) {
            arrowBtn.click();
        } else {
            container.click();
        }

        try {
            picker.first().waitFor(new Locator.WaitForOptions().setTimeout(2500));
        } catch (Exception e) {
            try {
                innerInput.focus();
                page.keyboard().press("Alt+ArrowDown");
                picker.first().waitFor(new Locator.WaitForOptions().setTimeout(2500));
            } catch (Exception e2) {
                try {
                    container.evaluate("el => { const c = sap.ui.getCore().byId(el.id); if (c && typeof c.open === 'function') c.open(); }");
                } catch (Exception ignored) {
                }
            }
        }
        WaitUtils.stabilize(page, 200);
    }

    public void close() {
        try {
            Locator popover = page.locator(".sapMPopover:visible, .sapMComboBoxBasePicker:visible, .sapMMultiComboBoxList:visible").first();
            if (popover.isVisible()) {
                page.keyboard().press("Escape");
                WaitUtils.stabilize(page, 200);
            }
            if (popover.isVisible()) {
                if (arrowBtn.isVisible()) {
                    arrowBtn.click();
                } else {
                    container.click();
                }
                WaitUtils.stabilize(page, 200);
            }
        } catch (Exception ignored) {
        }
    }

    public void selectOption(String optionText) {
        open();

        Locator option = page.locator(".sapMPopover:visible li:has-text('" + optionText + "'), " +
                ".sapMComboBoxBasePicker:visible li:has-text('" + optionText + "'), " +
                ".sapMMultiComboBoxList:visible li:has-text('" + optionText + "'), " +
                "li[role='checkbox']:has-text('" + optionText + "'), " +
                "li:has-text('" + optionText + "'):visible").first();

        WaitUtils.waitForElementVisible(option, ConfigReader.getDefaultTimeout());
        option.scrollIntoViewIfNeeded();
        WaitUtils.stabilize(page, 150);
        option.click();
        WaitUtils.stabilize(page, 300);

        close();
    }

    public boolean isOptionSelected(String optionText) {
        try {
            WaitUtils.stabilize(page, 200);
            // Check tokens or input text
            Locator tokens = container.locator(".sapMTokenText, .sapMToken");
            for (int i = 0; i < tokens.count(); i++) {
                String tokenText = tokens.nth(i).innerText().trim();
                if (tokenText.contains(optionText) || optionText.contains(tokenText)) {
                    return true;
                }
            }

            // Fallback: check inner input value
            String val = innerInput.inputValue();
            if (val != null && val.contains(optionText)) {
                return true;
            }

            // Fallback: check UI5 control selectedKeys
            Object selected = container.evaluate("el => { const c = sap.ui.getCore().byId(el.id); return (c && c.getSelectedKeys) ? c.getSelectedKeys() : []; }");
            if (selected != null && selected.toString().contains(optionText)) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Locator getContainer() {
        return container;
    }
}
