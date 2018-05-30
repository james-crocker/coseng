/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2018 SIOS Technology Corp.  All rights reserved.
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sios.stc.coseng.util.Http;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;
import com.sios.stc.coseng.util.Stringer.FilenameExtension;
import com.sios.stc.coseng.util.UriUtil;

public final class UriFound {

    private Directory         directory     = null;
    private TestNgContext     context       = null;
    private CosengUriValidate validate      = null;
    private MethodUriStat     methodUriStat = null;
    private Set<URI>          skipUris      = null;
    private Set<String>       skipTags      = null;

    private enum Filename {
        METHOD("uri"), ALL("all"), ALL_INVALID("all-invalid");

        private final String filename;

        private Filename(String filename) {
            this.filename = filename;
        }

        public String get() {
            return filename;
        }
    }

    UriFound(Directory directory, TestNgContext context, CosengUriValidate validate) {
        validate.validateAndPrepare();
        methodUriStat = new MethodUriStat(context);
        this.directory = directory;
        this.context = context;
        this.validate = validate;
    }

    public void put(String foundUri, String tag, URI route) {
        URI uri = null;
        try {
            uri = new URI(foundUri);
            methodUriStat.put(uri, tag, route);
            if (validate.isEnabled()) {
                Stat stat = methodUriStat.getStat(uri);
                stat.isValidUri = true;
                if (skipUriOrTag(uri, tag, route)) {
                    stat.isSkipped = true;
                } else if (stat.isConnected == false) {
                    URL url = uri.toURL();
                    Http.connect(url, validate.getRequestMethod(), validate.getBrowserVersion(),
                            validate.getRequestTimeoutMillisecond(), validate.isUseInsecureSsl(),
                            validate.isEnableJavaScript(), validate.isEnableCss(), validate.isEnableDownloadImages());
                    stat.responseCode = Http.getResponseCode(url);
                    stat.responseMessage = Http.getResponseMessage(url);
                    if (Http.isAccessible(url)) {
                        stat.isAccessible = true;
                    }
                    stat.isConnected = true;
                }
            }
        } catch (Exception e) {
            methodUriStat.put(foundUri, tag, route);
        }
    }

    void saveMethod() {
        try {
            LinkedList<URI> paths = new LinkedList<URI>();

            paths.add(directory.getUris());
            paths.add(UriUtil.getAbsolute(context.getISuite().getName()));
            paths.add(UriUtil.getAbsolute(context.getITestContext().getName()));
            paths.add(UriUtil.getAbsolute(context.getITestClass().getName()));
            paths.add(UriUtil.getAbsolute(context.getIInvokedMethod().getTestMethod().getMethodName()));

            URI path = UriUtil.concatFiles(paths);

            if (methodUriStat.hasMethodStat()) {
                URI validFilename = UriUtil.getAbsolute(Filename.METHOD.get() + FilenameExtension.CSV.get());
                URI validPath = UriUtil.concatFiles(path, validFilename);
                List<UriStat> uriStats = new ArrayList<UriStat>();
                uriStats.add(methodUriStat.getMethodStat());
                File validFile = Resource.getFile(validPath);
                save(validFile, uriStats);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to save found valid or invalid URIs", e);
        }
    }

    void saveAll() {
        /*
         * Test at this juncture should not have references to the TestNG Suite, Test,
         * Class or Method; place in root of report directory
         */
        try {
            if (!methodUriStat.isEmtpy()) {
                URI validPath = UriUtil.concatFiles(directory.getUris(),
                        UriUtil.getAbsolute(Filename.ALL.get() + FilenameExtension.CSV.get()));
                File validFile = Resource.getFile(validPath);
                save(validFile, methodUriStat.getStats());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to save all found valid and invalid URIs", e);
        }
    }

    private void save(File file, List<UriStat> uriStats) {
        /* Report in CSV style */
        String URI = "URI";
        String TAG = "Tag";
        String ROUTE = "FoundOnRoute";
        String SKIPPED = "SkippedValidate";
        String VALID = "Valid";
        String ACCESSIBLE = "Accessible";
        String RESPONSE_CODE = "ResponseCode";
        String RESPONSE_MESSAGE = "ResponseMessage";
        try {
            FileUtils.forceMkdirParent(file);
            /* Setup Apache Commons CSV */
            FileWriter fileWriter = new FileWriter(file);
            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withIgnoreEmptyLines().withQuoteMode(QuoteMode.ALL);
            CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            /* Create the header */
            Object[] header;
            boolean isValidate = validate.isEnabled();
            if (isValidate) {
                header = new String[] { URI, TAG, ROUTE, VALID, SKIPPED, ACCESSIBLE, RESPONSE_CODE, RESPONSE_MESSAGE };
            } else {
                header = new String[] { URI, TAG, ROUTE, VALID };
            }
            csvFilePrinter.printRecord(header);
            /* Create the line records */
            if (uriStats != null) {
                for (UriStat uriStat : uriStats) {
                    for (String uri : uriStat.keySet()) {
                        Stat stat = uriStat.get(uri);
                        for (String tag : stat.getTags()) {
                            List<String> record = new ArrayList<String>();
                            record.add(String.valueOf(uri));
                            record.add(StringUtils.join(stat.getTags(), Stringer.Separator.LIST.get()));
                            record.add(StringUtils.join(stat.getRoutes(tag), Stringer.Separator.LIST.get()));
                            record.add(String.valueOf(stat.isValidUri));
                            if (isValidate) {
                                record.add(String.valueOf(stat.isSkipped));
                                record.add(String.valueOf(stat.isAccessible));
                                record.add(String.valueOf(stat.responseCode));
                                record.add(stat.responseMessage);
                            }
                            csvFilePrinter.printRecord(record);
                        }
                    }
                }
            }
            csvFilePrinter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean skipUriOrTag(URI uri, String tag, URI route) {
        if (skipUris.contains(uri) || skipTags.contains(tag))
            return true;
        return false;
    }

}
