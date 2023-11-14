package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;

public class BuildThinSpikeProperties {

    private JsonElement state;
    private int radius;
    private int tries;
    private int minHeight;
    private int maxHeight;

    public BuildThinSpikeProperties state(String block) {
        state = blockStateToLenient(block);
        return this;
    }

    public BuildThinSpikeProperties radius(int i) {
        radius = i;
        return this;
    }

    public BuildThinSpikeProperties tries(int i) {
        tries = i;
        return this;
    }

    public BuildThinSpikeProperties heights(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:thin_spike");
        var config = new JsonObject();
        config.add("state", state);
        config.addProperty("radius", radius);
        config.addProperty("tries", tries);
        config.addProperty("min_height", minHeight);
        config.addProperty("max_height", maxHeight);
        json.add("config", config);
        return json;
    }
}
