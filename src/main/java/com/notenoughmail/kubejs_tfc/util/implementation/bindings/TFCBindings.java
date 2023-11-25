package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartComponent;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

@SuppressWarnings("unused")
@Info(value = "All of KubeJS TFC's bindings")
public class TFCBindings {

    @Info(value = "Provides access to all climate bindings")
    public static ClimateBindings climate = ClimateBindings.INSTANCE;
    @Info(value = "Provides access to all calendar bindings")
    public static CalendarBindings calendar = CalendarBindings.INSTANCE;
    @Info(value = "Provides access to all ingredient bindings")
    public static TFCIngredientBindings ingredient = TFCIngredientBindings.INSTANCE;
    @Info(value = "Provides access to all item stack provider bindings")
    public static ItemStackProviderBindings itemStackProvider = ItemStackProviderBindings.INSTANCE;

    // These look useless, but type wrappers and IngredientHelpers exist
    @Info(value = "Explicitly creates a block ingredient")
    public static BlockIngredient blockIngredient(BlockIngredient blockIngredient) {
        return blockIngredient;
    }

    @Info(value = "Explicitly creates a fluid ingredient")
    public static FluidIngredient fluidIngredient(FluidIngredient fluidIngredient) {
        return fluidIngredient;
    }

    @Info(value = "Explicitly creates a fluid stack ingredient")
    public static FluidStackIngredient fluidStackIngredient(FluidStackIngredient fluidStackIngredient) {
        return fluidStackIngredient;
    }

    @Info(value = "Explicitly creates a fluid stack ingredient")
    public static FluidStackIngredient fluidStackIngredient(FluidIngredient fluidIngredient, int amount) {
        return new FluidStackIngredient(fluidIngredient, amount);
    }

    // Possibly move this into a recipe sub-binding
    @Info(value = "Creates an alloy part, used in creating alloying recipes")
    public static AlloyPartComponent.AlloyPart alloyPart(String metal, double min, double max) {
        return new AlloyPartComponent.AlloyPart(metal, min, max);
    }
}
