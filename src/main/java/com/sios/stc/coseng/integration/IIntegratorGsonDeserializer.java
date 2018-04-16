package com.sios.stc.coseng.integration;

import java.util.Map;

import com.google.gson.JsonDeserializer;

public interface IIntegratorGsonDeserializer {

    abstract public Map<Class<?>, JsonDeserializer<?>> get();

}
