package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class AddGeodeProperties {

    private static final String notANumber = "[^0-9.]";

    private String outerBlockState;
    private String middleBlockState;
    private final Map<String, Float> innerBlockWeightList = new Object2ObjectOpenHashMap<>();

    public AddGeodeProperties outer(String s) {
        outerBlockState = s;
        return this;
    }

    public AddGeodeProperties middle(String s) {
        middleBlockState = s;
        return this;
    }

    public AddGeodeProperties inner(String... values) {
        for (String s : values) {
            var biValue = s.split(" ");
            innerBlockWeightList.put(biValue[1], Float.parseFloat(biValue[0].replaceAll(notANumber, "")));
        }
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:geode");

        var config = new JsonObject();
        config.add("outer", blockStateToLenient(outerBlockState));
        config.add("middle", blockStateToLenient(middleBlockState));

        var array = new JsonArray();
        for (Map.Entry<String, Float> entry : innerBlockWeightList.entrySet()) {
            var obj = new JsonObject();
            obj.add("data", blockStateToLenient(entry.getKey()));
            obj.addProperty("weight", entry.getValue());
            array.add(obj);
        }
        config.add("inner", array);

        json.add("config", config);
        return json;
    }

    private JsonElement blockStateToLenient(String block) {
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
