package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartComponent;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

@SuppressWarnings("unused")
@Info("All of KubeJS TFC's bindings")
public class TFCBindings {

    @Info("Provides access to all climate bindings")
    public static final ClimateBindings climate = ClimateBindings.INSTANCE;
    @Info("Provides access to all calendar bindings")
    public static final CalendarBindings calendar = CalendarBindings.INSTANCE;
    @Info("A collection of various un-categorized features and utilities")
    public static final MiscBindings misc = MiscBindings.INSTANCE;

    // These look useless, but type wrappers and IngredientHelpers exist
    @Info("Explicitly creates a block ingredient")
    public static BlockIngredient blockIngredient(BlockIngredient blockIngredient) {
        return blockIngredient;
    }

    @Info("Explicitly creates a fluid ingredient")
    public static FluidIngredient fluidIngredient(FluidIngredient fluidIngredient) {
        return fluidIngredient;
    }

    @Info("Explicitly creates a fluid stack ingredient")
    public static FluidStackIngredient fluidStackIngredient(FluidStackIngredient fluidStackIngredient) {
        return fluidStackIngredient;
    }

    @Info("Explicitly creates a fluid stack ingredient")
    public static FluidStackIngredient fluidStackIngredient(FluidIngredient fluidIngredient, int amount) {
        return new FluidStackIngredient(fluidIngredient, amount);
    }

    // Possibly move this into a recipe sub-binding
    @Info("Creates an alloy part, used in creating alloying recipes")
    public static AlloyPartComponent.AlloyPart alloyPart(String metal, double min, double max, boolean keepOriginalBounds) {
        return new AlloyPartComponent.AlloyPart(metal, min, max, keepOriginalBounds);
    }

    @Info("Creates an alloy part, used in creating alloying recipes")
    public static AlloyPartComponent.AlloyPart alloyPart(String metal, double min, double max) {
        return alloyPart(metal, min, max, true);
    }
}
