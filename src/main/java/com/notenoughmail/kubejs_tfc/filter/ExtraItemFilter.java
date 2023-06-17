package com.notenoughmail.kubejs_tfc.filter;

import com.notenoughmail.kubejs_tfc.recipe.crafting.ExtraProductsShapedJS;
import com.notenoughmail.kubejs_tfc.recipe.crafting.ExtraProductsShapelessJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;

public class ExtraItemFilter implements RecipeFilter {

    private final IngredientJS extra;
    private final boolean exact;

    public ExtraItemFilter(IngredientJS o, boolean b) {
        extra = o;
        exact = b;
    }

    @Override
    public boolean test(RecipeJS recipeJS) {
        if (recipeJS instanceof ExtraProductsShapedJS shaped) {
            return shaped.hasExtraItem(extra, exact);
        } else if (recipeJS instanceof ExtraProductsShapelessJS shapeless) {
            return shapeless.hasExtraItem(extra, exact);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ExtraItemFilter{extraOutput=" + extra + ", exact=" + exact + "}";
    }
}