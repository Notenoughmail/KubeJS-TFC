package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapelessRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AdvancedShapelessRecipeJS extends ShapelessRecipeJS {

    private ItemStackProviderJS itemProviderResult;
    private IngredientJS primaryIngredient;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredients");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.addAll(parseIngredientItemList(listJS.get(1)));
        primaryIngredient = inputItems.get(0);
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.addAll(parseIngredientItemList(json.get("ingredients")));
        if (json.has("primary_ingredient")) {
            primaryIngredient = parseIngredientItem(json.get("primary_ingredient")); // This is optional! 🙃
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            var ingredientsJson = new JsonArray();
            for (var in : inputItems) {
                for (var in1 : in.unwrapStackIngredient()) {
                    ingredientsJson.add(in1.toJson());
                }
            }

            if (primaryIngredient != null) {
                json.add("primary_ingredient", primaryIngredient.toJson());
            }
            json.add("ingredients", ingredientsJson);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}
