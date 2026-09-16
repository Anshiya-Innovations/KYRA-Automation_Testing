package utils;

import com.microsoft.playwright.Page;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "test-results/screenshots";

    public static String captureFailureScreenshot(Page page, String persona, String employeeId, String failedStep) {
        try {
            File dir = new File(SCREENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String safePersona = (persona != null ? persona.replace(" ", "") : "UnknownPersona");
            String safeEmpId = (employeeId != null ? employeeId.trim() : "UnknownId");
            String fileName = safePersona + "_" + safeEmpId + "_failure.png";
            Path targetPath = Paths.get(SCREENSHOT_DIR, fileName);

            if (page != null) {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(targetPath)
                        .setFullPage(true));

                System.out.println("==================================================");
                System.out.println("FAILURE SCREENSHOT CAPTURED");
                System.out.println("File        : " + targetPath.toAbsolutePath());
                System.out.println("Current URL : " + page.url());
                System.out.println("Failed Step : " + failedStep);
                System.out.println("Persona     : " + persona);
                System.out.println("Employee ID : " + employeeId);
                System.out.println("==================================================");

                return targetPath.toString();
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
        return null;
    }
}
