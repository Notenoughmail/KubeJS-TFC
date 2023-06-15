package com.notenoughmail.kubejs_tfc.recipe.rosia;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class ExtrudingAndRollingMachineRecipesJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredient");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.add(parseIngredientItem(listJS.get(1)));
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("output").getAsJsonObject());
        var array = json.get("ingredients").getAsJsonArray();
        inputItems.add(parseIngredientItem(array.get(0)));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("output", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            var array = new JsonArray();
            array.add(inputItems.get(0).withCount(1).toJson());
            json.add("ingredients", array);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}