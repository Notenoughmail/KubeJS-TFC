package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.util.Support;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Support.class, remap = false)
public interface SupportAccessor {

    @Accessor(value = "ingredient", remap = false)
    BlockIngredient kubejs_tfc$Ingredient();
}
