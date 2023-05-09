package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.rhino.Wrapper;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DrinkableData {

    public final FluidStackIngredientJS fluidIngredient;
    private float consumeChance;
    private int thirst;
    private int intoxication;
    private final List<JsonObject> effects;

    public static DrinkableData of(Object o) {
        if (o instanceof Wrapper w) {
            o =w.unwrap();
        }

        if (o instanceof DrinkableData d) {
            return d;
        }

        return new DrinkableData(o);
    }

    public DrinkableData(Object ingredient) {
        fluidIngredient = FluidStackIngredientJS.of(ingredient);
        consumeChance = 0f;
        thirst = 0;
        intoxication = 0;
        effects = new ArrayList<>();
    }

    public DrinkableData consumeChance(float consumeChance) {
        this.consumeChance = consumeChance;
        return this;
    }

    public DrinkableData thirst(int thirst) {
        this.thirst = thirst;
        return this;
    }

    public DrinkableData intoxication(int intoxication) {
        this.intoxication = intoxication;
        return this;
    }

    public DrinkableData effects(Object... effects) {
        for (var effect : effects) {
            this.effects.add(EffectData.of(effect).toJson());
        }
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.add("ingredient", fluidIngredient.toJsonNoAmount());
        json.addProperty("consume_chance", consumeChance);
        json.addProperty("thirst", thirst);
        json.addProperty("intoxication", intoxication);
        if (!effects.isEmpty()) {
            var array = new JsonArray();
            for (var obj : effects) {
                array.add(obj);
            }
            json.add("effects", array);
        }
        return json;
    }
}
