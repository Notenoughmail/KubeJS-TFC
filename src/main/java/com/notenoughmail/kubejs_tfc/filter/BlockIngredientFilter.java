package com.notenoughmail.kubejs_tfc.filter;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;

public class BlockIngredientFilter implements RecipeFilter {

    private final BlockIngredientJS in;
    private final boolean exact;

    public BlockIngredientFilter(BlockIngredientJS i, boolean b) {
        in = i;
        exact = b;
    }

    @Override
    public boolean test(RecipeJS recipeJS) {
        return recipeJS instanceof TFCRecipeJS tfcRecipeJS && tfcRecipeJS.hasBlockInput(in, exact);
    }

    @Override
    public String toString() {
        return "BlockIngredientFilter{in=" + in + ", exact=" + exact + "}";
    }
}