package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapedRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.ArrayList;
import java.util.List;

public class ExtraProductsShapedJS extends ShapedRecipeJS {

    private final List<ItemStackJS> extraProducts = new ArrayList<>();
    private JsonObject recipeJson = new JsonObject();
    private ShapedRecipeJS recipeJS;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("requires two arguments - result items and recipe");
        }

        extraProducts.addAll(parseResultItemList(listJS.get(0)));

        if (listJS.get(1) instanceof ShapedRecipeJS recipe) {
            recipe.serializeJson();
            recipeJS = recipe;
            recipeJson = recipe.json;
            recipe.dontAdd();
        } else {
            throw new RecipeExceptionJS("Second argument must be a shaped crafting recipe!");
        }
    }

    @Override
    public void deserialize() {
        recipeJson = json.get("recipe").getAsJsonObject();
        extraProducts.addAll(parseResultItemList(json.get("extra_products")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            var extras = new JsonArray();
            for (var result : extraProducts) {
                extras.add(result.toResultJson().getAsJsonObject());
            }
            json.add("extra_products", extras);
        }

        if (serializeInputs) {
            json.add("recipe", recipeJson);
        }
    }

    @Override
    public String getFromToString() {
        return recipeJS.getFromToString() + " + " + extraProducts;
    }
}
