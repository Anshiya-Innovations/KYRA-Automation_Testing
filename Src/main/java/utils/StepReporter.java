package utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class StepReporter {

    private final String suiteName;
    private final Map<String, String> steps = new LinkedHashMap<>();

    public StepReporter(String suiteName) {
        this.suiteName = suiteName;
    }

    public void section(String sectionTitle) {
        steps.put(sectionTitle, "---SECTION---");
        System.out.printf("%n--- %s ---%n", sectionTitle);
    }

    public void pass(String stepName) {
        steps.put(stepName, "PASS");
        System.out.printf("   ✓ [PASS] %s%n", stepName);
    }

    public void fail(String stepName) {
        steps.put(stepName, "FAIL");
        System.out.printf("   ✗ [FAIL] %s%n", stepName);
    }

    public void printSummary() {
        System.out.println();
        System.out.println("========================================");
        String header = suiteName.toUpperCase().trim();
        if (!header.endsWith("TEST")) {
            header += " TEST";
        }
        System.out.println(header);
        System.out.println("========================================");
        System.out.println();
        boolean allPassed = true;
        for (Map.Entry<String, String> entry : steps.entrySet()) {
            if ("---SECTION---".equals(entry.getValue())) {
                System.out.println();
                System.out.println(entry.getKey());
            } else {
                System.out.printf("%-26s : %s%n", entry.getKey(), entry.getValue());
                if (!"PASS".equals(entry.getValue())) {
                    allPassed = false;
                }
            }
        }
        System.out.println();
        System.out.println("========================================");
        if (allPassed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
        System.out.println("========================================");
        System.out.println();
    }
}
