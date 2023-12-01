package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.ChiselSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.typings.Info;

public class ChiselRecipeJS extends RecipeJS {

    @Info(value = "Specifies the chisel for the recipe, must be tagged 'tfc:chisels'")
    public ChiselRecipeJS itemIngredient(InputItem itemIngredient) {
        setValue(ChiselSchema.ITEM_INGREDIENT, itemIngredient);
        return this;
    }

    @Info(value = "Sets an extra item to be dropped upon chiseling")
    public ChiselRecipeJS extraDrop(ItemStackProviderJS extraDrop) {
        setValue(ChiselSchema.EXTRA_DROP, extraDrop);
        return this;
    }
}
