package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.ISupportProviderOutput;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.typings.Info;

import static com.notenoughmail.kubejs_tfc.recipe.schema.SimplePotSchema.FLUID_OUTPUT;
import static com.notenoughmail.kubejs_tfc.recipe.schema.SimplePotSchema.ITEM_OUTPUT;

@SuppressWarnings("unused")
public class SimplePotRecipeJS extends PotRecipeJS implements ISupportProviderOutput {

    @Info(value = "Sets the items that should be left in the pot after the recipe completes, accepts up to 5")
    public SimplePotRecipeJS itemOutput(ItemStackProviderJS[] itemOutput) {
        setValue(ITEM_OUTPUT, itemOutput);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public SimplePotRecipeJS fluidOutput(OutputFluid fluidOutput) {
        setValue(FLUID_OUTPUT, fluidOutput);
        return this;
    }

    @Info(value = "Sets the recipe's output items and fluid")
    public SimplePotRecipeJS outputs(ItemStackProviderJS[] itemOutput, OutputFluid fluidOutput) {
        setValue(ITEM_OUTPUT, itemOutput);
        setValue(FLUID_OUTPUT, fluidOutput);
        return this;
    }
}
