package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

import java.util.ArrayList;
import java.util.List;

public abstract class TFCRecipeJS extends RecipeJS {

    public String result = "minecraft:air";
    public BlockIngredientJS blockIngredient;
    public final List<FluidStackJS> outputFluids = new ArrayList<>();
    public final List<FluidStackIngredientJS> inputFluids = new ArrayList<>();
    public String sound = "minecraft:block.brewing_stand.brew";
    public JsonObject itemStackProvider;

    public JsonArray buildMetals(ListJS listJS) {
        var metals = new JsonArray();
        for (var metal : listJS) {
            var met = ListJS.of(metal);
            if (met == null || met.size() < 3) {
                throw new RecipeExceptionJS("Metal object must contain a metal, a minimum, and a maximum");
            }
            var me = new JsonObject();
            me.addProperty("metal", met.get(0).toString());
            me.addProperty("min", ListJS.orSelf(met.get(1)).toJson().getAsDouble());
            me.addProperty("max", ListJS.orSelf(met.get(2)).toJson().getAsDouble());
            metals.add(me);
        }
        return metals;
    }

    public JsonObject parseItemStackProvider(ListJS listJS) {
        var ISP = new JsonObject();
        var stack = parseResultItem(listJS.get(0)).toResultJson().getAsJsonObject();
        ISP.add("stack", stack);

        var modifiers = ListJS.orSelf(listJS.get(1)).toJson().getAsJsonArray();
        if (!modifiers.isEmpty()) {
            ISP.add("modifiers", modifiers);
        }

        return ISP;
    }

    public JsonObject itemStackToISProvider(JsonObject json) {
        var ISP = new JsonObject();
        ISP.add("stack", json);
        return ISP;
    }
}