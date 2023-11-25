package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class BuildPortionData {

    @Nullable
    private Ingredient ingredient;
    @Nullable
    private Float nutrientModifier;
    @Nullable
    private Float waterModifier;
    @Nullable
    private Float saturationModifier;

    @Info(value = "Sets the ingredient this portion corresponds to")
    public BuildPortionData ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    @Info(value = "Multiplies the nutrition from the food using this portion, defaults to 1")
    public BuildPortionData nutrientModifier(float mod) {
        nutrientModifier = mod;
        return this;
    }

    @Info(value = "Multiplies the water from the food using this portion, defaults to 1")
    public BuildPortionData waterModifier(float mod) {
        waterModifier = mod;
        return this;
    }

    @Info(value = "Multiplies the saturation from the food using this portion, defaults to 1")
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