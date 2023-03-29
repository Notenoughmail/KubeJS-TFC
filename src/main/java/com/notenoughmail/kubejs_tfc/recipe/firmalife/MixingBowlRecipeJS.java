package com.notenoughmail.kubejs_tfc.recipe.firmalife;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

// Inputs and outputs can be empty
// event.recipes.firmalife.mixing_bowl([], []);
// Is completely valid to FirmaLife!
public class MixingBowlRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - results and ingredients");
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
                inputFluids.add(fluid);
            } else {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }
    }

    @Override
    public void deserialize() {
        if (json.has("ingredients")) {
            for (var ingredient : json.get("ingredients").getAsJsonArray()) {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }
        if (json.has("fluid_ingredient")) {
            inputFluids.add(FluidStackIngredientJS.fromJson(json.get("fluid_ingredient")));
        }
        if (json.has("output_fluid")) {
            outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid")));
        }
        if (json.has("output_item")) {
            outputItems.add(parseResultItem(json.get("output_item")));
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (!outputFluids.isEmpty()) {
                json.add("output_fluid", outputFluids.get(0).toJson());
            }
            if (!outputItems.isEmpty()) {
                json.add("output_item", outputItems.get(0).toResultJson());
            }
        }

        if (serializeInputs) {
            if (!inputFluids.isEmpty()) {
                json.add("fluid_ingredient", inputFluids.get(0).toJson());
            }
            if (!inputItems.isEmpty()) {
                var array = new JsonArray();
                for (var ingredient : inputItems) {
                    array.add(ingredient.toJson().getAsJsonObject());
                }
                json.add("ingredients", array);
            }
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " + " + inputFluids + " -> " + outputItems + " + " + outputFluids;
    }
}
