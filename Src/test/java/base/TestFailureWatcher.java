package base;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;
import utils.ScreenshotUtils;

import java.util.Optional;

public class TestFailureWatcher implements TestWatcher, TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof BaseTest) {
            BaseTest baseTest = (BaseTest) testInstance;
            String persona = baseTest.getCurrentPersona();
            String empId = baseTest.getCurrentEmployeeId();
            String step = baseTest.getCurrentStep();

            ScreenshotUtils.captureFailureScreenshot(
                    baseTest.getPage(),
                    persona != null ? persona : context.getDisplayName(),
                    empId != null ? empId : "failure",
                    step != null ? step : throwable.getMessage()
            );
        }
        // Re-throw exception so test framework records the failure accurately
        throw throwable;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // Handled primarily by handleTestExecutionException prior to teardown
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
    }
}
