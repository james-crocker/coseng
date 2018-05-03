package com.sios.stc.coseng;

import java.net.URI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.run.TestJsonDeserializer;
import com.sios.stc.coseng.run.Tests;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;

public final class CliArgument {

    /*
     * Parse the command line arguments and attempt to get the referenced resources.
     * Print help usage and exit with value [0] when -help option set. Print help
     * usage and exit with value [1] for mis-configured options.
     *
     * @param args the args
     * 
     * @throws CosengException the coseng exception on parse errors and absent or
     * invalid resources
     * 
     * @see com.sios.stc.coseng.config.BrowserNode.webdriver.node.SeleniumNode
     * 
     * @see com.sios.stc.coseng.config.Test
     * 
     * @see com.sios.stc.coseng.tests.Tests
     * 
     * @since 2.0
     * 
     * @version.coseng
     */
    static Tests getTests(String[] args) {
        URI testsJsonResourceUri = null;
        HelpFormatter formatter = new HelpFormatter();
        String optHelp = "help";
        String optTest = "test";
        String helpUsage = "Valid COSENG command line options.";
        /* Define the accepted command line options */
        Options options = new Options();
        options.addOption(optHelp, false, "Help");
        options.addOption(optTest, true, "Tests JSON configuration resource URI");
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cli = parser.parse(options, args);
            /* Get the option values */
            /* Check option expectations */
            if (cli.hasOption(optHelp)) {
                formatter.printHelp(helpUsage, options);
            } else {
                if (!cli.hasOption(optTest) || cli.getOptionValue(optTest).isEmpty()) {
                    System.out.println("-" + optTest + " <arg> required");
                    System.exit(ExitStatus.FAILURE.getStatus());
                }
                testsJsonResourceUri = new URI(cli.getOptionValue(optTest));
            }
            return (Tests) Resource.getObjectFromJson(testsJsonResourceUri, TestJsonDeserializer.get(), Tests.class);
        } catch (Exception e) {
            throw new CosengConfigException(
                    "Unable to parse TestsConfig JSON resource " + Stringer.wrapBracket(testsJsonResourceUri), e);
        }
    }

}
