package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.AnvilSchema;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

// Recipe constructors cannot have two constructors with the same number of arguments and anvil working has 2 optional arguments, thus this
public class AnvilWorkingRecipeJS extends RecipeJS {

    public AnvilWorkingRecipeJS tier(int tier) {
        setValue(AnvilSchema.TIER, tier);
        return this;
    }

    public AnvilWorkingRecipeJS bonus(boolean applyBonus) {
        setValue(AnvilSchema.BONUS, applyBonus);
        return this;
    }
}
