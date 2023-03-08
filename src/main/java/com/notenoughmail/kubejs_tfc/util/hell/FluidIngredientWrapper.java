package com.notenoughmail.kubejs_tfc.util.hell;

import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;

public class FluidIngredientWrapper {

    public static FluidStackIngredientJS of(FluidStackIngredientJS o) {
        return FluidStackIngredientJS.of(o);
    }

    public static FluidStackIngredientJS of(FluidStackIngredientJS o, int amount) {
        var f = of(o);
        f.setAmount(amount);
        return f;
    }

    public static FluidStackIngredientJS water() {
        return new UnboundedFluidStackIngredientJS(FluidIngredient.of(KubeJSRegistries.fluids().get(FluidWrapper.WATER_ID)), (int) FluidStack.bucketAmount());
    }

    // For completeness' sake
    public static FluidStackIngredientJS lava() {
        return new UnboundedFluidStackIngredientJS(FluidIngredient.of(KubeJSRegistries.fluids().get(FluidWrapper.LAVA_ID)), (int) FluidStack.bucketAmount());
    }

    public static FluidStackIngredientJS water(int i) {
        return water().withAmount(i);
    }

    public static FluidStackIngredientJS lava(int i) {
        return lava().withAmount(i);
    }
}
