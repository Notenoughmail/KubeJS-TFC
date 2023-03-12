package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.ArrayList;
import java.util.List;

public abstract class TFCRecipeJS extends RecipeJS {

    public String result;
    public String ingredient;
    public final List<FluidStackJS> outputFluids = new ArrayList<>();
    public final List<JsonObject> inputFluids = new ArrayList<>();
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

    public JsonArray buildMetals(ListJS listJS) {
        var metals = new JsonArray();
        for (var metal : listJS) {
            var met = ListJS.of(metal);
            if (met == null || met.size() < 3) {
                throw new RecipeExceptionJS("Metal object must contain a metal, a minimum, and a maximum");
            }
            var me = new JsonObject();
            me.addProperty("metal", met.get(0).toString());
            me.addProperty("min", ListJS.orSelf(met.get(1)).toJson().getAsFloat());
            me.addProperty("max", ListJS.orSelf(met.get(2)).toJson().getAsFloat());
            metals.add(me);
        }
        return metals;
    }
}