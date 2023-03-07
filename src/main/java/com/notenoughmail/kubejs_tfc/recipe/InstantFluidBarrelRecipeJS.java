package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantFluidBarrelRecipeJS extends TFCRecipeJS {

    private JsonObject addedFluidStackIngredient = new JsonObject();

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires three arguments - result fluid, and ingredient fluids");
        }

        // Dumb way to get IDEA to stop yelling at me about inconvertible types
        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                throw new RecipeExceptionJS("First argument must be a fluid");
            }
        }

        fluidStackIngredient = parseFluidStackIngredient(ListJS.of(listJS.get(1)));
        addedFluidStackIngredient = parseFluidStackIngredient(ListJS.of(listJS.get(2)));

        if (listJS.size() > 3) {
            sound = listJS.get(3).toString();
        }
    }

    @Override
    public void deserialize() {
        outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid").getAsJsonObject()));
        fluidStackIngredient = json.get("primary_fluid").getAsJsonObject();
        addedFluidStackIngredient = json.get("added_fluid").getAsJsonObject();
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("output_fluid", outputFluids.get(0).toJson());
            json.addProperty("sound", sound);
        }

        if (serializeInputs) {
            json.add("primary_fluid", fluidStackIngredient);
            json.add("added_fluid", addedFluidStackIngredient);
        }
    }
}
