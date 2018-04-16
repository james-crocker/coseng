package com.sios.stc.coseng.run;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.opera.OperaDriverService;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriverService;

import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.util.Stringer;

public final class DriverServices {

    private static final InheritableThreadLocal<DriverService> driverService           = new InheritableThreadLocal<DriverService>();
    private DriverService                                      concurrentDriverService = null;
    private Browser                                            browser                 = null;
    private File                                               executable              = null;
    private File                                               logFile                 = null;

    /*
     * Concurrent web driver services are capable of being started once and used for
     * multiple web browser/web driver instances in parallel. Other services will be
     * created as needed.
     */

    DriverServices(Browser browser, File executable, File logFile) {
        switch (browser) {
            case FIREFOX:
                /*
                 * Incapable of concurrent web driver instantiations; must use
                 * WebDriverServiceFactory.
                 */
                break;
            case CHROME:
                if (executable != null)
                    concurrentDriverService = new ChromeDriverService.Builder().usingDriverExecutable(executable)
                            .withLogFile(logFile).usingAnyFreePort().build();
                else
                    concurrentDriverService = new ChromeDriverService.Builder().withLogFile(logFile).usingAnyFreePort()
                            .build();
                break;
            case EDGE:
                /*
                 * Incapable of concurrent web driver instantiations; must use
                 * WebDriverServiceFactory.
                 */

                break;
            case IE:
                if (executable != null)
                    concurrentDriverService = new EdgeDriverService.Builder().usingDriverExecutable(executable)
                            .withLogFile(logFile).usingAnyFreePort().build();
                else
                    concurrentDriverService = new EdgeDriverService.Builder().withLogFile(logFile).usingAnyFreePort()
                            .build();
                break;
            case SAFARI:
                /*
                 * Incapable of concurrent web driver instantiations; must use
                 * WebDriverServiceFactory.
                 */

                break;
            case OPERA:
                /*
                 * Incapable of concurrent web driver instantiations; must use
                 * WebDriverServiceFactory.
                 */

                break;
            default:
                throw new IllegalArgumentException("Unsupported browser " + Stringer.wrapBracket(browser));
        }
        this.browser = browser;
        this.executable = executable;
        this.logFile = logFile;
    }

    DriverService get() {
        if (concurrentDriverService != null)
            /* Concurrent driver service; single instance. */
            return concurrentDriverService;
        if (driverService.get() == null)
            driverService.set(DriverServiceFactory.getDriverService(browser, executable, logFile));
        return driverService.get();
    }

    void start() throws IOException {
        DriverService service = get();
        if (!isRunning(service)) {
            if (service instanceof GeckoDriverService) {
                ((GeckoDriverService) service).start();
            } else if (service instanceof ChromeDriverService) {
                ((ChromeDriverService) service).start();
            } else if (service instanceof EdgeDriverService) {
                ((EdgeDriverService) service).start();
            } else if (service instanceof InternetExplorerDriverService) {
                ((InternetExplorerDriverService) service).start();
            } else if (service instanceof SafariDriverService) {
                ((SafariDriverService) service).start();
            } else if (service instanceof OperaDriverService) {
                ((OperaDriverService) service).start();
            }
        } else {
            throw new IllegalStateException(
                    "Driver service " + Stringer.wrapBracket(service.getClass().getName()) + " is already running");
        }
    }

    void stop() {
        /* WebDriverService is for local/node instances */
        DriverService service = get();
        if (service instanceof GeckoDriverService) {
            ((GeckoDriverService) service).stop();
        } else if (service instanceof ChromeDriverService) {
            ((ChromeDriverService) service).stop();
        } else if (service instanceof EdgeDriverService) {
            ((EdgeDriverService) service).stop();
        } else if (service instanceof InternetExplorerDriverService) {
            ((InternetExplorerDriverService) service).stop();
        } else if (service instanceof SafariDriverService) {
            ((SafariDriverService) service).stop();
        } else if (service instanceof OperaDriverService) {
            ((OperaDriverService) service).stop();
        }
    }

    boolean isRunning() {
        return isRunning(get());
    }

    private boolean isRunning(DriverService service) {
        if (service instanceof GeckoDriverService) {
            return ((GeckoDriverService) service).isRunning();
        } else if (service instanceof ChromeDriverService) {
            return ((ChromeDriverService) service).isRunning();
        } else if (service instanceof EdgeDriverService) {
            return ((EdgeDriverService) service).isRunning();
        } else if (service instanceof InternetExplorerDriverService) {
            return ((EdgeDriverService) service).isRunning();
        } else if (service instanceof SafariDriverService) {
            return ((SafariDriverService) service).isRunning();
        } else if (service instanceof OperaDriverService) {
            return ((OperaDriverService) service).isRunning();
        }
        return false;
    }

}