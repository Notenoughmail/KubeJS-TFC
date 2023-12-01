package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BuildDrinkableData {

    private final FluidIngredient fluidIngredient;
    @Nullable
    private Float consumeChance;
    @Nullable
    private Integer thirst;
    @Nullable
    private Integer intoxication;
    private final JsonArray effects = new JsonArray();
    @Nullable
    private JsonObject food;


    public BuildDrinkableData(FluidIngredient fluidIngredient) {
        this.fluidIngredient = fluidIngredient;
    }

    @Info(value = "Sets the chance to consume the source block when drinking from a source block, in the range [0, 1]. Defaults to 0")
    public BuildDrinkableData consumeChance(float f) {
        consumeChance = f;
        return this;
    }

    @Info(value = "Sets the thirst the drinkable consumes, per 25mB drank, in the range [0, 100]. Defaults to 0")
    public BuildDrinkableData thirst(int i) {
        thirst = i;
        return this;
    }

    @Info(value = "The number of ticks the player will be intoxicated from, per 25mB drank. Defaults to 0")
    public BuildDrinkableData intoxication(int i) {
        intoxication = i;
        return this;
    }

    @Info(value = "Adds an effect to the drinkable", params = {
            @Param(name = "effect", value = "The name of the effect"),
            @Param(name = "effectData", value = "The effect properties that are applied to the effect")
    })
    @Generics(value = BuildEffectData.class)
    public BuildDrinkableData effect(String effect, Consumer<BuildEffectData> effectData) {
        var data = new BuildEffectData(effect);
        effectData.accept(data);
        effects.add(data.toJson());
        return this;
    }

    @Info(value = "Adds the specified effect to the drinkable with default duration, amplifier, and chance")
    public BuildDrinkableData effect(String effect) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", effect);
        effects.add(json);
        return this;
    }

    @Info(value = "Adds food data to the drinkable")
    @Generics(value = BuildFoodItemData.class)
    public BuildDrinkableData food(Consumer<BuildFoodItemData> foodData) {
        final BuildFoodItemData data = new BuildFoodItemData(null);
        foodData.accept(data);
        food = data.toJson();
        return this;
    }

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        json.add("ingredient", fluidIngredient.toJson());
        if (consumeChance != null) {
            json.addProperty("consume_chance", consumeChance);
        }
        if (thirst != null) {
            json.addProperty("thirst", thirst);
        }
        if (intoxication != null) {
            json.addProperty("intoxication", intoxication);
        }
        if (!effects.isEmpty()) {
            json.add("effects", effects);
        }
        if (food != null) {
            json.add("food", food);
        }
        return json;
    }
}