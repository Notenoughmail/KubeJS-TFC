package com.notenoughmail.kubejs_tfc.util.implementation.data;

@Deprecated
public class DrinkableDataWrapper {

    public static DrinkableData of(Object o) {
        return DrinkableData.of(o);
    }

    public static DrinkableData consumeChance(DrinkableData data, float consumeChance) {
        return data.consumeChance(consumeChance);
    }

    public static DrinkableData thirst(DrinkableData data, int thirst) {
        return data.thirst(thirst);
    }

    public static DrinkableData intoxication(DrinkableData data, int intoxication) {
        return data.intoxication(intoxication);
    }

    public static DrinkableData effect(DrinkableData data, Object... effects) {
        return data.effects(effects);
    }
}
