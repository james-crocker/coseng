package com.sios.stc.coseng.run;

import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;

import com.sios.stc.coseng.util.Stringer;

public final class TestNgSuiteXml {

    private Element suiteRoot = null;

    /*-
     * TestNG DTD http://testng.org/testng-1.0.dtd.php
     */

    private enum SuiteElement {
        SUITE("suite"), SUITE_FILES("suite-files"), SUITE_FILE("suite-file"), TEST("test"), CLASSES("classes"), CLASS(
                "class"), METHODS("methods"), INCLUDE("include"), GROUPS("groups"), DEFINE("define"), RUN("run"),;

        private final String element;

        private SuiteElement(String element) {
            this.element = element;
        }

        public String get() {
            return element;
        }

        @Override
        public String toString() {
            return name().toLowerCase() + Stringer.Separator.EQUALS.get() + Stringer.wrapBracket(element);
        }
    }

    private enum SuiteAttr {
        NAME("name"), PARALLEL("parallel"), THREAD_COUNT("thread-count"), PATH("path");

        private final String attribute;

        private SuiteAttr(String attribute) {
            this.attribute = attribute;
        }

        public String get() {
            return attribute;
        }

        @Override
        public String toString() {
            return name().toLowerCase() + Stringer.Separator.EQUALS.get() + Stringer.wrapBracket(attribute);
        }
    }

    TestNgSuiteXml(Element suiteRoot) {
        if (suiteRoot == null)
            throw new IllegalArgumentException("Element suiteRoot suite must be provided");
        this.suiteRoot = suiteRoot;
    }

    Attribute getSuiteName() {
        /*
         * A suite name is required per DTD.
         */
        return suiteRoot.getAttribute(SuiteAttr.NAME.get());
    }

    List<Element> getSuiteFiles() {
        return suiteRoot.getChildren(SuiteElement.SUITE_FILES.get());
    }

    List<Element> getSuiteFilesSuiteFiles(Element suiteFiles) {
        return suiteFiles.getChildren(SuiteElement.SUITE_FILE.get());
    }

    Attribute getSuiteFilesSuiteFilePath(Element suiteFilessuiteFile) {
        return suiteFilessuiteFile.getAttribute(SuiteAttr.PATH.get());
    }

}
