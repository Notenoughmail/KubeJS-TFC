package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.ChiselSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

public class ChiselRecipeJS extends RecipeJS {

    public ChiselRecipeJS itemIngredient(InputItem itemIngredient) {
        setValue(ChiselSchema.ITEM_INGREDIENT, itemIngredient);
        return this;
    }

    public ChiselRecipeJS extraDrop(ItemStackProviderJS extraDrop) {
        setValue(ChiselSchema.EXTRA_DROP, extraDrop);
        return this;
    }
}
