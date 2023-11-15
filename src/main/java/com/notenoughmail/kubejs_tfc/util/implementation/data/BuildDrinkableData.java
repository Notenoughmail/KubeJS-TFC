package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BuildDrinkableData {

    private final FluidIngredient fluidIngredient;
    @Nullable
    private Float consumeChance;
    @Nullable
    private Integer thirst;
    @Nullable
    private Integer intoxication;
    private final List<JsonObject> effects = new ArrayList<>();


    public BuildDrinkableData(FluidIngredient fluidIngredient) {
        this.fluidIngredient = fluidIngredient;
    }

    public BuildDrinkableData consumeChance(float f) {
        consumeChance = f;
        return this;
    }

    public BuildDrinkableData thirst(int i) {
        thirst = i;
        return this;
    }

    public BuildDrinkableData intoxication(int i) {
        intoxication = i;
        return this;
    }

    public BuildDrinkableData effect(String effect, Consumer<BuildEffectData> effectData) {
        var data = new BuildEffectData(effect);
        effectData.accept(data);
        effects.add(data.toJson());
        return this;
    }

    public BuildDrinkableData effect(String effect) {
        var json = new JsonObject();
        json.addProperty("type", effect);
        effects.add(json);
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
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
            var array = new JsonArray();
            for (JsonObject obj : effects) {
                array.add(obj);
            }
            json.add("effects", array);
        }
        return json;
    }
}