package com.notenoughmail.kubejs_tfc.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import org.jetbrains.annotations.Nullable;

public class TFCNotIngredientJS implements IngredientJS {

    @Nullable
    private final IngredientJS internalIngredient;

    public TFCNotIngredientJS(@Nullable IngredientJS ingredient) {
        internalIngredient = ingredient;
    }

    @Override
    public boolean test(ItemStackJS itemStackJS) {
        if (internalIngredient != null) {
            return !internalIngredient.test(itemStackJS);
        }
        return true; // I think? Maybe?
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:not");
        if (internalIngredient != null) {
            json.add("ingredient", internalIngredient.toJson());
        }
        return json;
    }

    @Override
    public String toString() {
        return "Ingredient.tfcNot(" + (internalIngredient != null ? internalIngredient.toString() : "") + ")";
    }
}
