package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantFluidBarrelRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result fluid, primary fluid, and added fluid");
        }

        if (listJS.get(0) instanceof FluidStackJS fluid) {
            outputFluids.add(fluid);
        } else {
            throw new RecipeExceptionJS("First argument must be a fluid");
        }

        inputFluids.add(FluidStackIngredientJS.of(listJS.get(1)));
        inputFluids.add(FluidStackIngredientJS.of(listJS.get(2)));
    }

    @Override
    public void deserialize() {
        outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid").getAsJsonObject()));
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("primary_fluid")));
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("added_fluid")));
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    public InstantFluidBarrelRecipeJS sound(String s) {
        sound = s;
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
            json.add("primary_fluid", inputFluids.get(0).toJson());
            json.add("added_fluid", inputFluids.get(1).toJson());
        }
    }

    @Override
    public String getFromToString() {
        return inputFluids + " -> " + outputFluids;
    }
}
