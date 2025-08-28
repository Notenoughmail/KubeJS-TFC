package com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCProviderRecipeJS;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import net.mrhitech.artisanal.common.recipes.DistilleryRecipe;

import java.util.List;

import static com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.schema.DistillerySchema.*;

public class DistilleryRecipeJS extends TFCProviderRecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    public DistilleryRecipeJS inputItem(InputItem ingredient) {
        setValue(INPUT_ITEM, ingredient);
        return this;
    }

    public DistilleryRecipeJS inputFluid(FluidStackIngredient fluidIngredient) {
        setValue(INPUT_FLUID, fluidIngredient);
        return this;
    }

    public DistilleryRecipeJS inputs(InputItem ingredient, FluidStackIngredient fluidIngredient) {
        setValue(INPUT_ITEM, ingredient);
        setValue(INPUT_FLUID, fluidIngredient);
        return this;
    }

    public DistilleryRecipeJS resultItem(ItemStackProviderJS resultItem) {
        setValue(RESULT_ITEM, resultItem);
        return this;
    }

    public DistilleryRecipeJS resultFluid(OutputFluid resultFluid) {
        setValue(RESULT_FLUID, resultFluid);
        return this;
    }

    public DistilleryRecipeJS results(ItemStackProviderJS resultItem, OutputFluid resultFluid) {
        setValue(RESULT_ITEM, resultItem);
        setValue(RESULT_FLUID, resultFluid);
        return this;
    }

    public DistilleryRecipeJS leftoverItem(ItemStackProviderJS leftoverItem) {
        setValue(LEFTOVER_ITEM, leftoverItem);
        return this;
    }

    public DistilleryRecipeJS leftoverFluid(OutputFluid leftoverFluid) {
        setValue(LEFTOVER_FLUID, leftoverFluid);
        return this;
    }

    public DistilleryRecipeJS leftovers(ItemStackProviderJS leftoverItem, OutputFluid leftoverFluid) {
        setValue(LEFTOVER_ITEM, leftoverItem);
        setValue(LEFTOVER_FLUID, leftoverFluid);
        return this;
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof DistilleryRecipe d) {
            return List.of(d.getIngredientItem().ingredient());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original distillery recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a distillery recipe?");
        }
    }
}
