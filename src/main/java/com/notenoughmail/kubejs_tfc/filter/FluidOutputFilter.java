package com.notenoughmail.kubejs_tfc.filter;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;

public class FluidOutputFilter implements RecipeFilter {

    private final FluidStackJS out;
    private final boolean exact;

    public FluidOutputFilter(FluidStackJS o, boolean b) {
        out = o;
        exact = b;
    }

    @Override
    public boolean test(RecipeJS recipeJS) {
        return recipeJS instanceof TFCRecipeJS tfcRecipeJS && tfcRecipeJS.hasFluidOutput(out, exact);
    }

    @Override
    public String toString() {
        return "FluidOutputFilter{out=" + out + ", exact=" + exact + "}";
    }
}