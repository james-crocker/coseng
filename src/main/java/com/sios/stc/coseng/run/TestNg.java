package com.sios.stc.coseng.run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.testng.xml.XmlSuite;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.integration.Integrator;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;
import com.sios.stc.coseng.util.UriUtil;

public final class TestNg {

    @Expose
    private URI      suiteXml                    = null;
    @Expose
    private URI      outputDirectory             = null;
    @Expose
    private Boolean  skipRemainingTestsOnFailure = null;
    @Expose
    private Set<URI> integratorsJson             = null;
    @Expose
    private Integer  verbosity                   = null;

    private Directory            directory   = null;
    private List<Integrator>     integrators = new ArrayList<Integrator>();
    private List<XmlSuite>       xmlSuites   = new ArrayList<XmlSuite>();
    private TestNgContext        context     = new TestNgContext();
    private Map<String, Integer> suiteNameId = new LinkedHashMap<String, Integer>();

    List<XmlSuite> getXmlSuites() {
        return xmlSuites;
    }

    public List<Integrator> getIntegrators() {
        List<Integrator> newList = new ArrayList<Integrator>();
        newList.addAll(integrators);
        return newList;
    }

    public Integrator getIntegrator(Class<?> clazz) {
        for (Integrator integrator : getIntegrators()) {
            if (integrator.getClass().equals(clazz)) {
                return integrator;
            }
        }
        return null;
    }

    public int getVerbosity() {
        return verbosity.intValue();
    }

    public Directory getDirectory() {
        return directory;
    }

    public boolean isSkipRemainingTestsOnFailure() {
        return skipRemainingTestsOnFailure.booleanValue();
    }

    public void setSkipRemainingTestsOnFailure(boolean skipTests) {
        skipRemainingTestsOnFailure = skipTests;
    }

