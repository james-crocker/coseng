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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.util.Stringer;

public final class Tests implements Iterable<Test> {

    @Expose
    private List<Test> cosengTests = null;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this).toString();
    }

    List<URI> getOutputDirectories() {
        List<URI> directories = new ArrayList<URI>();
        for (Test test : cosengTests) {
            directories.add(test.getTestNg().getDirectory().getOutput());
        }
        return directories;
    }

    List<String> getFailed() {
        List<String> failed = new ArrayList<String>();
        for (Test test : cosengTests) {
            if (test.isFailed()) {
                failed.add(test.getId());
            }
        }
        return failed;
    }

    void validateAndPrepare() {
        try {
            if (cosengTests == null || cosengTests.contains(null))
                throw new IllegalArgumentException("Tests must be provided");
            Set<String> ids = new HashSet<String>();
            for (int i = 0; i < cosengTests.size(); i++) {
                Test test = cosengTests.get(i);
                String testId = test.getId();
                if (testId == null || testId.isEmpty())
                    throw new IllegalArgumentException(
                            "Test number " + Stringer.wrapBracket((i + 1)) + " field id must be provided");
                if (ids.contains(testId))
                    throw new IllegalArgumentException("Test number " + Stringer.wrapBracket((i + 1))
                            + StringUtils.SPACE + Stringer.wrapBracket(testId)
                            + " must be unique; another test with the same id exists");
                ids.add(testId);
                test.validateAndPrepare();
            }
        } catch (Exception e) {
            throw new CosengConfigException("Could not validate and prepare cosengTests", e);
        }
    }

    @Override
    public Iterator<Test> iterator() {
        return cosengTests.iterator();
    }

}
