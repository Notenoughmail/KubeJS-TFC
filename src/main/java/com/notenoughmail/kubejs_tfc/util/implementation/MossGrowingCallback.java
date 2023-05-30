package com.notenoughmail.kubejs_tfc.util.implementation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface MossGrowingCallback {
    boolean convertToMossy(Level currentLevel, BlockPos pos, BlockState state, boolean needsWater);
}