    public TestNgContext getContext() {
        return context;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "directory", "integrators", "xmlSuites", "context",
                "suiteNameId");
    }

    void validateAndPrepare(Test test) {
        try {
            /* Directories FIRST! */
            directory = new Directory();
            outputDirectory = directory.getOutputDirectory(test.getId(), outputDirectory);
            directory.make();
            /* Suite */
            if (suiteXml == null)
                throw new IllegalArgumentException("Field suiteXml must have a TestNG Suite XML");

            URI suitesDir = UriUtil.concatFiles(directory.getResources(), UriUtil.getAbsolute("testng-suites"));
            suiteXml = UriUtil.getCanonical(suiteXml);

            List<String> suiteXmls = new ArrayList<String>();
            suiteXmls.add(getVerifiedSuiteXml(suitesDir, suiteXml, null));

            XmlSuite xmlSuite = new XmlSuite();
            xmlSuite.setName(test.getId());

            xmlSuite.setSuiteFiles(suiteXmls);
            xmlSuites.add(xmlSuite);
            if (skipRemainingTestsOnFailure == null)
                skipRemainingTestsOnFailure = false;
            /* Verbosity */
            if (verbosity == null)
                verbosity = 0;
            if (verbosity < 0 || verbosity > 10)
                throw new IllegalArgumentException("Invalid verbosity; valid 0..10");
            /*
             * Integrators
             * 
             * Config Integrators are not inline with the Test JSON, they are a URI list
             * reference; so need to validate a bit differently from the rest of the
             * Config/Run classes.
             */
            if (integratorsJson != null) {
                if (integratorsJson.contains(null)) {
                    throw new IllegalArgumentException("Field integratorJsons may not contain null elements");
                } else {
                    Set<URI> canonicalIntegrators = new HashSet<URI>();
                    for (URI uri : integratorsJson) {
                        URI canonicalIntegrator;
                        try {
                            canonicalIntegrator = UriUtil.getCanonical(uri);
                        } catch (URISyntaxException | IOException e) {
                            throw new IllegalArgumentException(e);
                        }
                        canonicalIntegrators.add(canonicalIntegrator);
                        TestNgIntegrator integrator = (TestNgIntegrator) Resource.getObjectFromJson(canonicalIntegrator,
                                TestNgIntegrator.class);
                        Integrator concreteIntegrator = integrator.validateAndPrepare(test);
                        integrators.add(concreteIntegrator);
                    }
                    integratorsJson = canonicalIntegrators;
                }
            }
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

    private String getVerifiedSuiteXml(URI resourcesDirectory, URI suiteXmlSource, String prefix) {

        try {
            if (!UriUtil.isFileScheme(resourcesDirectory))
                throw new IllegalArgumentException("Resources directory " + Stringer.wrapBracket(resourcesDirectory)
                        + " must have URI scheme " + Stringer.wrapBracket(UriUtil.Uri.SCHEME_FILE.get()));

            Document jdomSuite = getJdomSuite(suiteXmlSource);
            TestNgSuiteXml xml = new TestNgSuiteXml(jdomSuite.getRootElement());

            /*
             * Set the suite name; a suite name is required per DTD. Add .xml later as the
             * top suite name is used to construct any suite-files sub directories.
             */
            Attribute suiteName = xml.getSuiteName();
            /*
             * While the first suite XML file doesn't need to be unique, subsequent
             * instances of same named suites will need to be uniquely identified; otherwise
             * TestNG will rename them. eg. <suite name="test1"...> used in other
             * <suite-files>. The first "test1" is unchanged. TestNG will rename the second
             * instance as "test1 (0)". Track suite names and append id if current suite
             * name matches prior names.
             */

            if (prefix != null) {
                suiteName.setValue(prefix + Stringer.Separator.FILENAME.get() + suiteName.getValue());
            }

            String sSuiteName = suiteName.getValue();
            if (suiteNameId.containsKey(sSuiteName)) {
                Integer id = suiteNameId.get(sSuiteName);
                id++;
                suiteNameId.put(sSuiteName, id);
                suiteName.setValue(sSuiteName + Stringer.Separator.FILENAME.get() + id);
                suiteNameId.put(suiteName.getValue(), 0);
            } else {
                suiteNameId.put(sSuiteName, 0);
            }

            /* Now scan for <suite-files> and recurse */
            int suiteFilesCount = 0;
            URI suitesDirectory = null;
            for (Element e1 : xml.getSuiteFiles()) {
                suiteFilesCount++;
                suitesDirectory = UriUtil.concatFiles(resourcesDirectory, UriUtil.getAbsolute(suiteName.getValue()));
                suitesDirectory = UriUtil.concatFiles(suitesDirectory,
                        UriUtil.getAbsolute("suite-files" + suiteFilesCount));

                for (Element e2 : xml.getSuiteFilesSuiteFiles(e1)) {
                    /* Path required per DTD */
                    Attribute suiteFilePath = xml.getSuiteFilesSuiteFilePath(e2);
                    URI suiteUriSource = UriUtil.getCanonical(suiteFilePath.getValue());
                    suiteFilePath.setValue(getVerifiedSuiteXml(suitesDirectory, suiteUriSource,
                            suiteName.getValue() + Stringer.Separator.FILENAME.get() + "sf" + suiteFilesCount));
                }
            }

            // TODO Add classes and methods available validator; scan suite xml for expected
            // invokable method count.

            URI modSuiteXml = UriUtil.concatFiles(resourcesDirectory,
                    UriUtil.getAbsolute(suiteName.getValue() + Stringer.FilenameExtension.XML.get()));

            XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
            // xout.output(jdomSuite, System.out);
            File suiteXmlFile = Resource.getFile(modSuiteXml);
            FileUtils.forceMkdirParent(suiteXmlFile);
            suiteXmlFile.createNewFile();
            OutputStream out = new FileOutputStream(suiteXmlFile, false);
            xout.output(jdomSuite, out);
            out.close();
            return suiteXmlFile.getCanonicalPath();
        } catch (Exception e) {
            throw new CosengConfigException(
                    "Unable to use TestNG Suite XML resource " + Stringer.wrapBracket(suiteXmlSource), e);
        }
    }

    private Document getJdomSuite(URI suiteXmlSource) {
        InputStream suiteInput = Resource.getInputStream(suiteXmlSource);
        /* Read original suite */
        SAXBuilder jdomBuilder = new SAXBuilder();
        Document jdomSuite = null;
        try {
            jdomSuite = jdomBuilder.build(suiteInput);
            suiteInput.close();
        } catch (Exception e) {
            throw new CosengConfigException("Unable to deserialize TestNG Suite XML " + Stringer.wrapBracket(suiteXml),
                    e);
        }
        return jdomSuite;
    }

}
