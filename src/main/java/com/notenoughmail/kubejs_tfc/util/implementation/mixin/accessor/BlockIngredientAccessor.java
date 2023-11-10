package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BlockIngredient.class, remap = false)
public interface BlockIngredientAccessor {

    @Accessor(value = "FACTORY", remap = false)
    IngredientType.Factory<Block, BlockIngredient> accessor$getFactory();
}
