package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import org.jetbrains.annotations.Nullable;

public class BuildEffectData {

    private final String type;
    @Nullable
    private Integer duration;
    @Nullable
    private Integer amplifier;
    @Nullable
    private Float chance;

    public BuildEffectData(String type) {
        this.type = type;
    }

    public BuildEffectData duration(int i) {
        duration = i;
        return this;
    }

    public BuildEffectData amplifier(int i) {
        amplifier = i;
        return this;
    }

    public BuildEffectData chance(float f) {
        chance = f;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", type);
        if (duration != null) {
            json.addProperty("duration", duration);
        }
        if (amplifier != null) {
            json.addProperty("amplifier", amplifier);
        }
        if (chance != null) {
            json.addProperty("chance", chance);
        }
        return json;
    }
}
