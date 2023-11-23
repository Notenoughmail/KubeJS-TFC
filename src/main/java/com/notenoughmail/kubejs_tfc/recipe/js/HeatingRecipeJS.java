package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.HeatingSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

public class HeatingRecipeJS extends RecipeJS {

    public HeatingRecipeJS resultItem(ItemStackProviderJS resultItem) {
        setValue(HeatingSchema.ITEM_RESULT, resultItem);
        return this;
    }

    public HeatingRecipeJS resultFluid(OutputFluid resultFluid) {
        setValue(HeatingSchema.FLUID_RESULT, resultFluid);
        return this;
    }

    public HeatingRecipeJS results(ItemStackProviderJS resultItem, OutputFluid resultFluid) {
        setValue(HeatingSchema.ITEM_RESULT, resultItem);
        setValue(HeatingSchema.FLUID_RESULT, resultFluid);
        return this;
    }
}
