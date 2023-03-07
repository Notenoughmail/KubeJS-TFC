package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantBarrelRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - results, ingredient item, and ingredient fluid");
        }

        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                outputItems.add(parseResultItem(result));
            }
        }

        inputItems.add(parseIngredientItem(listJS.get(2)));
        fluidStackIngredient = parseFluidStackIngredient(ListJS.of(listJS.get(3)));

        if (listJS.size() > 3) {
            sound = listJS.get(3).toString();
        }
    }

    @Override
    public void deserialize() {
        if (json.has("input_item")) {
            inputItems.add(parseIngredientItem(json.get("input_item")));
        }
        if (json.has("input_fluid")) {
            fluidStackIngredient = json.get("input_fluid").getAsJsonObject();
        }
        if (json.has("output_item")) {
            outputItems.add(parseResultItem(json.get("output_item")));
        }
        if (json.has("output_fluid")) {
            outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid").getAsJsonObject()));
        }
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (!outputItems.isEmpty()) {
                json.add("output_item", outputItems.get(0).toResultJson());
            }
            if (!outputFluids.isEmpty()) {
                json.add("output_fluid", outputFluids.get(0).toJson());
            }
            json.addProperty("sound", sound);
        }

        if (serializeInputs) {
            if (!inputItems.isEmpty()) {
                json.add("input_item", inputItems.get(0).toJson());
            }
            if (fluidStackIngredient != null) {
                json.add("input_fluid", fluidStackIngredient);
            }
        }
    }
}
