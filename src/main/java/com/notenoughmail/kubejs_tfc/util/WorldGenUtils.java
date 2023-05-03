package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class WorldGenUtils {

    public static final String notANumber = "[^0-9.]";

    public static JsonElement blockStateToLenient(String block) {
        if (block.matches(".+\\[.+\\]")) {
            var blockState = block.replace("]", "").split("\\[");
            var states = blockState[1].split(",");

            var json = new JsonObject();
            json.addProperty("Name", blockState[0]);
            var properties = new JsonObject();
            for (String state : states) {
                var value = state.split("=");
                properties.addProperty(value[0], value[1]);
            }
            json.add("Properties", properties);
            return json;
        }

        return new JsonPrimitive(block);
    }
}
