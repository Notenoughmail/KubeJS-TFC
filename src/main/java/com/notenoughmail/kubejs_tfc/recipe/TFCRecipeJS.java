package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

public abstract class TFCRecipeJS extends RecipeJS {

    public String result;
    public String ingredient;
    public JsonObject fluidStackIngredient = new JsonObject();

    public JsonObject buildFluidStackIngredient(ListJS listJS) {
        var ingredient = new JsonArray();
        var ingredients = ListJS.orSelf(listJS.get(0));
        for (Object o : ingredients) {
            var fluid = o.toString().replaceAll("\\\"", ""); // Quick and dirty way to make it work
            if (fluid.matches("#.+")) {
                var object = new JsonObject();
                object.addProperty("tag", fluid.replaceFirst("#", ""));
                ingredient.add(object);
            } else {
                ingredient.add(fluid);
            }
        }
        var json = new JsonObject();
        json.add("ingredient", ingredient);
        json.addProperty("amount", ListJS.orSelf(listJS.get(1)).toJson().getAsInt());
        return json;
    }

    public boolean getOptionalBoolMember(JsonObject json, String member, boolean fallback) {
        if (json.get(member).isJsonNull()) {
            return fallback;
        } else {
            return json.get(member).getAsBoolean();
        }
    }

    public int getOptionalIntMember(JsonObject json, String member, int fallback) {
        if (json.get(member).isJsonNull()) {
            return fallback;
        } else {
            return json.get(member).getAsInt();
        }
    }
}