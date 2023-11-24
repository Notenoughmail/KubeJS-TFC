package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.SimplePotSchema;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

public class SimplePotRecipeJS extends RecipeJS {

    public SimplePotRecipeJS itemOutput(OutputItem[] itemOutput) {
        setValue(SimplePotSchema.ITEM_OUTPUT, itemOutput);
        return this;
    }

    public SimplePotRecipeJS fluidOutput(OutputFluid fluidOutput) {
        setValue(SimplePotSchema.FLUID_OUTPUT, fluidOutput);
        return this;
    }

    public SimplePotRecipeJS outputs(OutputItem[] itemOutput, OutputFluid fluidOutput) {
        setValue(SimplePotSchema.ITEM_OUTPUT, itemOutput);
        setValue(SimplePotSchema.FLUID_OUTPUT, fluidOutput);
        return this;
    }
}
