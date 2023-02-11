package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class WeldingRecipeJS extends TFCRecipeJS {

    private int weldingTier = -1; // TFC's default

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2 ) {
            throw new RecipeExceptionJS("Requires at least two arguments - result and inputs");
        }
        if (listJS.size() > 2) {
            weldingTier = ListJS.orSelf(listJS.get(2)).toJson().getAsInt();
        }

        inputItems.addAll(parseIngredientItemList(listJS.get(1)));
        json.add("first_input", inputItems.get(0).toJson());
        json.add("second_input", inputItems.get(1).toJson());

        json.addProperty("tier", weldingTier);

        outputItems.add(parseResultItem(listJS.get(0)));
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        inputItems.add(parseIngredientItem(json.get("first_input")));
        inputItems.add(parseIngredientItem(json.get("second_input")));
        weldingTier = json.get("tier").getAsInt();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("first_input", inputItems.get(0).toJson());
            json.add("second_input", inputItems.get(1).toJson());
            json.addProperty("tier", weldingTier);
        }
    }
}
