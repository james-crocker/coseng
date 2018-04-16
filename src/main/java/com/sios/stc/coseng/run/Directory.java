package com.sios.stc.coseng.run;

import java.net.URI;

import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.UriUtil;

public final class Directory {

    private URI output      = null;
    private URI reports     = null;
    private URI resources   = null;
    private URI uris        = null;
    private URI screenshots = null;
    private URI logs        = null;

    public URI getOutput() {
        return output;
    }

    public URI getReports() {
        return reports;
    }

    public URI getResources() {
        return resources;
    }

    public URI getUris() {
        return uris;
    }

    public URI getScreenshots() {
        return screenshots;
    }

    public URI getLogs() {
        return logs;
    }

    URI getOutputDirectory(String id, URI outputDirectory) {
        try {
            if (outputDirectory != null)
                output = UriUtil.getCanonical(outputDirectory);
            else
                output = UriUtil.getCanonical("coseng-tests");
            output = UriUtil.concatFiles(output, UriUtil.getAbsolute(id));
            reports = UriUtil.concatFiles(output, UriUtil.getAbsolute("reports"));
            resources = UriUtil.concatFiles(output, UriUtil.getAbsolute("resources"));
            uris = UriUtil.concatFiles(output, UriUtil.getAbsolute("uris"));
            screenshots = UriUtil.concatFiles(output, UriUtil.getAbsolute("screenshots"));
            logs = UriUtil.concatFiles(output, UriUtil.getAbsolute("logs"));
        } catch (Exception e) {
            throw new CosengConfigException("Unable to construct output directory paths", e);
        }
        return output;
    }

    void make() {
        Resource.makeDirectoryClean(output);
        Resource.makeDirectory(reports);
        Resource.makeDirectory(resources);
        Resource.makeDirectory(uris);
        Resource.makeDirectory(screenshots);
        Resource.makeDirectory(logs);
    }

}
