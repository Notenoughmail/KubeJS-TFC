package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.minecraft.world.level.material.Fluids;

@FunctionalInterface
public interface MossGrowingCallback {
    boolean convertToMossy(BlockContainerJS container, boolean needsWater);

    MossGrowingCallback DEFAULT = (container, needsWater) -> (!needsWater || FluidHelpers.isSame(container.getLevel().getFluidState(container.getPos()), Fluids.WATER));
    MossGrowingCallback ABOVE = (container, needsWater) -> (!needsWater || FluidHelpers.isSame(container.getLevel().getFluidState(container.getPos().above()), Fluids.WATER));
}
