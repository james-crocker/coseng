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
package com.sios.stc.coseng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import wiremock.com.google.common.io.CharStreams;

/**
 * The Class Resource which will attempt to find a requested resource on the
 * file system or within the classpath.
 *
 * @since 2.0
 * @version.coseng
 */
public final class Resource {

    public static File getFile(URI resource) {
        return new File(resource);
    }

    public static String getString(URI resource) {
        try {
            return getString(getInputStream(resource));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get resource " + Stringer.wrapBracket(resource), e);
        }
    }

    private static String getString(InputStream inputStream) {
        try {
            return CharStreams.toString(new InputStreamReader(inputStream));
        } catch (Exception e) {
            throw new RuntimeException("Unable to read input stream", e);
        }
    }

    public static InputStream getInputStream(final URI uri) {
        String msg = "Resource " + Stringer.wrapBracket(uri) + " is not available";
        InputStream inputStream = null;
        try {
            URI canonicalUri = UriUtil.getCanonical(uri);
            if (UriUtil.isFileScheme(canonicalUri)) {
                File file = new File(canonicalUri);
                if (file.exists() && file.canRead()) {
                    inputStream = new FileInputStream(file);
                }
            } else if (UriUtil.isResourceScheme(canonicalUri)) {
                if (canonicalUri.isOpaque()) {
                    Class<?> callerClass = getCallerClass();
                    msg += " relative to calling class " + Stringer.wrapBracket(callerClass.getName());
                    inputStream = callerClass.getResourceAsStream(canonicalUri.getPath());
                } else {
                    inputStream = Resource.class.getResourceAsStream(canonicalUri.getPath());
                }
            } else {
                inputStream = canonicalUri.toURL().openStream();
            }
            if (inputStream == null)
                throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        }
        return inputStream;
    }

    private static Class<?> getCallerClass() {
        String className = Thread.currentThread().getStackTrace()[4].getClassName();
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException("Calling class name " + Stringer.wrapBracket(className) + " not found", e);
        }
    }

    public static void saveInputStream(InputStream input, URI uri) {
        String message = "Unable to save to resource " + Stringer.wrapBracket(uri.getPath());
        try {
            URI canonicalUri = UriUtil.getCanonical(uri);
            if (input == null || !UriUtil.isFileScheme(canonicalUri))
                throw new IllegalArgumentException();
            File resourceFile = getFile(canonicalUri);
            FileUtils.copyInputStreamToFile(input, resourceFile);
        } catch (Exception e) {
            throw new RuntimeException(message, e);
        }
    }

    public static Object getObjectFromJson(URI jsonResource, Class<?> desiredClass) {
        return getObjectFromJson(jsonResource, null, desiredClass, true);
    }

    public static Object getObjectFromJson(URI jsonResource, Class<?> desiredClass,
            boolean excludeFieldsWithoutExposeAnnotation) {
        return getObjectFromJson(jsonResource, null, desiredClass, excludeFieldsWithoutExposeAnnotation);
    }

    public static Object getObjectFromJson(URI jsonResource, Map<Class<?>, JsonDeserializer<?>> typeDeserializers,
            Class<?> desiredClass) {
        return getObjectFromJson(jsonResource, typeDeserializers, desiredClass, true);
    }

    public static Object getObjectFromJson(URI jsonResource, Map<Class<?>, JsonDeserializer<?>> typeDeserializers,
            Class<?> desiredClass, boolean excludeFieldsWithoutExposeAnnotation) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            if (typeDeserializers != null)
                for (Class<?> typeClass : typeDeserializers.keySet()) {
                    gsonBuilder.registerTypeAdapter(typeClass, typeDeserializers.get(typeClass));
                }
            Gson gson = null;
            if (excludeFieldsWithoutExposeAnnotation)
                gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
            else
                gson = gsonBuilder.create();
            InputStream inputStream = getInputStream(jsonResource);
            inputStream.available();
            Object object = (Object) gson.fromJson(new InputStreamReader(inputStream), desiredClass);
            if (object == null)
                throw new NullPointerException();
            return object;
        } catch (Exception e) {
            throw new RuntimeException("Exception reading JSON file " + Stringer.wrapBracket(jsonResource), e);
        }
    }

    public static String getJsonFromObject(Object object, Map<Class<?>, JsonSerializer<?>> typeSerializers,
            boolean serializeNull) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation().setPrettyPrinting();
        if (serializeNull)
            gsonBuilder.serializeNulls();
        if (typeSerializers != null && !typeSerializers.isEmpty())
            for (Class<?> clazz : typeSerializers.keySet()) {
                gsonBuilder.registerTypeAdapter(clazz, typeSerializers.get(clazz));
            }
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static void makeDirectory(URI uri) {
        makeDirectory(uri, false);
    }

    public static void makeDirectoryClean(URI uri) {
        makeDirectory(uri, true);
    }

    private static void makeDirectory(URI uri, boolean clean) {
        try {
            File directoryFile = new File(uri);
            FileUtils.forceMkdir(directoryFile);
            if (clean)
                FileUtils.cleanDirectory(directoryFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not make directory " + Stringer.wrapBracket(uri) + " with clean "
                    + Stringer.wrapBracket(clean), e);
        }
    }

    public static void touchFile(File file) {
        try {
            FileUtils.touch(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not touch file " + Stringer.wrapBracket(file), e);
        }
    }

    public static void zipFolder(Path sourcePath, Path zipPath) {
        if (sourcePath != null && zipPath != null) {
            try {
                FileOutputStream zipFile = new FileOutputStream(zipPath.toFile());
                ZipOutputStream zipOutput = new ZipOutputStream(zipFile);
                Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String relativeFile = sourcePath.relativize(file).toString();
                        zipOutput.putNextEntry(new ZipEntry(relativeFile));
                        Files.copy(file, zipOutput);
                        zipOutput.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
                zipOutput.close();
                zipFile.close();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create zip file " + Stringer.wrapBracket(zipPath) + " with "
                        + Stringer.wrapBracket(sourcePath) + " content", e);
            }
        }
    }

    public static void addClassPathsToThread(Set<URI> classPaths) {
        /*
         * http://www.invertedsoftware.com/tutorials/Load-your-CLASSPATH-at-runtime.html
         * https://stackoverflow.com/questions/1010919/adding-files-to-java-classpath-at
         * -runtime
         */
        if (classPaths != null) {
            try {
                URL[] urls = new URL[classPaths.size()];
                int i = 0;
                for (URI classPath : classPaths) {
                    classPath = UriUtil.getCanonical(classPath);
                    urls[i] = classPath.toURL();
                    i++;
                }
                URLClassLoader urlClassLoader = new URLClassLoader(urls,
                        Thread.currentThread().getContextClassLoader());
                Thread.currentThread().setContextClassLoader(urlClassLoader);
            } catch (Exception e) {
                throw new RuntimeException("Unable to load class path URIs", e);
            }
        }
    }

}
