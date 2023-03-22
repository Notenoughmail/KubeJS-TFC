package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class SimplePotRecipeJS extends TFCRecipeJS {

    private int duration;
    private float temperature;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - results, ingredients, duration, and temperature");
        }

        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                outputItems.add(parseResultItem(result));
            }
        }

        for (var ingredient : ListJS.orSelf(listJS.get(1))) {
            if (ingredient instanceof FluidStackJS fluid) {
                inputFluids.add(FluidStackIngredientJS.of(fluid));
            } else if (ingredient instanceof FluidStackIngredientJS fluid) {
                inputFluids.add(FluidStackIngredientJS.of(fluid));
            } else {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }

        // Blame js, toString does not return an int here
        duration = (int) Float.parseFloat(listJS.get(2).toString());

        temperature = Float.parseFloat(listJS.get(3).toString());
    }

    @Override
    public void deserialize() {
        for (var ingredient : json.get("ingredients").getAsJsonArray()) {
            inputItems.add(parseIngredientItem(ingredient));
        }
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("fluid_ingredient")));
        duration = json.get("duration").getAsInt();
        temperature = json.get("temperature").getAsFloat();
        outputFluids.add(FluidStackJS.fromJson(json.get("fluid_output")));
        if (json.has("item_output")) {
            for (var results : json.get("item_output").getAsJsonArray()) {
                outputItems.add(parseResultItem(results.getAsJsonObject()));
            }
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (!outputFluids.isEmpty()) {
                json.add("fluid_output", outputFluids.get(0).toJson());
            }
            if (!outputItems.isEmpty()) {
                var array = new JsonArray();
                for (var result : outputItems) {
                    array.add(result.toResultJson().getAsJsonObject());
                }
                json.add("item_output", array);
            }
        }

        if (serializeInputs) {
            json.add("fluid_ingredient", inputFluids.get(0).toJson());
            var array = new JsonArray();
            for (var ingredient : inputItems) {
                array.add(ingredient.toJson().getAsJsonObject());
            }
            json.add("ingredients", array);
            json.addProperty("duration", duration);
            json.addProperty("temperature", temperature);
        }
    }

    @Override
    public String toString() {
        return inputItems + " + " + inputFluids + " -> " + outputItems + " + " + outputFluids;
    }
}
