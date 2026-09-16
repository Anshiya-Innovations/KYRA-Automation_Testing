package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Warning: config.properties file not found in classpath. Using defaults.");
            }
        } catch (IOException ex) {
            System.err.println("Error loading config.properties: " + ex.getMessage());
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        return properties.getProperty(key, defaultValue);
    }

    public static String getBaseUrl() {
        return getProperty("app.baseUrl", "http://localhost:8080/test/flp.html?sap-ui-xx-viewCache=false#app-preview");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("browser.headless", "false"));
    }

    public static int getSlowMo() {
        return Integer.parseInt(getProperty("browser.slowMo", "200"));
    }

    public static boolean isMaximized() {
        return Boolean.parseBoolean(getProperty("browser.maximized", "true"));
    }

    public static double getBrowserZoom() {
        return Double.parseDouble(getProperty("browser.zoom", "1.0"));
    }

    public static double getDeviceScaleFactor() {
        return Double.parseDouble(getProperty("browser.deviceScaleFactor", "1.0"));
    }

    public static int getViewportWidth() {
        return Integer.parseInt(getProperty("browser.viewport.width", "1920"));
    }

    public static int getViewportHeight() {
        return Integer.parseInt(getProperty("browser.viewport.height", "1080"));
    }

    public static int getDefaultTimeout() {
        return Integer.parseInt(getProperty("timeout.default", "10000"));
    }

    public static int getNavigationTimeout() {
        return Integer.parseInt(getProperty("timeout.navigation", "15000"));
    }

    public static String getRequesterPersona() {
        return getProperty("persona.requester.name", "Requester");
    }

    public static String getRequesterId() {
        return getProperty("persona.requester.id", "emp010");
    }

    public static String getApproverPersona() {
        return getProperty("persona.approver.name", "Approver");
    }

    public static String getApproverId() {
        return getProperty("persona.approver.id", "emp085");
    }

    public static String getCompliancePersona() {
        return getProperty("persona.compliance.name", "Compliance Review");
    }

    public static String getComplianceId() {
        return getProperty("persona.compliance.id", "emp095");
    }
}
