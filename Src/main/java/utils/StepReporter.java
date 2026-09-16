package utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class StepReporter {

    private final String suiteName;
    private final Map<String, String> steps = new LinkedHashMap<>();

    public StepReporter(String suiteName) {
        this.suiteName = suiteName;
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
        System.out.println(suiteName.toUpperCase() + " TEST");
        System.out.println("========================================");
        for (Map.Entry<String, String> entry : steps.entrySet()) {
            System.out.printf("%-18s: %s%n", entry.getKey(), entry.getValue());
        }
        System.out.println("========================================");
        System.out.println();
    }
}
