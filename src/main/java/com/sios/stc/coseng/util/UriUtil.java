package com.sios.stc.coseng.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class UriUtil {

    public enum Uri {
        SCHEME_FILE("file"), SCHEME_RESOURCE("resource"), PATH_SEPARATOR("/");

        private String value = null;

        private Uri(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }

        public boolean equals(String value) {
            return this.value.equals(value);
        }
    }
    // private static final String SCHEME_FILE = "file";
    // private static final String SCHEME_RESOURCE = "resource";
    // private static final String PATH_SEPARATOR = "/";

    public static URI getAbsolute(String resource) throws URISyntaxException, IOException {
        return getCanonical(Uri.PATH_SEPARATOR.get() + resource);
    }

    public static URI getCanonical(String resource) throws URISyntaxException, IOException {
        if (resource == null || resource.isEmpty()) {
            throw new IllegalArgumentException("Field resource must be provided");
        }
        return getCanonical(new URI(null, null, resource, null));
    }

    public static URI getCanonical(URI uri) throws URISyntaxException, IOException {
        /*-
         * Presume a File resource
         * URI: "dir1/dir2/file" (absolute false, opaque false)
         * URI: "/dir1/dir2/file" (absolute false, opaque false)
         * 
         * Definite File resource
         * URI: "file:/dir1/dir2/file" (absolute true, opaque false)
         * URI: "file:dir1/dir2/file (absolute true, opaque true)
         * 
         * Java Class Resource (no adjustments; let class loader getResource handle relative or root search)
         * URI: "resource:/dir1/dir2/file" relative (absolute true, opaque false)
         * URI: "resource:dir1/dir2/file" from root (absolute true, opaque true)
         * 
         * URL Resource
         * URI: "http://co.com/java.jar" (absolute true, opaque false)
         * URI: "ftp://co.com/suite.xml" (absolute true, opaque false)
         * 
         */
        if (uri.getScheme() == null || uri.getScheme().isEmpty()) {
            if (uri.getPath().startsWith(Uri.PATH_SEPARATOR.get())) {
                /*- Treat as absolute; URI: "/dir1/dir2/file" => file:/dir1/dir2/file */
                return new URI(Uri.SCHEME_FILE.get(), null, uri.getPath(), null).normalize();
            } else {
                /*- Treat as relative; URI: "dir1/dir2/file" => file:/<current_dir>/dir1/dir2/file */
                return new URI(Uri.SCHEME_FILE.get(), null,
                        new File(StringUtils.EMPTY).getCanonicalPath() + Uri.PATH_SEPARATOR.get() + uri.getPath(), null)
                                .normalize();
            }
        } else if (isFileScheme(uri) && uri.isOpaque()) {
            /*- Treat as relative; URI "file:dir1/dir2/file" => file:/<current_dir>/dir1/dir2/file */
            return new URI(Uri.SCHEME_FILE.get(), null, new File(StringUtils.EMPTY).getCanonicalPath()
                    + Uri.PATH_SEPARATOR.get() + uri.getSchemeSpecificPart(), null).normalize();
        }
        return uri;
    }

    public static boolean isFileScheme(URI uri) {
        if (uri != null) {
            return Uri.SCHEME_FILE.equals(uri.getScheme());
        }
        return false;
    }

    public static boolean isResourceScheme(URI uri) {
        if (uri != null) {
            return Uri.SCHEME_RESOURCE.equals(uri.getScheme());
        }
        return false;
    }

    public static URI concatFiles(URI uri, List<String> resources) throws URISyntaxException, IOException {
        if (uri == null || resources == null) {
            throw new IllegalArgumentException("Field uri and resources must be provided");
        }
        LinkedList<URI> uris = new LinkedList<URI>();
        uris.add(uri);
        for (String resource : resources) {
            uris.add(getCanonical(resource));
        }
        return concatFiles(uris);
    }

    public static URI concatFiles(List<String> resources) throws URISyntaxException, IOException {
        if (resources == null) {
            throw new IllegalArgumentException("Field resources must be provided");
        }
        LinkedList<URI> uris = new LinkedList<URI>();
        for (String resource : resources) {
            uris.add(getCanonical(resource));
        }
        return concatFiles(uris);
    }

    public static URI concatFiles(LinkedList<URI> uris) throws URISyntaxException {
        if (uris == null) {
            throw new IllegalArgumentException("URIs may not be null");
        }
        if (uris.size() == 1) {
            return uris.pop();
        } else {
            return concatFiles(uris.pop(), concatFiles(uris));
        }
    }

    public static URI concatFiles(String uri1, String uri2) throws URISyntaxException, IOException {
        return concatFiles(getCanonical(uri1), getCanonical(uri2));
    }

    public static URI concatFiles(URI uri1, String uri2) throws URISyntaxException, IOException {
        return concatFiles(uri1, getCanonical(uri2));
    }

    public static URI concatFiles(String uri1, URI uri2) throws URISyntaxException, IOException {
        return concatFiles(getCanonical(uri1), uri2);
    }

    public static URI concatFiles(URI uri1, URI uri2) throws URISyntaxException {
        if (!isFileScheme(uri1) || !isFileScheme(uri2)) {
            throw new IllegalArgumentException(
                    "Both URI may not be null and must have scheme " + Stringer.wrapBracket(Uri.SCHEME_FILE.get()));
        } else {
            return new URI(Uri.SCHEME_FILE.get(), null, uri1.getPath() + uri2.getPath(), null).normalize();
        }
    }

}
