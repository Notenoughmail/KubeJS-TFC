package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.IRecipeJSExtension;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapelessRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ExtraProductsShapelessJS extends ShapelessRecipeJS implements IRecipeJSExtension {

    private final List<ItemStackJS> extraProducts = new ArrayList<>();
    private JsonObject recipeJson;
    private ShapelessRecipeJS recipeJS;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - extra result items and recipe");
        }

        extraProducts.addAll(parseResultItemList(listJS.get(0)));

        if (listJS.get(1) instanceof ShapelessRecipeJS recipe) {
            recipe.serializeJson();
            recipeJS = recipe;
            recipeJson = recipe.json;
            recipe.dontAdd();
        } else {
            throw new RecipeExceptionJS("Second argument must be a shapeless crafting recipe!");
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

    public boolean hasExtraItem(IngredientJS i, boolean exact) {
        for (ItemStackJS out : extraProducts) {
            if (exact ? i.equals(out) : i.test(out)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tfcReplaceExtraItem(IngredientJS i, ItemStackJS with, boolean exact, BiFunction<ItemStackJS, ItemStackJS, ItemStackJS> function) {
        boolean changed = false;

        for (int j =0 ; j < extraProducts.size() ; j++) {
            if (exact) {
                if (!i.equals(extraProducts.get(j))) {
                    continue;
                }
            } else if (!i.test(extraProducts.get(j))) {
                continue;
            }

            extraProducts.set(j, convertReplacedOutput(j, extraProducts.get(j), function.apply(with.copy(), extraProducts.get(j))));
            changed = true;
            serializeOutputs = true;
            save();
        }
        return changed;
    }
}