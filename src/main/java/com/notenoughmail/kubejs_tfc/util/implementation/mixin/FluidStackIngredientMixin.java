package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FluidStackIngredient.class, remap = false)
public abstract class FluidStackIngredientMixin implements InputFluid {

    @Shadow(remap = false)
    public abstract FluidIngredient ingredient();

    @Shadow(remap = false)
    public abstract int amount();

    @Override
    public FluidLike kjs$copy(long a) {
        return UtilsJS.cast(new FluidStackIngredient(
                ingredient(),
                (int) a
        ));
    }

    @Override
    public boolean matches(FluidLike other) {
        return other instanceof FluidStackJS js && ingredient().test(js.getFluid());
    }

    @Override
    public long kjs$getAmount() {
        return amount();
    }
}
