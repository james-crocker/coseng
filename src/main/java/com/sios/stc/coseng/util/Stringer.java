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
package com.sios.stc.coseng.util;

public class Stringer {

    public static final String UNKNOWN = "unknown";
    public static final String NULL    = "null";

    public enum Html {
        LINE_BREAK("<br>"), BOLD_BEGIN("<strong>"), BOLD_END("</strong>"), ITALIC_BEGIN("<em>"), ITALIC_END("</em>");

        private final String element;

        Html(String element) {
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

    public enum Punctuation {
        SINGLE_QUOTE("'"), DOUBLE_QUOTE("\""), BRACKET_OPEN("["), BRACKET_CLOSE("]"), PARENS_OPEN("("), PARENS_CLOSE(
                ")");

        private final String element;

        Punctuation(String element) {
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

    public enum FilenameExtension {
        XML(".xml"), PNG(".png"), CSV(".csv");

        private final String extension;

        FilenameExtension(String extension) {
            this.extension = extension;
        }

        public String get() {
            return extension;
        }

        @Override
        public String toString() {
            return name().toLowerCase() + Stringer.Separator.EQUALS.get() + Stringer.wrapBracket(extension);
        }
    }

    public enum Separator {
        LIST(", "), FILENAME("_"), EQUALS("=");

        private String separator = null;

        private Separator(String separator) {
            this.separator = separator;
        }

        public String get() {
            return separator;
        }

        @Override
        public String toString() {
            return name().toLowerCase() + Stringer.Separator.EQUALS.get() + Stringer.wrapBracket(separator);
        }
    }

    private String string = null;

    public Stringer() {
        // do nothing; string already null
    }

    public Stringer(Object object) {
        string = getString(object);
    }

    public static String keyValue(Object o1, Object o2) {
        return getString(o1) + Stringer.Separator.EQUALS.get() + Stringer.wrapBracket(o2);
    }

    public Stringer htmlBold() {
        string = htmlBold(string);
        return this;
    }

    public static String htmlBold(Object object) {
        return wrap(object, Html.BOLD_BEGIN.get(), Html.BOLD_END.get());
    }

    public Stringer htmlItalic() {
        string = htmlItalic(string);
        return this;
    }

    public static String htmlItalic(Object object) {
        return wrap(object, Html.ITALIC_BEGIN.get(), Html.ITALIC_END.get());
    }

    public Stringer htmlLineBreak() {
        string = htmlLineBreak(string);
        return this;
    }

    public static String htmlLineBreak(Object object) {
        return getString(object) + Html.LINE_BREAK.get();
    }

    public Stringer wrapSingleQuote() {
        string = wrapDoubleQuote(string);
        return this;
    }

    public static String wrapSingleQuote(Object object) {
        return wrap(object, Punctuation.SINGLE_QUOTE.get(), Punctuation.SINGLE_QUOTE.get());
    }

    public Stringer wrapDoubleQuote() {
        string = wrapDoubleQuote(string);
        return this;
    }

    public static String wrapDoubleQuote(Object object) {
        return wrap(object, Punctuation.DOUBLE_QUOTE.get(), Punctuation.DOUBLE_QUOTE.get());
    }

    public Stringer wrapBracket() {
        string = wrapBracket(string);
        return this;
    }

    public static String wrapBracket(Object object) {
        return wrap(object, Punctuation.BRACKET_OPEN.get(), Punctuation.BRACKET_CLOSE.get());
    }

    public Stringer wrapParentheses() {
        string = wrapParentheses(string);
        return this;
    }

    public static String wrapParentheses(Object object) {
        return wrap(object, Punctuation.PARENS_OPEN.get(), Punctuation.PARENS_CLOSE.get());
    }

    public Stringer clear() {
        string = null;
        return this;
    }

    @Override
    public String toString() {
        return string;
    }

    private static String getString(Object object) {
        return object == null ? NULL : object.toString();
    }

    private static String wrap(Object object, String prepend, String append) {
        return prepend + getString(object) + append;
    }

}
