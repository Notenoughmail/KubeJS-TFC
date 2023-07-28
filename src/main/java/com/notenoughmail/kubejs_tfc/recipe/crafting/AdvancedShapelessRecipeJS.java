package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.IRecipeJSExtension;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapelessRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.function.BiFunction;

public class AdvancedShapelessRecipeJS extends ShapelessRecipeJS implements IRecipeJSExtension {

    public ItemStackProviderJS itemProviderResult;
    public IngredientJS primaryIngredient;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredients");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.addAll(parseIngredientItemList(listJS.get(1)));
        primaryIngredient = inputItems.get(0).unwrapStackIngredient().get(0);
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

    public boolean hasItemProviderOutput(ItemStackProviderJS output, boolean exact) {
        if (itemProviderResult == null) {
            return false;
        }
        return exact ? output.equals(itemProviderResult) : output.test(itemProviderResult);
    }

    @Override
    public boolean tfcReplaceItemProvider(ItemStackProviderJS out, ItemStackProviderJS with, boolean exact, BiFunction<ItemStackProviderJS, ItemStackProviderJS, ItemStackProviderJS> function) {
        boolean changed = false;

        if (exact) {
            if (itemProviderResult.equals(out)) {
                changed = true;
            }
        } else if (itemProviderResult.test(out)) {
            changed = true;
        }

        if (changed) {
            itemProviderResult = function.apply(with.copy(), itemProviderResult);
            serializeOutputs = true;
            save();
        }
        return changed;
    }
}