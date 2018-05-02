package com.sios.stc.coseng.aut;

import java.io.InputStream;
import java.net.URI;

import org.openqa.selenium.WebElement;

import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.UriUtil;

public class File {

    Test test = null;

    File(Test test) {
        this.test = test;
    }

    public void upload(WebElement uploadElement, URI resource, boolean saveResource) {
        if (uploadElement == null || resource == null)
            throw new IllegalArgumentException("Field uploadElement and resource must be provided");
        if (!uploadElement.isDisplayed() || uploadElement.getAttribute("readonly") != null)
            throw new IllegalStateException("Web element uploadElement must be displayed and not readonly");
        String name = UriUtil.getLastName(resource);
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Unable to get resource name");
        try {
            InputStream fileInput = Resource.getInputStream(resource);
            java.io.File resourceDir = Resource.getFile(test.getTestNg().getDirectory().getResources());

            java.io.File localResource = null;

            if (saveResource) {
                localResource = new java.io.File(resourceDir + java.io.File.separator + name);
            } else {
                localResource = java.io.File.createTempFile(name, null);
                localResource.deleteOnExit();
            }

            Resource.saveInputStream(fileInput, localResource.toURI());
            /*
             * NOTE! ((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
             * in WebDriverLifecycle.startWebDriver(Test)
             * 
             * If set here instead of earlier when starting the web driver you may get
             * exceptions that the "path is not absolute".
             */
            uploadElement.sendKeys(localResource.getCanonicalPath());
        } catch (Exception e) {
            throw new RuntimeException("Unable to upload file [{}]; assure file exists", e);
        }
    }

}
