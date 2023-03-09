package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class BlastFurnaceRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result fluid, fluid ingredient, and catalyst");
        }

        // Dumb way to get IDEA to stop yelling at me about inconvertible types
        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                throw new RecipeExceptionJS("First argument must be a fluid");
            }
        }

        boolean other = true;
        for (var ingredientFluid : ListJS.orSelf(listJS.get(1))) {
            if (ingredientFluid instanceof FluidStackJS fluid) {
                inputFluids.add(fluidStackToFSIngredient(fluid.toJson()));
                other = false;
            }
        }
        if (other) {
            inputFluids.add(parseFluidStackIngredient(ListJS.of(listJS.get(1))));
        }

        inputItems.add(parseIngredientItem(listJS.get(2)).asIngredientStack().ingredient);
    }

    @Override
    public void deserialize() {
        inputItems.add(parseIngredientItem(json.get("catalyst")));
        inputFluids.add(json.get("fluid").getAsJsonObject());
        outputFluids.add(FluidStackJS.fromJson(json.get("result").getAsJsonObject()));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputFluids.get(0).toJson());
        }

        if (serializeInputs) {
            json.add("catalyst", inputItems.get(0).toJson());
            json.add("fluid", inputFluids.get(0));
        }
    }
}
