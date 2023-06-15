package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

/**
 * A recipe builder for recipes whose constructors perfectly match {@link net.dries007.tfc.common.recipes.SimpleItemRecipe SimpleItemRecipe}. <br>
 * Originally QuernRecipeJS, but changed for FirmaLife's smoking and drying recipe types.
 */
public class SimpleRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredient");
        }

        inputItems.add(parseIngredientItem(listJS.get(1)));

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(json.get("ingredient")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}