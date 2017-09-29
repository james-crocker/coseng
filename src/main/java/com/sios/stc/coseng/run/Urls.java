/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2017 SIOS Technology Corp.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sios.stc.coseng.run;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;

import com.sios.stc.coseng.Common;
import com.sios.stc.coseng.util.Http;

public class Urls {

    private static final String URLS_FILENAME            = "urls";
    private static final String URLS_FILENAME_ALL_PREFIX = "all";
    private static final String URLS_FILENAME_EXT        = ".csv";

    private static boolean hasVerifiedAllUrl = false;
    private static int     findUrlCount      = 0;

    /* Global collection of found URLs; composed of URL, tags and routes. */
    private static Map<String, Map<String, Set<String>>> allUrlTagRoutes =
            new HashMap<String, Map<String, Set<String>>>();

    /* Atomic URLs, tag and routes; url responses should *not* be verified. */
    private Map<String, Map<String, Set<String>>> urlTagRoutes =
            new HashMap<String, Map<String, Set<String>>>();

    protected void putUrl(String url, String tag, String route) {
        /* tag may be null or empty */
        if (url != null && route != null) {
            /* Global */
            putUrl(url, tag, route, allUrlTagRoutes);
            /* Atomic */
            putUrl(url, tag, route, urlTagRoutes);
        }
    }

    private static void putUrl(String url, String tag, String route,
            Map<String, Map<String, Set<String>>> data) {
        /* tag may be null or empty */
        if (url != null && route != null) {
            if (data.containsKey(url)) {
                /* adjust existing */
                Map<String, Set<String>> tagRoutes = data.get(url);
                if (tagRoutes.containsKey(tag)) {
                    Set<String> routes = tagRoutes.get(tag);
                    routes.add(route);
                } else {
                    Set<String> routes = new HashSet<String>();
                    routes.add(route);
                    tagRoutes.put(tag, routes);
                }
            } else {
                /* new url */
                Set<String> routes = new HashSet<String>();
                routes.add(route);
                Map<String, Set<String>> tagRoutes = new HashMap<String, Set<String>>();
                tagRoutes.put(tag, routes);
                data.put(url, tagRoutes);
            }
        }
    }

    private static void verifyAllAccessible(Test test) {
        if (test != null && test.isAllowFindUrls() && test.isAllowVerifyUrls()) {
            Logging.logResults(test, org.apache.logging.log4j.Level.INFO,
                    Logging.TASK_PREFIX + "Verify all found URL", null, null);
            Set<String> skipUrls = test.getSkipUrls();
            Set<String> skipTags = test.getSkipUrlTags();
            for (String url : allUrlTagRoutes.keySet()) {
                boolean skip = false;
                if (skipUrls != null && skipUrls.contains(url)) {
                    skip = true;
                }
                for (String tag : allUrlTagRoutes.get(url).keySet()) {
                    if (skipTags != null && skipTags.contains(tag)) {
                        skip = true;
                    }
                    if (skip) {
                        Logging.logResults(test, org.apache.logging.log4j.Level.WARN,
                                Logging.MESSAGE_PREFIX + "Skip URL [" + url + "], tag [" + tag
                                        + "]; found on routes [" + allUrlTagRoutes.get(url).get(tag)
                                        + "]",
                                Assertions.SUCCESS, Assertions.SUCCESS);
                        continue;
                    }
                    if (!Http.isAccessible(url)) {
                        org.apache.logging.log4j.Level logLevel;
                        if (test.isFailTestOnFailVerifyUrls()) {
                            logLevel = org.apache.logging.log4j.Level.ERROR;
                            test.setIsFailed(true);
                        } else {
                            logLevel = org.apache.logging.log4j.Level.WARN;
                        }
                        Logging.logResults(test, logLevel,
                                Logging.MESSAGE_PREFIX + "URL [" + url + "], tag [" + tag
                                        + "], response code [" + Http.getResponseCode(url)
                                        + "]; found on routes "
                                        + StringUtils.join(allUrlTagRoutes.get(url).get(tag),
                                                Common.DEFAULT_LIST_SEPARATOR),
                                Assertions.SUCCESS, Assertions.FAIL);
                    }
                }
            }
        }
        hasVerifiedAllUrl = true;
    }

