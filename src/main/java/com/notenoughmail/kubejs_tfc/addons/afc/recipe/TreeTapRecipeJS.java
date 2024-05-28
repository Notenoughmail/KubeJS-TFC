package com.notenoughmail.kubejs_tfc.addons.afc.recipe;

import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

@SuppressWarnings("unused")
public class TreeTapRecipeJS extends RecipeJS {

    public TreeTapRecipeJS resultFluid(OutputFluid fluid) {
        setValue(TreeTapSchema.RESULT_FLUID, fluid);
        return this;
    }

    public TreeTapRecipeJS minTemp(float temp) {
        setValue(TreeTapSchema.MIN_TEMP, temp);
        return this;
    }

    public TreeTapRecipeJS maxTemp(float temp) {
        setValue(TreeTapSchema.MAX_TEMP, temp);
        return this;
    }

    public TreeTapRecipeJS temps(float min, float max) {
        setValue(TreeTapSchema.MIN_TEMP, min);
        setValue(TreeTapSchema.MAX_TEMP, max);
        return this;
    }

    public TreeTapRecipeJS requiresNaturalLog(boolean required) {
        setValue(TreeTapSchema.REQUIRES_NATURAL_LOG, required);
        return this;
    }

    public TreeTapRecipeJS springOnly(boolean springOnly) {
        setValue(TreeTapSchema.SPRING_ONLY, springOnly);
        return this;
    }
}
