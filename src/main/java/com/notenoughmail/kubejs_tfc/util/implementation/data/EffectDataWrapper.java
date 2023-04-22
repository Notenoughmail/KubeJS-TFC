package com.notenoughmail.kubejs_tfc.util.implementation.data;

public class EffectDataWrapper {

    public static EffectData type(String o) {
        return EffectData.of(o);
    }

    public static EffectData duration(EffectData data, int duration) {
        return data.duration(duration);
    }

    public static EffectData amplifier(EffectData data, int amplifier) {
        return data.amplifier(amplifier);
    }

    public static EffectData chance(EffectData data, float chance) {
        return data.chance(chance);
    }
}
