package com.notenoughmail.kubejs_tfc.util.hell;

import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.level.material.Fluids;

public class EmptyFluidStackIngredientJS extends FluidStackIngredientJS {

    public static final EmptyFluidStackIngredientJS INSTANCE = new EmptyFluidStackIngredientJS();

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public FluidStackIngredient getFluidstackIngredient() {
        return null;
    }

    @Override
    public FluidIngredient getFluidIngedient() {
        return FluidIngredient.of(Fluids.EMPTY);
    }

    @Override
    public int getAmount() {
        return 0;
    }

    @Override
    public void setAmount(int amount) {
    }

    @Override
    public String toString() {
        return "Fluid.empty";
    }

    @Override
    public FluidStackIngredientJS copy() {
        return this;
    }
}
