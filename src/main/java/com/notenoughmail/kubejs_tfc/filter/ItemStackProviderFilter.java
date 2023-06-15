package com.notenoughmail.kubejs_tfc.filter;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.crafting.AdvancedShapedRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.crafting.AdvancedShapelessRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;

public class ItemStackProviderFilter implements RecipeFilter {

    private final ItemStackProviderJS provider;
    private final boolean exact;

    public ItemStackProviderFilter(ItemStackProviderJS p, boolean b) {
        provider = p;
        exact = b;
    }

    @Override
    public boolean test(RecipeJS recipeJS) {
        if (recipeJS instanceof TFCRecipeJS tfcRecipeJS) {
            return tfcRecipeJS.hasItemProviderOutput(provider, exact);
        } else if (recipeJS instanceof AdvancedShapedRecipeJS advShapedRecipeJS) {
            return advShapedRecipeJS.hasItemProviderOutput(provider, exact);
        } else if (recipeJS instanceof AdvancedShapelessRecipeJS advShapelessRecipeJS) {
            return advShapelessRecipeJS.hasItemProviderOutput(provider, exact);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ItemStackProviderFilter{out=" + provider + ", exact=" + exact + "}";
    }
}