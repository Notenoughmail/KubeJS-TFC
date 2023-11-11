package com.notenoughmail.kubejs_tfc.util.implementation;

import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.BlockIngredientAccessor;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class IngredientHelpers {

    // Mixing into records is such a pain
    public static final IngredientType.Factory<Block, BlockIngredient> blockIngredientFactory = ((BlockIngredientAccessor) (Object) new BlockIngredient(null)).accessor$getFactory();

    public static BlockIngredient block(Block block) {
        return new BlockIngredient(List.of(new IngredientType.ObjEntry<>(block)));
    }

    public static FluidIngredient fluid(Fluid fluid) {
        return new FluidIngredient(List.of(new IngredientType.ObjEntry<>(fluid)));
    }
}
