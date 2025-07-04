package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.AnvilSchema;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

@SuppressWarnings("unused")
public class AnvilWorkingRecipeJS extends TFCProviderRecipeJS {

    @Info(value = "Sets the minimum tier of anvil the recipe requires to perform")
    public AnvilWorkingRecipeJS tier(int tier) {
        setValue(AnvilSchema.TIER, tier);
        return this;
    }

    @Info(value = "Determines if the recipe should grant a forging bonus or not")
    public AnvilWorkingRecipeJS bonus(boolean applyBonus) {
        setValue(AnvilSchema.BONUS, applyBonus);
        return this;
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof AnvilRecipe a) {
            return List.of(a.getInput());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not an anvil recipe?");
        }
    }
}
