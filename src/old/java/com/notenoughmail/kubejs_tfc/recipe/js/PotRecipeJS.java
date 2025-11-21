package com.notenoughmail.kubejs_tfc.recipe.js;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.PotRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class PotRecipeJS extends TFCRecipeJS {

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof PotRecipe p) {
            return List.copyOf(p.getItemIngredients());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original pot recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a pot recipe?");
        }
    }
}
