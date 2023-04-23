package com.notenoughmail.kubejs_tfc.util.implementation.data;

public class FoodItemDataWrapper {

    public static FoodItemData of(Object o) {
        return FoodItemData.of(o);
    }

    public static FoodItemData hunger(FoodItemData data, int i) {
        return data.hunger(i);
    }

    public static FoodItemData saturation(FoodItemData data, float f) {
        return data.saturation(f);
    }

    public static FoodItemData water(FoodItemData data, float f) {
        return data.water(f);
    }

    public static FoodItemData decayModifier(FoodItemData data, float f) {
        return data.decayModifier(f);
    }

    public static FoodItemData grain(FoodItemData data, float f) {
        return data.grain(f);
    }

    public static FoodItemData fruit(FoodItemData data, float f) {
        return data.fruit(f);
    }

    public static FoodItemData vegetables(FoodItemData data, float f) {
        return data.vegetables(f);
    }

    public static FoodItemData protein(FoodItemData data, float f) {
        return data.protein(f);
    }

    public static FoodItemData dairy(FoodItemData data, float f) {
        return data.dairy(f);
    }
}
