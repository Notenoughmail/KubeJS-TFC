package com.notenoughmail.kubejs_tfc.util.implementation;

import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.BlockIngredientAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.FluidIngredientAccessor;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class IngredientHelpers {

    // Mixing into records is such a pain
    public static final IngredientType.Factory<Block, BlockIngredient> blockIngredientFactory = ((BlockIngredientAccessor) (Object) new BlockIngredient(null)).accessor$getFactory();
    public static final IngredientType.Factory<Fluid, FluidIngredient> fluidIngredientFactory = ((FluidIngredientAccessor) (Object) new FluidIngredient(null)).accessor$getFactory();

    public static <T, I extends IngredientType<T>> I of(T obj, IngredientType.Factory<T, I> factory) {
        return factory.factory().apply(List.of(new IngredientType.ObjEntry<>(obj)));
    }

    public static BlockIngredient of(Block block) {
        return of(block, blockIngredientFactory);
    }

    public static FluidIngredient of(Fluid fluid) {
        return of(fluid, fluidIngredientFactory);
    }
}
