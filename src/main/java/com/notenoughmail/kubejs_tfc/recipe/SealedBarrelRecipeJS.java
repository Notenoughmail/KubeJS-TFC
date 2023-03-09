package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class SealedBarrelRecipeJS extends TFCRecipeJS {

    private int duration;
    // private JsonObject onSealISP = new JsonObject();
    // private JsonObject onUnsealISP = new JsonObject();

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result(s), ingredient(s), and duration");
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
                inputFluids.add(fluidStackToFSIngredient(fluid.toJson()));
            } else if (ingredient.toString().matches("\\[\\[.+\\]")) {
                inputFluids.add(parseFluidStackIngredient(ListJS.of(ingredient)));
            } else {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }

        duration = ListJS.orSelf(listJS.get(2)).toJson().getAsInt();

        if (listJS.size() > 3) {
            sound = listJS.get(3).toString();
        }

        // if (duration < 1) {
        //     // TODO: ISPs - separate methods if I can get that to work
        // }
    }

    @Override
    public void deserialize() {
        if (json.has("input_item")) {
            inputItems.add(parseIngredientItem(json.get("input_item")));
        }
        if (json.has("input_fluid")) {
            inputFluids.add(json.get("input_fluid").getAsJsonObject());
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
        duration = json.get("duration").getAsInt();
        // if (json.has("on_seal")) {
        //     onSealISP = json.get("on_seal").getAsJsonObject();
        // }
        // if (json.has("on_unseal")) {
        //     onUnsealISP = json.get("on_unseal").getAsJsonObject();
        // }
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
            if (!inputFluids.isEmpty()) {
                json.add("input_fluid", inputFluids.get(0));
            }
            json.addProperty("duration", duration);
        }
    }
}
