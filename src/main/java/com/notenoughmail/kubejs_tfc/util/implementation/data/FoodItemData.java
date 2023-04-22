package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.Wrapper;

public class FoodItemData {

    public final IngredientJS ingredient;
    private int hunger;
    private float saturation;
    private float water;
    private float decay;
    private float grain;
    private float fruit;
    private float vegetables;
    private float protein;
    private float dairy;

    public static FoodItemData of(Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o instanceof FoodItemData f) {
            return f;
        }

        return new FoodItemData(o);
    }

    public FoodItemData(Object ingredient) {
        this.ingredient = IngredientJS.of(ingredient).unwrapStackIngredient().get(0);
        hunger = 4;
        saturation = 0f;
        water = 0f;
        decay = 1f;
        grain = 0f;
        fruit = 0f;
        vegetables = 0f;
        protein = 0f;
        dairy = 0f;
    }

    public FoodItemData hunger(int hunger) {
        this.hunger = hunger;
        return this;
    }

    public FoodItemData saturation(float saturation) {
        this.saturation = saturation;
        return this;
    }

    public FoodItemData water(float water) {
        this.water = water;
        return this;
    }

    public FoodItemData decayModifier(float modifier) {
        decay = modifier;
        return this;
    }

    public FoodItemData grain(float grain) {
        this.grain = grain;
        return this;
    }

    public FoodItemData fruit(float fruit) {
        this.fruit = fruit;
        return this;
    }

    public FoodItemData vegetables(float vegetables) {
        this.vegetables = vegetables;
        return this;
    }

    public FoodItemData protein(float protein) {
        this.protein = protein;
        return this;
    }

    public FoodItemData dairy(float dairy) {
        this.dairy = dairy;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("hunger", hunger);
        json.addProperty("saturation", saturation);
        json.addProperty("water", water);
        json.addProperty("decay_modifier", decay);
        json.addProperty("grain", grain);
        json.addProperty("fruit", fruit);
        json.addProperty("vegetables", vegetables);
        json.addProperty("protein", protein);
        json.addProperty("dairy", dairy);
        return json;
    }
}
