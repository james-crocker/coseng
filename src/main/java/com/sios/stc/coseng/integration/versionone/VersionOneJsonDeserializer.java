package com.sios.stc.coseng.integration.versionone;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sios.stc.coseng.Triggers.Phase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.IIntegratorGsonDeserializer;

public final class VersionOneJsonDeserializer implements IIntegratorGsonDeserializer {

    @Override
    public Map<Class<?>, JsonDeserializer<?>> get() {
        return getStatic();
    }

    static Map<Class<?>, JsonDeserializer<?>> getStatic() {
        Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<Class<?>, JsonDeserializer<?>>();

        JsonDeserializer<TriggerOn> triggerOnTypeDeserializer = new JsonDeserializer<TriggerOn>() {
            public TriggerOn deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return TriggerOn.valueOf(json.getAsString().toUpperCase());
            }
        };
        deserializers.put(TriggerOn.class, triggerOnTypeDeserializer);

        JsonDeserializer<Phase> phaseTypeDeserializer = new JsonDeserializer<Phase>() {
            public Phase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return Phase.valueOf(json.getAsString().toUpperCase());
            }
        };
        deserializers.put(Phase.class, phaseTypeDeserializer);

        return deserializers;
    }

}
