package com.sios.stc.coseng.run;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;
import com.sios.stc.coseng.util.UriUtil;

public final class SeleniumWebDriver {

    private static final Logger log = LogManager.getLogger(SeleniumWebDriver.class);

    @Expose
    private Location location             = null;
    @Expose
    private File     executable           = null;
    @Expose
    private Boolean  oneWebDriver         = null;
    @Expose
    private Long     waitTimeoutSecond    = null;
    @Expose
    private Long     waitSleepMillisecond = null;
    @Expose
    private Level    logLevel             = null;
    @Expose
    private URL      gridUrl              = null;

    private Test           test              = null;
    private WebDrivers     webDrivers        = null;
    private DriverServices driverServices    = null;
    private AtomicInteger  startedWebDrivers = new AtomicInteger(0);
    private AtomicInteger  stoppedWebDrivers = new AtomicInteger(0);

    public enum Location {
        NODE, GRID
    };

    public Location getLocation() {
        return location;
    }

    public File getExecutable() {
        return executable;
    }

    public boolean isOneWebDriver() {
        return oneWebDriver.booleanValue();
    }

    public long getWaitTimeoutSecond() {
        return waitTimeoutSecond.longValue();
    }

    public long getWaitSleepMillisecond() {
        return waitSleepMillisecond.longValue();
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public URL getGridUrl() {
        return gridUrl;
    }

    public WebDrivers getWebDrivers() {
        return webDrivers;
    }

    DriverServices getDriverServices() {
        return driverServices;
    }

    public int getStartedWebDrivers() {
        return startedWebDrivers.get();
    }

    public int getStoppedWebDrivers() {
        return stoppedWebDrivers.get();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "test", "webDrivers", "driverServices",
                "startedWebDrivers", "stoppedWebDrivers");
    }

