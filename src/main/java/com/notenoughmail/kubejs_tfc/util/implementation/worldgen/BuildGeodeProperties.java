package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;
import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.notANumber;

public class BuildGeodeProperties {

    private String outerBlockState;
    private String middleBlockState;
    private final Map<String, Float> innerBlockWeightList = new Object2ObjectOpenHashMap<>();

    public BuildGeodeProperties outer(String s) {
        outerBlockState = s;
        return this;
    }

    public BuildGeodeProperties middle(String s) {
        middleBlockState = s;
        return this;
    }

    public BuildGeodeProperties inner(String... values) {
        for (String s : values) {
            var biValue = s.split(" ");
            if (biValue.length == 2) {
                innerBlockWeightList.put(biValue[1], Float.parseFloat(biValue[0].replaceAll(notANumber, "")));
            } else {
                innerBlockWeightList.put(biValue[0], 1F);
            }
        }
        return this;
    }

    @HideFromJS
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
}
