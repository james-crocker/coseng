package com.sios.stc.coseng.integration;

import java.util.Map;

import com.google.gson.JsonDeserializer;

public interface IIntegratorGsonDeserializer {

    public Map<Class<?>, JsonDeserializer<?>> get();

}
