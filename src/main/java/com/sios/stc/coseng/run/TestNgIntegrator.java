package com.sios.stc.coseng.run;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.integration.Integrator;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.UriUtil;

public final class TestNgIntegrator {

    @Expose
    private String                         name                = null;
    @Expose
    private Set<URI>                       classPaths          = null;
    @Expose
    private String                         integratorClassName = null;
    @Expose
    private TestNgIntegratorGsonSerializer gsonSerializer      = null;
    @Expose
    private URI                            configJson          = null;

    private Class<?> integratorClass = null;
    private Class<?> dataClass       = null;

    public String getName() {
        return this.name;
    }

    public URI getConfig() {
        return this.configJson;
    }

    public Class<?> getIntegratorClass() {
        return integratorClass;
    }

    public Class<?> getDataClass() {
        return dataClass;
    }

    public TestNgIntegratorGsonSerializer getGsonSerializer() {
        return gsonSerializer;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "integratorClass", "dataClass");
    }

    Integrator validateAndPrepare(Test test) {
        /* classPath, gsonSerializer and config may be null */
        try {
            if (name == null || name.isEmpty() || integratorClassName == null || integratorClassName.isEmpty()
                    || configJson == null)
                throw new IllegalArgumentException(
                        "Field name, integratorClassName, dataClassName and configJson must be provided");
            if (classPaths != null) {
                if (classPaths.contains(null))
                    throw new IllegalArgumentException("Field classPaths may not contain null elements");
                if (!classPaths.isEmpty()) {
                    Set<URI> canonicalClassPaths = new HashSet<URI>();
                    for (URI uri : classPaths) {
                        canonicalClassPaths.add(UriUtil.getCanonical(uri));
                    }
                    classPaths = canonicalClassPaths;
                    Resource.addClassPathsToThread(classPaths);
                }
            }
            integratorClass = Class.forName(integratorClassName);
            if (gsonSerializer == null)
                gsonSerializer = new TestNgIntegratorGsonSerializer();
            gsonSerializer.validateAndPrepare();
            configJson = UriUtil.getCanonical(configJson);
            /* Get the concrete class */
            Integrator integrator;
            integrator = (Integrator) integratorClass.newInstance();
            integrator.validateAndPrepare(test, configJson);
            return integrator;
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
