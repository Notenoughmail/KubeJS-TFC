package com.notenoughmail.kubejs_tfc.filter;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;

public class FluidStackIngredientFilter implements RecipeFilter {

    private final FluidStackIngredientJS in;
    private final boolean exact;

    public FluidStackIngredientFilter(FluidStackIngredientJS i, boolean b) {
        in = i;
        exact = b;
    }

    @Override
    public boolean test(RecipeJS recipeJS) {
        return recipeJS instanceof TFCRecipeJS tfcRecipeJS && tfcRecipeJS.hasFluidInput(in, exact);
    }

    @Override
    public String toString() {
        return "FluidStackIngredientFilter{in=" + in + ", exact=" + exact + "}";
    }
}