    void startWebDriver() {
        /*
         * Web driver service for location [node] is started and verified running in
         * TestNgListener.
         */
        try {
            Browser browser = test.getSelenium().getBrowser().getType();
            File browserExecutable = test.getSelenium().getBrowser().getExecutable();
            MutableCapabilities options = test.getSelenium().getBrowser().getOptions();
            Location location = getLocation();
            URL gridUrl = getGridUrl();

            RemoteWebDriver remoteWebDriver = null;

            switch (browser) {
                case FIREFOX:
                    FirefoxOptions fo = (FirefoxOptions) options;
                    if (Location.NODE.equals(location)) {
                        if (browserExecutable != null)
                            fo.setBinary(browserExecutable.getCanonicalPath());
                        /*
                         * Firefox geckodriver single instance can't process parallel sessions (see
                         * TestNgListener)
                         */
                        driverServices.start();
                        remoteWebDriver = new RemoteWebDriver(driverServices.get().getUrl(), fo);
                    } else {
                        remoteWebDriver = new RemoteWebDriver(gridUrl, fo);
                    }
                    break;
                case CHROME:
                    ChromeOptions co = (ChromeOptions) options;
                    if (Location.NODE.equals(location)) {
                        if (browserExecutable != null)
                            co.setBinary(browserExecutable.getCanonicalPath());
                        /*
                         * Chrome chromedriver single instance *can* process parallel sessions (see
                         * TestNgListener)
                         */
                        remoteWebDriver = new RemoteWebDriver(driverServices.get().getUrl(), co);
                    } else {
                        remoteWebDriver = new RemoteWebDriver(gridUrl, co);
                    }
                    break;
                case EDGE:
                    EdgeOptions eo = (EdgeOptions) options;
                    if (Location.NODE.equals(location)) {
                        /*
                         * Edge MicrosoftWebDriver single instance can't process parallel sessions (see
                         * TestNgListener)
                         */
                        driverServices.start();
                        remoteWebDriver = new RemoteWebDriver(driverServices.get().getUrl(), eo);
                    } else {
                        remoteWebDriver = new RemoteWebDriver(gridUrl, eo);
                    }
                    break;
                case IE:
                    InternetExplorerOptions ieo = (InternetExplorerOptions) options;
                    if (Location.NODE.equals(location)) {
                        /*
                         * Internet Explorer IEDriverServer single instance *can* process parallel
                         * sessions (see TestNgListener)
                         */
                        remoteWebDriver = new RemoteWebDriver(driverServices.get().getUrl(), ieo);
                    } else {
                        remoteWebDriver = new RemoteWebDriver(gridUrl, ieo);
                    }
                    break;
                case SAFARI:
                    SafariOptions so = (SafariOptions) options;
                    if (Location.NODE.equals(location)) {
                        /*
                         * Safari web driver single instance can't process parallel sessions (see
                         * TestNgListener)
                         */
                        driverServices.start();
                        remoteWebDriver = new RemoteWebDriver(driverServices.get().getUrl(), so);
                    } else {
                        remoteWebDriver = new RemoteWebDriver(gridUrl, so);
                    }
                    break;
                case OPERA:
                    OperaOptions oo = (OperaOptions) options;
                    if (Location.NODE.equals(location)) {
                        /*
                         * Opera web driver single instance can't process parallel sessions (see
                         * TestNgListener)
                         */
                        driverServices.start();
                        remoteWebDriver = new RemoteWebDriver(driverServices.get().getUrl(), oo);
                    }
                    remoteWebDriver = new RemoteWebDriver(gridUrl, oo);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser " + Stringer.wrapBracket(browser));

            }
            if (!driverServices.isRunning())
                throw new IllegalStateException("Web driver service "
                        + Stringer.wrapBracket(driverServices.get().getClass().getName()) + " is not running");
            // TODO may be out of date
            /*
             * Set the file detector for uploads; set early. Setting early ameliorates
             * absolute pathing issues if set later. FireFox fileUpload (0.19.0 geckodriver,
             * 53+FF) does not function if setFileDector. Chrome (2.32 chromedriver,
             * 60+Chrome) and others work as expected with setFileDetector.
             */
            remoteWebDriver.setFileDetector(new LocalFileDetector());

            /* Set dimension or maximize */
            Dimension dimension = test.getSelenium().getBrowser().getDimension();
            if (dimension != null)
                remoteWebDriver.manage().window().setSize(dimension);
            else if (test.getSelenium().getBrowser().isMaximize())
                remoteWebDriver.manage().window().maximize();

            /* Set logging level */
            remoteWebDriver.setLogLevel(this.getLogLevel());

            /* Make test aware of Selenium tooling */
            webDrivers.setWebDriverCollection(remoteWebDriver,
                    test.getSelenium().getWebDriverContext().getWaitTimeoutSecond(),
                    test.getSelenium().getWebDriverContext().getWaitSleepMillisecond());

            startedWebDrivers.getAndIncrement();
            log.debug("Started web driver [{}], thread [{}]", webDrivers.getRemoteWebDriver().hashCode(),
                    Thread.currentThread().getId());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error starting web driver. Browser and web driver mismatch? Check for zombie web driver processes",
                    e);
        }
    }

    void stopWebDriver() {
        try {
            RemoteWebDriver remoteWebDriver = webDrivers.getRemoteWebDriver();
            remoteWebDriver.quit();
            /*
             * For web driver services which can't process concurrently, for safe measure
             * stop the service even though the webDriver.quit() should close the session
             * and therfore the service.
             */
            Browser browser = test.getSelenium().getBrowser().getType();
            switch (browser) {
                case FIREFOX:
                    driverServices.stop();
                    break;
                case CHROME:
                    break;
                case EDGE:
                    driverServices.stop();
                    break;
                case IE:
                    break;
                case SAFARI:
                    driverServices.stop();
                    break;
                case OPERA:
                    driverServices.stop();
                    break;
            }
            stoppedWebDrivers.getAndIncrement();
            log.debug("Stopped web driver [{}], thread [{}]", webDrivers.getRemoteWebDriver().hashCode(),
                    Thread.currentThread().getId());
        } catch (Exception ignore) {
            // best effort; may have been skipRemainingTestsOnFailure
        }
    }

    void validateAndPrepare(Test test) {
        try {
            if (location == null)
                location = Location.NODE;
            if (executable != null && Location.NODE.equals(location) && !executable.canExecute())
                throw new IllegalArgumentException(
                        "Web driver executable " + Stringer.wrapBracket(executable) + " must exist and be executable");
            if (oneWebDriver == null)
                oneWebDriver = false;
            if (waitTimeoutSecond == null)
                waitTimeoutSecond = new Long(5);
            if (waitSleepMillisecond == null)
                waitSleepMillisecond = new Long(250);
            if (logLevel == null)
                logLevel = Level.FINE;
            if (gridUrl == null && Location.GRID.equals(location))
                throw new IllegalArgumentException(
                        "Field gridUrl must be provided when location " + Stringer.wrapBracket(location));

            /*
             * Setting up driver service depends on TestNg directories being setup. Do a
             * second 'prepare' with prepareDriverService() *after* TestNg
             * validateAndPrepare() completed in Test.
             */
            webDrivers = new WebDrivers();
            this.test = test;
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

    void prepareDriverService() {
        if (Location.NODE.equals(test.getSelenium().getWebDriverContext().getLocation())) {
            try {
                Browser browser = test.getSelenium().getBrowser().getType();
                File executable = getExecutable();

                LinkedList<URI> logBits = new LinkedList<URI>();
                logBits.add(test.getTestNg().getDirectory().getLogs());
                logBits.add(UriUtil
                        .getAbsolute(browser.name().toLowerCase() + Stringer.Separator.FILENAME.get() + "webDriver"));
                File logFile = Resource.getFile(UriUtil.concatFiles(logBits));

                Resource.touchFile(logFile);

                driverServices = new DriverServices(browser, executable, logFile);
            } catch (Exception e) {
                throw new CosengConfigException(e);
            }
        }
    }

}
