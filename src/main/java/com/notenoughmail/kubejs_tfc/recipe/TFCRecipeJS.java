package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.ArrayList;
import java.util.List;

public abstract class TFCRecipeJS extends RecipeJS {

    public String result;
    public String ingredient;
    public List<FluidStackJS> outputFluids = new ArrayList<>();
    public List<JsonObject> inputFluids = new ArrayList<>();
    public String sound = "minecraft:block.brewing_stand.brew";

    public JsonObject parseFluidStackIngredient(ListJS listJS) {
        var ingredient = new JsonArray();
        var ingredients = ListJS.orSelf(listJS.get(0));
        for (Object o : ingredients) {
            var fluid = o.toString().replaceAll("\\\"", ""); // Quick and dirty way to make it work
            if (fluid.matches("#.+")) {
                var object = new JsonObject();
                object.addProperty("tag", fluid.replaceFirst("#", ""));
                var tag = new JsonArray();
                tag.add(object);
                ingredient.add(tag);
            } else {
                var fluidIngredient = new JsonArray();
                fluidIngredient.add(fluid);
                ingredient.add(fluidIngredient);
            }
        }
        var json = new JsonObject();
        json.add("ingredient", ingredient);
        json.addProperty("amount", listJS.size() > 1 ? ListJS.orSelf(listJS.get(1)).toJson().getAsInt() : FluidStack.bucketAmount());
        return json;
    }

    public JsonObject fluidStackToFSIngredient(JsonObject json) {
        var json1 = new JsonObject();
        json1.addProperty("amount", json.get("amount").getAsInt());
        var json2 = new JsonObject();
        if (json.has("fluid")) {
            json2.add("fluid", json.get("fluid"));
        } else if (json.has("tag")) {
            json2.add("tag", json.get("tag"));
        } else if (json.has("fluidTag")) {
            json2.add("tag", json.get("fluidTag")); //Kubejs Additions allows Fluid.of to accept tags
        }
        json1.add("ingredient", json2);
        return json1;
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