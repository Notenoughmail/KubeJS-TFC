package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.Wrapper;

public class EffectData {

    public String type;
    public int duration;
    public int amplifier;
    public float chance;

    public static final EffectData EMPTY = new EffectData("empty");

    public static EffectData of(Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o instanceof EffectData e) {
            return e;
        } else if (o instanceof CharSequence) {
            var s = o.toString();
            return new EffectData(s);
        }

        ConsoleJS.SERVER.error("Something went wrong while parsing an effect's type! Must be a string. For a full list of allowed values run '/kubejs dump_registry minecraft:mob_effect' in-game");
        return EMPTY;
    }

    public EffectData(String type) {
        this.type = type;
        duration = 20;
        amplifier = 0;
        chance = 1f;
    }

    public EffectData duration(int duration) {
        this.duration = duration;
        return this;
    }

    public EffectData amplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    public EffectData chance(float chance) {
        this.chance = chance;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("duration", duration);
        json.addProperty("amplifier", amplifier);
        json.addProperty("chance", chance);
        return json;
    }
}
