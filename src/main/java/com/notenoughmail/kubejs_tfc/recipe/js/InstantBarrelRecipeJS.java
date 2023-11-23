package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.recipe.schema.BarrelInstantSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class InstantBarrelRecipeJS extends RecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    public InstantBarrelRecipeJS outputItem(ItemStackProviderJS outputItem) {
        setValue(BarrelInstantSchema.OUTPUT_ITEM, outputItem);
        return this;
    }

    public InstantBarrelRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(BarrelInstantSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public InstantBarrelRecipeJS outputs(ItemStackProviderJS outputItem, OutputFluid outputFluid) {
        setValue(BarrelInstantSchema.OUTPUT_ITEM, outputItem);
        setValue(BarrelInstantSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public InstantBarrelRecipeJS inputItem(InputItem inputItem) {
        setValue(BarrelInstantSchema.INPUT_ITEM, inputItem);
        return this;
    }

    public InstantBarrelRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(BarrelInstantSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    public InstantBarrelRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(BarrelInstantSchema.INPUT_ITEM, inputItem);
        setValue(BarrelInstantSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    public InstantBarrelRecipeJS sound(String sound) {
        setValue(BarrelInstantSchema.SOUND, sound);
        return this;
    }
}
