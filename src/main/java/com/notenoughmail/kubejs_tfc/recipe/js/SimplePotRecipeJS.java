package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.SimplePotSchema;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.typings.Info;

public class SimplePotRecipeJS extends TFCRecipeJS {

    @Info(value = "Sets the items that should be left in the pot after the recipe completes, accepts up to 5")
    public SimplePotRecipeJS itemOutput(OutputItem[] itemOutput) {
        setValue(SimplePotSchema.ITEM_OUTPUT, itemOutput);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public SimplePotRecipeJS fluidOutput(OutputFluid fluidOutput) {
        setValue(SimplePotSchema.FLUID_OUTPUT, fluidOutput);
        return this;
    }

    @Info(value = "Sets the recipe's output items and fluid")
    public SimplePotRecipeJS outputs(OutputItem[] itemOutput, OutputFluid fluidOutput) {
        setValue(SimplePotSchema.ITEM_OUTPUT, itemOutput);
        setValue(SimplePotSchema.FLUID_OUTPUT, fluidOutput);
        return this;
    }
}
