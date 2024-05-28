package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.AnvilSchema;
import dev.latvian.mods.kubejs.typings.Info;

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
}
