package com.sios.stc.coseng.aut;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.run.TestNgContext;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;
import com.sios.stc.coseng.util.UriUtil;

public final class Window {

    private Test test = null;

    Window(Test test) {
        this.test = test;
    }

    public void saveScreenshot() {
        saveScreenshot(null);
    }

    public void saveScreenshot(String filename) {
        try {
            WebDriver webDriver = test.getSelenium().getWebDriverContext().getWebDrivers().getWebDriver();
            if (test.getCoseng().getScreenshot().isEnable()
                    || test.getCoseng().getScreenshot().isEnableOnAssertFail()) {
                LinkedList<URI> filePaths = new LinkedList<URI>();
                TestNgContext context = test.getTestNg().getContext();
                filePaths.add(test.getTestNg().getDirectory().getScreenshots());
                filePaths.add(UriUtil.getAbsolute(context.getISuite().getName()));
                filePaths.add(UriUtil.getAbsolute(context.getITestContext().getName()));
                filePaths.add(UriUtil.getAbsolute(context.getITestClass().getName()));
                filePaths.add(UriUtil.getAbsolute(context.getIInvokedMethod().getTestMethod().getMethodName()));

                URI filePath = UriUtil.concatFiles(filePaths);
                File screenshotIn = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

                if (filename == null || filename.isEmpty()) {
                    DateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
                    Calendar cal = Calendar.getInstance();
                    filename = dateFormat.format(cal.getTime());
                }

                File screenshotOut = Resource.getFile(UriUtil.concatFiles(filePath,
                        UriUtil.getAbsolute(filename + Stringer.FilenameExtension.PNG.get())));
                FileUtils.copyFile(screenshotIn, screenshotOut);
            }
        } catch (IllegalStateException ignore) {
            // webDriver not set or used; ignore
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Unable to save screenshot", e);
        }
    }

}
