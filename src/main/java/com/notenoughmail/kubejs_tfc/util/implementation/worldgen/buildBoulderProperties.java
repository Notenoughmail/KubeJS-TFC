package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.ArrayList;
import java.util.List;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;

public class buildBoulderProperties {

    private final List<JsonObject> states = new ArrayList<>();

    public buildBoulderProperties state(String rock, String... blocks) {
        var obj = new JsonObject();
        obj.add("rock", blockStateToLenient(rock));
        var array = new JsonArray();
        for (String s : blocks) {
            array.add(blockStateToLenient(s));
        }
        obj.add("blocks", array);
        states.add(obj);
        return this;
    }

    @HideFromJS
    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:boulder");
        var states = new JsonArray();
        for (JsonObject obj : this.states) {
            states.add(obj);
        }
        var config = new JsonObject();
        config.add("states", states);
        json.add("config", config);
        return json;
    }
}
