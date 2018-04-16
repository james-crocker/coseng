package com.sios.stc.coseng.run;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;

public final class TestNgIntegratorGsonSerializer {

    @Expose
    private String deserializerClassName = null;
    @Expose
    private String serializerClassName   = null;

    private Class<?> deserializerClass = null;
    private Class<?> serializerClass   = null;

    public TestNgIntegratorGsonSerializer() {
        // do nothing (gson)
    }

    Class<?> getDeserializerClass() {
        return deserializerClass;
    }

    Class<?> getSerializerClass() {
        return serializerClass;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "deserializerClass", "serializerClass");
    }

    void validateAndPrepare() {
        try {
            if (deserializerClassName == null || deserializerClassName.isEmpty() || serializerClassName == null
                    || serializerClassName.isEmpty())
                throw new IllegalArgumentException(
                        "Field deserializerClassName and serializerClassName must be provided");
            deserializerClass = Class.forName(deserializerClassName);
            serializerClass = Class.forName(serializerClassName);
        } catch (ClassNotFoundException e) {
            throw new CosengConfigException(e);
        }
    }

}
