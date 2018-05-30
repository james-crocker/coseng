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
package com.sios.stc.coseng;

import com.sios.stc.coseng.run.CosengTests;
import com.sios.stc.coseng.run.Tests;

/**
 * The Class RunTests. The public main class for executing Concurrent Selenium
 * TestNG (COSENG) suites.
 *
 * @since 2.0
 * @version.coseng
 */
public final class RunTests extends CosengTests {

    /**
     * The main method for executing Concurrent Selenium TestNG (COSENG) suites.
     * Requires a Tests JSON resource.
     *
     * @param args
     *            the command line arguments to configure a COSENG test execution;
     *            -help for usage.
     * @since 3.0.0
     * @version.coseng
     */

    public static void main(final String[] args) {
        Global.set();
        Tests tests = CliArgument.getTests(args);
        CosengTests.run(tests);
    }

}
