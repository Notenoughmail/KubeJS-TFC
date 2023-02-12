package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

public abstract class TFCRecipeJS extends RecipeJS {

    public JsonObject fluidIngredient = new JsonObject();

    public JsonObject buildFluidIngredient(String fluid, int amount) {
        var json = new JsonObject();
        json.addProperty("ingredient", fluid);
        json.addProperty("amount", amount);
        return json;
    }
}