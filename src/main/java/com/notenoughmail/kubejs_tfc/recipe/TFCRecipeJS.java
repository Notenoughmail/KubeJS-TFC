package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.hell.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.ArrayList;
import java.util.List;

public abstract class TFCRecipeJS extends RecipeJS {

    public String result;
    public String ingredient;
    public JsonObject fluidResult;
    public List<FluidStackJS> outputFluids = new ArrayList<>();
    public JsonObject fluidStackIngredient = new JsonObject();
    public List<FluidStackIngredientJS> inputFluids = new ArrayList<>();
    public String sound = "minecraft:block.brewing_stand.brew";

    public JsonObject parseFluidStackIngredient(ListJS listJS) {
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

    public JsonObject parseFluidStack(ListJS listJS) {
        var json = new JsonObject();
        json.addProperty("fluid", listJS.get(0).toString());
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