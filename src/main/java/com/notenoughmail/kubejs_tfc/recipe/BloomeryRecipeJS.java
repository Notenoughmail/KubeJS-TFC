package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class BloomeryRecipeJS extends TFCRecipeJS {

    private int duration;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - result, fluid ingredient, catalyst, and duration");
        }

        outputItems.add(parseResultItem(listJS.get(0)));

        inputFluids.add(FluidStackIngredientJS.of(listJS.get(1)));

        inputItems.add(parseIngredientItem(listJS.get(2)).asIngredientStack().ingredient);

        duration = ListJS.orSelf(listJS.get(3)).toJson().getAsInt();
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        inputItems.add(parseIngredientItem(json.get("catalyst")));
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("fluid")));
        duration = json.get("duration").getAsInt();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("catalyst", inputItems.get(0).toJson());
            json.add("fluid", inputFluids.get(0).toJson());
            json.addProperty("duration", duration);
        }
    }

    @Override
    public String toString() {
        return inputItems + " + " + inputFluids + " -> " + outputItems;
    }
}
