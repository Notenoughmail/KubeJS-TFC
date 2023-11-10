package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FluidIngredient.class, remap = false)
public interface FluidIngredientAccessor {

    @Accessor(value = "FACTORY", remap = false)
    IngredientType.Factory<Fluid, FluidIngredient> accessor$getFactory();
}
