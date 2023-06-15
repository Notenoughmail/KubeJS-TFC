package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class BlastFurnaceRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result fluid, fluid ingredient, and catalyst");
        }

        if (listJS.get(0) instanceof FluidStackJS fluid) {
            outputFluids.add(fluid);
        } else {
            throw new RecipeExceptionJS("First argument must be a fluid");
        }

        inputFluids.add(FluidStackIngredientJS.of(listJS.get(1)));

        inputItems.add(parseIngredientItem(listJS.get(2)).asIngredientStack().ingredient);
    }

    @Override
    public void deserialize() {
        inputItems.add(parseIngredientItem(json.get("catalyst")));
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("fluid")));
        outputFluids.add(FluidStackJS.fromJson(json.get("result").getAsJsonObject()));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputFluids.get(0).toJson());
        }

        if (serializeInputs) {
            json.add("catalyst", inputItems.get(0).toJson());
            json.add("fluid", inputFluids.get(0).toJson());
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " + " + inputFluids + " -> " + outputFluids;
    }
}