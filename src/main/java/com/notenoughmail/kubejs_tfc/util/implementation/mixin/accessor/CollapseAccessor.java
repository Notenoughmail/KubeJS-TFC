package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.util.tracker.Collapse;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = Collapse.class, remap = false)
public interface CollapseAccessor {

    @Accessor(remap = false)
    BlockPos getCenterPos();

    @Accessor(remap = false)
    List<BlockPos> getNextPositions();

    @Accessor(remap = false)
    double getRadiusSquared();
}
