package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.ChiselSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

// Technically has an ISP output
@SuppressWarnings("unused")
public class ChiselRecipeJS extends TFCProviderRecipeJS {

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

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof ChiselRecipe c) {
            return c.getItemIngredient() == null ? List.of() : List.of(c.getItemIngredient());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a chisel recipe?");
        }
    }
}
