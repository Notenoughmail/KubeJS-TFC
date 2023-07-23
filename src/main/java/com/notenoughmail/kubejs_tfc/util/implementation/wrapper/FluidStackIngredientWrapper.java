package com.notenoughmail.kubejs_tfc.util.implementation.wrapper;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;

public class FluidStackIngredientWrapper {

    public static FluidStackIngredientJS of(Object o) {
        return FluidStackIngredientJS.of(o);
    }

    public static FluidStackIngredientJS of(Object o, int i) {
        return FluidStackIngredientJS.of(o, i);
    }

    public static FluidStackIngredientJS water() {
        return FluidStackIngredientJS.of("minecraft:water");
    }

    public static FluidStackIngredientJS water(int i) {
        return FluidStackIngredientJS.of("minecraft:water", i);
    }

    public static FluidStackIngredientJS forgeWater() {
        return FluidStackIngredientJS.of("#forge:water");
    }

    public static FluidStackIngredientJS forgeWater(int i) {
        return FluidStackIngredientJS.of("#forge:water", i);
    }

    public static FluidStackIngredientJS empty() {
        return FluidStackIngredientJS.EMPTY;
    }
}
