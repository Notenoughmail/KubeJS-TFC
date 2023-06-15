package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
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
    public ItemStackProviderJS itemProviderResult;

    public JsonArray buildMetals(ListJS listJS) {
        var metals = new JsonArray();
        for (var metal : listJS) {
            var met = ListJS.of(metal);
            if (met == null || met.size() < 3) {
                throw new RecipeExceptionJS("Metal object must contain a metal, a minimum, and a maximum");
            }
            var me = new JsonObject();
            me.addProperty("metal", met.get(0).toString());
            me.addProperty("min", Double.parseDouble(met.get(1).toString()));
            me.addProperty("max", Double.parseDouble(met.get(2).toString()));
            metals.add(me);
        }
        return metals;
    }

    /**
     * This, as the name implies, 'fixes' {@link IngredientJS#toJson()} which is incompatible iff its stack size is 1. <br>
     * See: {@link net.dries007.tfc.common.recipes.ingredients.ItemStackIngredient#fromJson(JsonObject) ItemStackIgnredient#fromjson()}
     * @param ingredientJS The KubeJS ingredient which needs to be parsed by ItemStackIngredient
     * @return A jsonElement which is parse-able by ItemStackIngredient#fromJson()
     */
    public JsonElement fixBrokenKubeIngredientStack(IngredientJS ingredientJS) {
        if (ingredientJS.getCount() == 1) {
            var ingredientStack = new JsonObject();
            ingredientStack.add("ingredient", ingredientJS.toJson());
            return ingredientStack;
        } else {
            return ingredientJS.toJson();
        }
    }

    public boolean hasFluidInput(FluidStackIngredientJS ingredient, boolean exact) {
        for (FluidStackIngredientJS in : inputFluids) {
            if (exact ? in.equals(ingredient) : in.test(ingredient)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBlockInput(BlockIngredientJS ingredient, boolean exact) {
        if (blockIngredient == null) {
            return false;
        }
        return exact ? ingredient.equals(blockIngredient) : ingredient.test(blockIngredient);
    }

    public boolean hasFluidOutput(FluidStackJS output, boolean exact) {
        for (FluidStackJS out : outputFluids) {
            if (exact ? out.strongEquals(output) : out.equals(output)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasItemProviderOutput(ItemStackProviderJS output, boolean exact) {
        if (itemProviderResult == null) {
            return false;
        }
        return exact ? output.equals(itemProviderResult) : output.test(itemProviderResult);
    }
}