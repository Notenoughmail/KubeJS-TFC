package com.notenoughmail.kubejs_tfc.util.hell;

import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class UnboundedFluidStackIngredientJS extends FluidStackIngredientJS {

    private FluidStackIngredient fluidStackIngredient;

    public UnboundedFluidStackIngredientJS(FluidIngredient fluidIngredient, int amount) {
        fluidStackIngredient = new FluidStackIngredient(fluidIngredient, amount);
    }

    @Override
    public FluidStackIngredient getFluidstackIngredient() {
        return fluidStackIngredient;
    }

    @Override
    public int getAmount() {
        return fluidStackIngredient.amount();
    }

    @Override
    public void setAmount(int amount) {
        fluidStackIngredient = new FluidStackIngredient(fluidStackIngredient.ingredient(), amount);
    }

    @Override
    public FluidStackIngredientJS copy() {
        return new UnboundedFluidStackIngredientJS(fluidStackIngredient.ingredient(), fluidStackIngredient.amount());
    }
}
