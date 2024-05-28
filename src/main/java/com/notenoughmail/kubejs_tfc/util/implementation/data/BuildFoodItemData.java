package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BuildFoodItemData {

    @Nullable
    private final Ingredient ingredient;
    @Nullable
    private String type;
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

    public BuildFoodItemData(@Nullable Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Info(value = "Sets the food definition to be a special type, either 'dynamic', or 'dynamic_bowl'. If set, none of the other values should be set")
    public BuildFoodItemData type(String type) {
        this.type = type;
        return this;
    }

    @Info(value = "Sets how much hunger this food restores, defaults to 4. A full hunger bar is equal to 20")
    public BuildFoodItemData hunger(int hunger) {
        this.hunger = hunger;
        return this;
    }

    @Info(value = "Sets how much saturation this food restores, defaults to 0")
    public BuildFoodItemData saturation(float saturation) {
        this.saturation = saturation;
        return this;
    }

    @Info(value = "Sets how much water this food restores, defaults to 0. A full water bar is equal to 100")
    public BuildFoodItemData water(float water) {
        this.water = water;
        return this;
    }

    @Info(value = "Sets how quickly this food decays, defaults to 1. A higher number indicates a faster decay value")
    public BuildFoodItemData decayModifier(float modifier) {
        decay = modifier;
        return this;
    }

    @Info(value = "Sets how much grain nutrient this food restores, defaults to 0")
    public BuildFoodItemData grain(float grain) {
        this.grain = grain;
        return this;
    }

    @Info(value = "Sets how much fruit nutrient this food restores, defaults to 0")
    public BuildFoodItemData fruit(float fruit) {
        this.fruit = fruit;
        return this;
    }

    @Info(value = "Sets how much vegetables nutrient this food restores, defaults to 0")
    public BuildFoodItemData vegetables(float vegetables) {
        this.vegetables = vegetables;
        return this;
    }

    @Info(value = "Sets how much protein nutrient this food restores, defaults to 0")
    public BuildFoodItemData protein(float protein) {
        this.protein = protein;
        return this;
    }

    @Info(value = "Sets how much dairy nutrient this food restores, defaults to 0")
    public BuildFoodItemData dairy(float dairy) {
        this.dairy = dairy;
        return this;
    }

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        if (ingredient != null) {
            json.add("ingredient", ingredient.toJson());

            // Only allowed in food definitions and not ISP modifiers (presumably?)
            if (type != null && !type.isEmpty()) {
                json.addProperty("type", type);
            }
        }
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