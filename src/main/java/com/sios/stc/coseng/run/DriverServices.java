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
import com.sios.stc.coseng.util.Random;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;

public final class DriverServices {

    private static final ThreadLocal<DriverService> driverService           = new ThreadLocal<DriverService>();
    private DriverService                           concurrentDriverService = null;
    private boolean                                 concurrentCapable       = false;
    private Browser                                 browser                 = null;
    private File                                    executable              = null;
    private File                                    logFile                 = null;

    /*
     * Concurrent web driver services are capable of being started once and used for
     * multiple web browser/web driver instances in parallel. Other services will be
     * created as needed.
     */

    DriverServices(Browser browser, File executable, File logFile) {
        this.browser = browser;
        this.executable = executable;
        this.logFile = logFile;
        switch (browser) {
            case FIREFOX:
                concurrentCapable = false;
                break;
            case CHROME:
                concurrentCapable = true;
                concurrentDriverService = DriverServiceFactory.getDriverService(browser, executable, logFile);
                break;
            case EDGE:
                concurrentCapable = false;
                break;
            case IE:
                concurrentCapable = true;
                concurrentDriverService = DriverServiceFactory.getDriverService(browser, executable, logFile);
                break;
            case SAFARI:
                concurrentCapable = false;
                break;
            case OPERA:
                concurrentCapable = false;
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser " + Stringer.wrapBracket(browser));
        }
    }

    boolean isConcurrentCapable() {
        return concurrentCapable;
    }

    DriverService get() {
        if (concurrentDriverService != null)
            return concurrentDriverService;
        if (driverService.get() == null)
            driverService.set(DriverServiceFactory.getDriverService(browser, executable, getUniqueLogFile()));
        return driverService.get();
    }

    void start() {
        DriverService service = get();
        if (!isRunning(service)) {
            try {
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
            } catch (IOException e) {
                throw new IllegalStateException("Driver service " + Stringer.wrapBracket(service.getClass().getName())
                        + " could not be started", e);
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

    private File getUniqueLogFile() {
        String uniqueFile;
        try {
            uniqueFile = logFile.getCanonicalPath() + Stringer.Separator.FILENAME.get()
                    + String.valueOf(Random.getRandomInt(1, 1024)) + Stringer.Separator.FILENAME.get() + "threadId"
                    + Stringer.Separator.FILENAME.get() + Thread.currentThread().getId();
        } catch (IllegalArgumentException | IOException ignore) {
            // best effort
            return logFile;
        }
        File file = new File(uniqueFile);
        Resource.touchFile(file);
        return file;
    }

}