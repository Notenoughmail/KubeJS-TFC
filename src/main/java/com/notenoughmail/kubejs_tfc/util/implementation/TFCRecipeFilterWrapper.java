package com.notenoughmail.kubejs_tfc.util.implementation;

import com.notenoughmail.kubejs_tfc.filter.BlockIngredientFilter;
import com.notenoughmail.kubejs_tfc.filter.FluidOutputFilter;
import com.notenoughmail.kubejs_tfc.filter.FluidStackIngredientFilter;
import com.notenoughmail.kubejs_tfc.filter.ItemStackProviderFilter;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;

public class TFCRecipeFilterWrapper {

    public static RecipeFilter blockIngredient(BlockIngredientJS block) {
        return new BlockIngredientFilter(block, false);
    }

    public static RecipeFilter blockIngredient(BlockIngredientJS block, boolean exact) {
        return new BlockIngredientFilter(block, exact);
    }

    public static RecipeFilter fluidOutput(FluidStackJS fluid) {
        return new FluidOutputFilter(fluid, false);
    }

    public static RecipeFilter fluidOutput(FluidStackJS fluid, boolean exact) {
        return new FluidOutputFilter(fluid, exact);
    }

    public static RecipeFilter fluidInput(FluidStackIngredientJS fluidIngredient) {
        return new FluidStackIngredientFilter(fluidIngredient, false);
    }

    public static RecipeFilter fluidInput(FluidStackIngredientJS fluidIngredient, boolean exact) {
        return new FluidStackIngredientFilter(fluidIngredient, exact);
    }

    public static RecipeFilter itemProvider(ItemStackProviderJS itemProvider) {
        return new ItemStackProviderFilter(itemProvider, false);
    }

    public static RecipeFilter itemProvider(ItemStackProviderJS itemProvider, boolean exact) {
        return new ItemStackProviderFilter(itemProvider, exact);
    }
}