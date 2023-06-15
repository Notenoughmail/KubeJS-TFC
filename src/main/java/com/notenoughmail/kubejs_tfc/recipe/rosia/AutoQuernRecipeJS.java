package com.notenoughmail.kubejs_tfc.recipe.rosia;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AutoQuernRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredient");
        }

        outputItems.add(parseResultItem(listJS.get(0)));

        inputItems.add(parseIngredientItem(listJS.get(1)));
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("output")));
        var array = json.get("ingredients").getAsJsonArray();
        inputItems.add(parseIngredientItem(array.get(0)));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("output", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            var array = new JsonArray();
            array.add(inputItems.get(0).withCount(1).toJson());
            json.add("ingredients", array);
        }
    }
}