    protected void saveFound(Test test) {
        if (test != null && test.isAllowFindUrls()) {
            try {
                ArrayList<String> dirPaths = new ArrayList<String>();
                dirPaths.add(test.getReportDirectoryFile().getAbsolutePath());
                dirPaths.add(Common.TESTNG_REPORT_COSENG_DIR);
                dirPaths.add(test.getTestNgSuite().getName());
                dirPaths.add(test.getTestNgTest().getName());
                dirPaths.add(test.getTestNgClass().getName());
                dirPaths.add(test.getTestNgMethod().getTestMethod().getMethodName());
                String dirPath = StringUtils.join(dirPaths, File.separator);
                File file = new File(
                        dirPath + File.separator + URLS_FILENAME + Common.FILENAME_SEPARATOR
                                + String.valueOf(findUrlCount) + URLS_FILENAME_EXT);
                /*
                 * Regardless of verified allowed or not; never do for
                 * individual finds
                 */
                save(dirPath, file, urlTagRoutes, false);
                findUrlCount++;
                Logging.logResults(test, org.apache.logging.log4j.Level.INFO,
                        Logging.TASK_PREFIX + "Save found URLs [" + file.getCanonicalPath() + "]",
                        Assertions.SUCCESS, Assertions.SUCCESS);
            } catch (Exception e) {
                /* Don't throw exception; fail with notice */
                Logging.logResults(test, org.apache.logging.log4j.Level.ERROR,
                        Logging.TASK_PREFIX + "Unable to save found URLs;" + e.getMessage(),
                        Assertions.SUCCESS, Assertions.FAIL);
            }
        }
    }

    protected static void saveAllFound(Test test) {
        /*
         * Test at this juncture should not have references to the TestNG Suite,
         * Test, Class or Method; place in root of report directory
         */
        if (test != null && test.isAllowFindUrls()) {
            try {
                if (test.isAllowVerifyUrls() && !hasVerifiedAllUrl) {
                    verifyAllAccessible(test);
                }
                ArrayList<String> dirPaths = new ArrayList<String>();
                dirPaths.add(test.getReportDirectoryFile().getAbsolutePath());
                dirPaths.add(Common.TESTNG_REPORT_COSENG_DIR);
                String dirPath = StringUtils.join(dirPaths, File.separator);
                File file = new File(dirPath + File.separator + URLS_FILENAME_ALL_PREFIX
                        + Common.FILENAME_SEPARATOR + URLS_FILENAME + URLS_FILENAME_EXT);
                save(dirPath, file, allUrlTagRoutes, hasVerifiedAllUrl);
                Logging.logResults(
                        test, org.apache.logging.log4j.Level.INFO, Logging.TASK_PREFIX
                                + "Save all found URLs [" + file.getCanonicalPath() + "]",
                        Assertions.SUCCESS, Assertions.SUCCESS);
            } catch (Exception e) {
                /* Don't throw exception; fail with notice */
                Logging.logResults(test, org.apache.logging.log4j.Level.ERROR,
                        Logging.TASK_PREFIX + "Unable to save all found URLs;" + e.getMessage(),
                        Assertions.SUCCESS, Assertions.FAIL);
            }
        }
    }

    private static void save(String dirPath, File file, Map<String, Map<String, Set<String>>> data,
            boolean verify) throws RuntimeException {
        /* Report in CSV style */
        String URL = "URL";
        String TAG = "Tag";
        String ROUTE = "Route";
        String RESPONSE = "Response";
        if (dirPath == null || dirPath.isEmpty() || file == null || data == null
                || data.isEmpty()) {
            return;
        }
        try {
            /* Make directory */
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            /* Setup Apache Commons CSV */
            FileWriter fileWriter = new FileWriter(file);
            CSVFormat csvFileFormat =
                    CSVFormat.DEFAULT.withIgnoreEmptyLines().withQuoteMode(QuoteMode.ALL);
            CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            /* Create the header */
            Object[] header;
            if (verify) {
                header = new String[] { URL, RESPONSE, TAG, ROUTE };
            } else {
                header = new String[] { URL, TAG, ROUTE };
            }
            csvFilePrinter.printRecord(header);
            /* Create the line records */
            for (String url : data.keySet()) {
                for (String tag : data.get(url).keySet()) {
                    for (String route : data.get(url).get(tag)) {
                        List<String> record = new ArrayList<String>();
                        record.add(url);
                        if (verify) {
                            record.add(Http.getResponseCode(url).toString());
                        }
                        record.add(tag);
                        record.add(route);
                        csvFilePrinter.printRecord(record);
                    }
                }
            }
            csvFilePrinter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
