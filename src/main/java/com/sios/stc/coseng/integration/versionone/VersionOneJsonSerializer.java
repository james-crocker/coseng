package com.sios.stc.coseng.integration.versionone;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sios.stc.coseng.Triggers.Phase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.IIntegratorGsonSerializer;

public final class VersionOneJsonSerializer implements IIntegratorGsonSerializer {

    @Override
    public Map<Class<?>, JsonSerializer<?>> get() {
        return getStatic();
    }

    static Map<Class<?>, JsonSerializer<?>> getStatic() {
        Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<Class<?>, JsonSerializer<?>>();

        JsonSerializer<TriggerOn> triggerOnTypeSerializer = new JsonSerializer<TriggerOn>() {
            @Override
            public JsonElement serialize(TriggerOn arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(TriggerOn.class, triggerOnTypeSerializer);

        JsonSerializer<Phase> phaseTypeSerializer = new JsonSerializer<Phase>() {
            @Override
            public JsonElement serialize(Phase arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(Phase.class, phaseTypeSerializer);

        return serializers;
    }

}
