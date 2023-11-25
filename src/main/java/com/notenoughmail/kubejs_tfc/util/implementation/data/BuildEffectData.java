package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.typings.Info;
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

    @Info(value = "Sets the number of ticks the effect is applied for, defaults to 20")
    public BuildEffectData duration(int i) {
        duration = i;
        return this;
    }

    @Info(value = "Sets the level of the potion effect, defaults to 0")
    public BuildEffectData amplifier(int i) {
        amplifier = i;
        return this;
    }

    @Info(value = "Sets the chance, per 25mB drank, the effect will be applied, in the range [0, 1]. Defaults to 1")
    public BuildEffectData chance(float f) {
        chance = f;
        return this;
    }

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
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