package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.HeatingSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

@SuppressWarnings("unused")
public class HeatingRecipeJS extends TFCProviderRecipeJS {

    @Info(value = "Sets the recipe's output ItemStackProvider, implicitly has the 'tfc:copy_heat' modifier added")
    public HeatingRecipeJS resultItem(ItemStackProviderJS resultItem) {
        setValue(HeatingSchema.ITEM_RESULT, resultItem);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public HeatingRecipeJS resultFluid(OutputFluid resultFluid) {
        setValue(HeatingSchema.FLUID_RESULT, resultFluid);
        return this;
    }

    @Info(value = "Sets the recipe's output ItemStackProvider and fluid")
    public HeatingRecipeJS results(ItemStackProviderJS resultItem, OutputFluid resultFluid) {
        setValue(HeatingSchema.ITEM_RESULT, resultItem);
        setValue(HeatingSchema.FLUID_RESULT, resultFluid);
        return this;
    }

    @Info(value = "Determines if the recipe will consider the durability of an item when melting into a fluid")
    public HeatingRecipeJS useDurability(boolean useDurability) {
        setValue(HeatingSchema.USE_DURABILITY, useDurability);
        return this;
    }

    @Info(value = "Sets the item output's chance")
    public HeatingRecipeJS chance(float chance) {
        setValue(HeatingSchema.CHANCE, chance);
        return this;
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof HeatingRecipe h) {
            return List.of(h.getIngredient());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a heating recipe?");
        }
    }
}
