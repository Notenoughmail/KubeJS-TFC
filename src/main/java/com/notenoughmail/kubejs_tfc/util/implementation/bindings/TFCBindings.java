package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartComponent;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

// TODO: JSDocs
@SuppressWarnings("unused")
public class TFCBindings {

    public static ClimateBindings climate = ClimateBindings.INSTANCE;
    public static CalendarBindings calendar = CalendarBindings.INSTANCE;
    public static TFCIngredientBindings ingredient = TFCIngredientBindings.INSTANCE;
    public static ItemStackProviderBindings itemStackProvider = ItemStackProviderBindings.INSTANCE;

    // These look useless, but type wrappers and IngredientHelpers exist
    public static BlockIngredient blockIngredient(BlockIngredient blockIngredient) {
        return blockIngredient;
    }

    public static FluidIngredient fluidIngredient(FluidIngredient fluidIngredient) {
        return fluidIngredient;
    }

    public static FluidStackIngredient fluidStackIngredient(FluidStackIngredient fluidStackIngredient) {
        return fluidStackIngredient;
    }

    // Possibly move this into a recipe sub-binding
    public static AlloyPartComponent.AlloyPart alloyPart(String metal, double min, double max) {
        return new AlloyPartComponent.AlloyPart(metal, min, max);
    }
}
