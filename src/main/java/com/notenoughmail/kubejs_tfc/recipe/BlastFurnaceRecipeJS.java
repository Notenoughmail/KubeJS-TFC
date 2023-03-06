package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class BlastFurnaceRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result, fluid ingredient, and catalyst");
        }

        fluidResult = buildFluidStack(ListJS.of(listJS.get(0)));

        fluidStackIngredient = buildFluidStackIngredient(ListJS.of(listJS.get(1)));

        inputItems.add(parseIngredientItem(listJS.get(2)).asIngredientStack().ingredient);
    }

    @Override
    public void deserialize() {
        inputItems.add(parseIngredientItem(json.get("catalyst")));
        fluidStackIngredient = json.get("fluid").getAsJsonObject();
        fluidResult = json.get("result").getAsJsonObject();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", fluidResult);
        }

        if (serializeInputs) {
            json.add("catalyst", inputItems.get(0).toJson());
            json.add("fluid", fluidStackIngredient);
        }

        System.out.println(json);
    }
}
