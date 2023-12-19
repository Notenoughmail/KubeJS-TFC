package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.js;

import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema.MixingBowlSchema;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class MixingBowlRecipeJS extends TFCRecipeJS {

    public MixingBowlRecipeJS outputItem(OutputItem outputItem) {
        setValue(MixingBowlSchema.OUTPUT_ITEM, outputItem);
        return this;
    }

    public MixingBowlRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(MixingBowlSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public MixingBowlRecipeJS outputs(OutputItem outputItem, OutputFluid outputFluid) {
        setValue(MixingBowlSchema.OUTPUT_ITEM, outputItem);
        setValue(MixingBowlSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public MixingBowlRecipeJS itemIngredients(InputItem[] ingredients) {
        setValue(MixingBowlSchema.INGREDIENTS, ingredients);
        return this;
    }

    public MixingBowlRecipeJS fluidIngredient(FluidStackIngredient fluidIngredient) {
        setValue(MixingBowlSchema.FLUID_INGREDIENT, fluidIngredient);
        return this;
    }

    public MixingBowlRecipeJS ingredients(InputItem[] ingredients, FluidStackIngredient fluidIngredient) {
        setValue(MixingBowlSchema.INGREDIENTS, ingredients);
        setValue(MixingBowlSchema.FLUID_INGREDIENT, fluidIngredient);
        return this;
    }
}
