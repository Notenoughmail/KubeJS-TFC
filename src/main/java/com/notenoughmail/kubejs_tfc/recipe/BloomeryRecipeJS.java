package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class BloomeryRecipeJS extends TFCRecipeJS {

    public int duration;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - result, fluid ingredient, catalyst, and duration");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputFluids.add(FluidStackIngredientJS.of(listJS.get(1)));

        inputItems.add(parseIngredientItem(listJS.get(2)).asIngredientStack().ingredient);

        // Blame js, toString does not return an int-parsable string here
        duration = (int) Float.parseFloat(listJS.get(3).toString());
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(json.get("catalyst")));
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("fluid")));
        duration = json.get("duration").getAsInt();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("catalyst", inputItems.get(0).toJson());
            json.add("fluid", inputFluids.get(0).toJson());
            json.addProperty("duration", duration);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " + " + inputFluids + " -> " + itemProviderResult;
    }
}