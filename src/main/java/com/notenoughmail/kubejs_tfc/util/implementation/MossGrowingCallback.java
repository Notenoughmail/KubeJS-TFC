package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface MossGrowingCallback {
    boolean convertToMossy(BlockContainerJS container, boolean needsWater);

    BiPredicate<Level, BlockPos> IS_ADJACENT_TO_WATER = (level, pos) -> {
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (Direction dir : Helpers.DIRECTIONS) {
            final FluidState state = level.getFluidState(cursor.setWithOffset(pos, dir));
            if (FluidHelpers.isSame(state, Fluids.WATER)) {
                return true;
            }
        }
        return false;
    };

    MossGrowingCallback ADJACENT = (container, needsWater) -> (!needsWater || IS_ADJACENT_TO_WATER.test(container.getLevel(), container.getPos()));
    MossGrowingCallback WATER_LOGGED = (container, needsWater) -> (!needsWater || FluidHelpers.isSame(container.getBlockState().getFluidState(), Fluids.WATER));
}
