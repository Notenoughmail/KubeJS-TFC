package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import org.jetbrains.annotations.Nullable;

public class BuildFoodItemData {

    private final IngredientJS ingredient;
    @Nullable
    private Integer hunger;
    @Nullable
    private Float saturation;
    @Nullable
    private Float water;
    @Nullable
    private Float decay;
    @Nullable
    private Float grain;
    @Nullable
    private Float fruit;
    @Nullable
    private Float vegetables;
    @Nullable
    private Float protein;
    @Nullable
    private Float dairy;

    public BuildFoodItemData(IngredientJS ingredient) {
        this.ingredient = ingredient;
    }

    public BuildFoodItemData hunger(int hunger) {
        this.hunger = hunger;
        return this;
    }

    public BuildFoodItemData saturation(float saturation) {
        this.saturation = saturation;
        return this;
    }

    public BuildFoodItemData water(float water) {
        this.water = water;
        return this;
    }

    public BuildFoodItemData decayModifier(float modifier) {
        decay = modifier;
        return this;
    }

    public BuildFoodItemData grain(float grain) {
        this.grain = grain;
        return this;
    }

    public BuildFoodItemData fruit(float fruit) {
        this.fruit = fruit;
        return this;
    }

    public BuildFoodItemData vegetables(float vegetables) {
        this.vegetables = vegetables;
        return this;
    }

    public BuildFoodItemData protein(float protein) {
        this.protein = protein;
        return this;
    }

    public BuildFoodItemData dairy(float dairy) {
        this.dairy = dairy;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        if (hunger != null) {
            json.addProperty("hunger", hunger);
        }
        if (saturation != null) {
            json.addProperty("saturation", saturation);
        }
        if (water != null) {
            json.addProperty("water", water);
        }
        if (decay != null) {
            json.addProperty("decay_modifier", decay);
        }
        if (grain != null) {
            json.addProperty("grain", grain);
        }
        if (fruit != null) {
            json.addProperty("fruit", fruit);
        }
        if (vegetables != null) {
            json.addProperty("vegetables", vegetables);
        }
        if (protein != null) {
            json.addProperty("protein", protein);
        }
        if (dairy != null) {
            json.addProperty("dairy", dairy);
        }
        return json;
    }
}
