package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantFluidBarrelRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result fluid, primary fluid, and added fluid");
        }

        // Dumb way to get IDEA to stop yelling at me about inconvertible types
        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                throw new RecipeExceptionJS("First argument must be a fluid");
            }
        }

        // Fragile and dumb, but I have a choice between a proper implementation of a FluidStackIngredientJS and this
        boolean primaryBool = true;
        for (var primary : ListJS.orSelf(listJS.get(1))) {
            if (primary instanceof FluidStackJS fluid) {
                inputFluids.add(fluidStackToFSIngredient(fluid.toJson()));
                primaryBool = false;
            }
        }
        if (primaryBool) {
            inputFluids.add(parseFluidStackIngredient(ListJS.of(listJS.get(1))));
        }

        boolean addedBool = true;
        for (var added : ListJS.orSelf(listJS.get(2))) {
            if (added instanceof FluidStackJS fluid) {
                inputFluids.add(fluidStackToFSIngredient(fluid.toJson()));
                addedBool = false;
            }
        }
        if (addedBool) {
            inputFluids.add(parseFluidStackIngredient(ListJS.of(listJS.get(2))));
        }
    }

    @Override
    public void deserialize() {
        outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid").getAsJsonObject()));
        inputFluids.add(json.get("primary_fluid").getAsJsonObject());
        inputFluids.add(json.get("added_fluid").getAsJsonObject());
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    public InstantFluidBarrelRecipeJS sound(Object o) {
        var sound = ListJS.orSelf(o);
        this.sound = sound.get(0).toString();
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("output_fluid", outputFluids.get(0).toJson());
            json.addProperty("sound", sound);
        }

        if (serializeInputs) {
            json.add("primary_fluid", inputFluids.get(0));
            json.add("added_fluid", inputFluids.get(1));
        }
    }
}
