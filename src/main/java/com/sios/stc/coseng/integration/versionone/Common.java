package com.sios.stc.coseng.integration.versionone;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;

public final class Common {

    enum V1Asset {
        TEST("Test"), BACKLOG("Story");

        private String asset;

        private V1Asset(String asset) {
            this.asset = asset;
        }

        public String get() {
            return asset;
        }
    }

    enum V1Attr {
        NAME("Name"), STATUS("Status"), TIMEBOX("Timebox"), DESCRIPTION("Description"), SETUP("Setup"), INPUTS(
                "Inputs"), STEPS("Steps"), EXPECTED_RESULTS("ExpectedResults"), ACTUAL_RESULTS("ActualResults");

        private final String attribute;

        private V1Attr(String attribute) {
            this.attribute = attribute;
        }

        public String get() {
            return attribute;
        }
    }

    enum Separator {
        ATTR(":"), NAME(" - "), ASSERT_RESULT(": "), PARAM_VALUE(" = ");

        private String separator;

        private Separator(String separator) {
            this.separator = separator;
        }

        public String get() {
            return separator;
        }
    }

    static void setOrAddField(boolean isSet, List<Field> fields, String attribute, String value, TriggerOn trigger,
            TestPhase phase) {
        if (value != null && !value.isEmpty() && attribute != null && !attribute.isEmpty()) {
            if (fields.isEmpty()) {
                fields.add(new Field(attribute, StringUtils.EMPTY, value, trigger, phase));
            } else if (isSet) {
                Field field = getField(fields, attribute, trigger, phase);
                if (field != null) {
                    field.setValue(value);
                } else {
                    fields.add(new Field(attribute, StringUtils.EMPTY, value, trigger, phase));
                }
            } else {
                Field field = getField(fields, attribute, trigger, phase);
                if (field != null) {
                    field.setValue(field.getValue() + value);
                } else {
                    fields.add(new Field(attribute, StringUtils.EMPTY, value, trigger, phase));
                }
            }
        }
    }

    static List<Field> getFields(List<Field> fields, TriggerOn trigger, TestPhase phase) {
        List<Field> matchedFields = new ArrayList<Field>();
        for (Field field : fields) {
            if (trigger.equals(field.getTriggerOn()) && phase.equals(field.getTestPhase()))
                matchedFields.add(field);
        }
        return matchedFields;
    }

    static Field getField(List<Field> fields, String attribute, TriggerOn trigger, TestPhase phase) {
        for (Field field : getFields(fields, trigger, phase)) {
            if (attribute.equals(field.getAttribute()))
                return field;
        }
        return null;
    }

    static String getValue(List<Field> fields, String attribute, TriggerOn trigger, TestPhase phase) {
        Field field = getField(fields, attribute, trigger, phase);
        if (field != null)
            return field.getValue();
        return StringUtils.EMPTY;
    }

}
