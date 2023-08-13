package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import org.jetbrains.annotations.Nullable;

public class BuildPortionData {

    @Nullable
    private IngredientJS ingredient;
    @Nullable
    private Float nutrientModifier;
    @Nullable
    private Float waterModifier;
    @Nullable
    private Float saturationModifier;

    public BuildPortionData ingredient(IngredientJS ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public BuildPortionData nutrientModifier(float mod) {
        nutrientModifier = mod;
        return this;
    }

    public BuildPortionData waterModifier(float mod) {
        waterModifier = mod;
        return this;
    }

    public BuildPortionData saturationModifier(float mod) {
        saturationModifier = mod;
        return this;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (ingredient != null) {
            obj.add("ingredient", ingredient.toJson());
        }
        if (nutrientModifier != null) {
            obj.addProperty("nutrient_modifier", nutrientModifier);
        }
        if (waterModifier != null) {
            obj.addProperty("water_modifier", waterModifier);
        }
        if (saturationModifier != null) {
            obj.addProperty("saturation_modifier", saturationModifier);
        }
        return obj;
    }
}