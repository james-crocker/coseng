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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.sios.stc.coseng.run.TestJsonDeserializer;
import com.sios.stc.coseng.run.Tests;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;

final class CliArgument {

    static Tests getTests(String[] args) {
        URI testsJsonResourceUri = null;
        boolean hasHelp = false;
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(120);
        helpFormatter.setLeftPadding(0);

        String header = "Concurrent Selenium TestNG (COSENG) provides concurrent Selenium web driver lifecycle "
                + "management leveraging the TestNG testing framework.";
        String footer = "Documentation: https://github.com/siostechcorp/coseng/wiki";

        Options options = new Options();
        String optHelp = "help";
        String unoHelp = "h";
        Option helpOption = Option.builder(unoHelp).longOpt(optHelp).hasArg(false).required(false)
                .desc("Program usage, help and documentation information").build();
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cli = null;

        /*
         * As -t is required it would throw an exception if user only providing -h for
         * help. Parse twice and flag if help requested.
         */
        try {
            cli = parser.parse(options, args);
            if (cli.hasOption(unoHelp) || cli.hasOption(optHelp)) {
                hasHelp = true;
            }
        } catch (ParseException e) {
            // do nothing
        }

        String optTests = "tests";
        String unoTests = "t";
        Option testsOption = Option.builder(unoTests).longOpt(optTests).hasArg(true).required(true)
                .desc("Tests JSON configuration resource URI").build();
        options.addOption(testsOption);

        try {
            cli = parser.parse(options, args);
            testsJsonResourceUri = new URI(cli.getOptionValue(optTests));
            return (Tests) Resource.getObjectFromJson(testsJsonResourceUri, TestJsonDeserializer.get(), Tests.class);
        } catch (ParseException e) {
            if (hasHelp) {
                helpFormatter.printHelp("coseng", header, options, footer, true);
                System.exit(ExitStatus.SUCCESS.getStatus());
                return null;
            } else {
                throw new IllegalArgumentException(e);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                    "Unable to parse TestsConfig JSON resource " + Stringer.wrapBracket(testsJsonResourceUri), e);
        }
    }

}
