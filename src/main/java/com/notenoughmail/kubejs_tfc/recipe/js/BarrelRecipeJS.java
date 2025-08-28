package com.notenoughmail.kubejs_tfc.recipe.js;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.dries007.tfc.common.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public abstract class BarrelRecipeJS extends TFCProviderRecipeJS {

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof BarrelRecipe b) {
            return b.getInputItem() == ItemStackIngredient.EMPTY ? List.of() : List.of(b.getInputItem().ingredient());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original barrel recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a barrel recipe?");
        }
    }
}
