package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.HeatingSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.typings.Info;

public class HeatingRecipeJS extends RecipeJS {

    @Info(value = "Sets the recipe's output ItemStackProvider, implicitly has the 'tfc:copy_heat' modifier added")
    public HeatingRecipeJS resultItem(ItemStackProviderJS resultItem) {
        setValue(HeatingSchema.ITEM_RESULT, resultItem);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public HeatingRecipeJS resultFluid(OutputFluid resultFluid) {
        setValue(HeatingSchema.FLUID_RESULT, resultFluid);
        return this;
    }

    @Info(value = "Sets the recipe's output ItemStackProvider and fluid")
    public HeatingRecipeJS results(ItemStackProviderJS resultItem, OutputFluid resultFluid) {
        setValue(HeatingSchema.ITEM_RESULT, resultItem);
        setValue(HeatingSchema.FLUID_RESULT, resultFluid);
        return this;
    }
}
