package com.notenoughmail.kubejs_tfc.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import org.jetbrains.annotations.Nullable;

public class NotRottenIngredientJS implements IngredientJS {

    @Nullable
    private final IngredientJS internalIngredient;

    public NotRottenIngredientJS(@Nullable IngredientJS ingredient) {
        internalIngredient = ingredient;
    }

    @Override
    public boolean test(ItemStackJS itemStackJS) {
        if (internalIngredient != null) {
            return internalIngredient.test(itemStackJS);
        }
        return FoodCapability.get(itemStackJS.getItemStack()) != null;
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:not_rotten");
        if (internalIngredient != null) {
            json.add("ingredient", internalIngredient.toJson());
        }
        return json;
    }

    @Override
    public String toString() {
        return "Ingredient.notRotten(" + (internalIngredient != null ? internalIngredient.toString() : "") + ")";
    }
}