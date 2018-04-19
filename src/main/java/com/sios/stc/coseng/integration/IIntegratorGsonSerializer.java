package com.sios.stc.coseng.integration;

import java.util.Map;

import com.google.gson.JsonSerializer;

public interface IIntegratorGsonSerializer {

    public Map<Class<?>, JsonSerializer<?>> get();

}
