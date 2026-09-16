package utils;

import java.util.ArrayList;
import java.util.List;

public class StepReporter {

    public static class ReportEntry {
        public final String name;
        public final String status;
        public final boolean isSection;

        public ReportEntry(String name, String status, boolean isSection) {
            this.name = name;
            this.status = status;
            this.isSection = isSection;
        }
    }

    private final String suiteName;
    private final List<ReportEntry> entries = new ArrayList<>();

    public StepReporter(String suiteName) {
        this.suiteName = suiteName;
    }

    public void section(String sectionTitle) {
        entries.add(new ReportEntry(sectionTitle, "---SECTION---", true));
        System.out.printf("%n--- %s ---%n", sectionTitle);
    }

    public void pass(String stepName) {
        entries.add(new ReportEntry(stepName, "PASS", false));
        System.out.printf("   ✓ [PASS] %s%n", stepName);
    }

    public void fail(String stepName) {
        entries.add(new ReportEntry(stepName, "FAIL", false));
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
        boolean firstSection = true;
        for (ReportEntry entry : entries) {
            if (entry.isSection) {
                if (!firstSection) {
                    System.out.println();
                }
                System.out.println(entry.name);
                firstSection = false;
            } else {
                System.out.printf("%-27s : %s%n", entry.name, entry.status);
                if (!"PASS".equals(entry.status)) {
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
