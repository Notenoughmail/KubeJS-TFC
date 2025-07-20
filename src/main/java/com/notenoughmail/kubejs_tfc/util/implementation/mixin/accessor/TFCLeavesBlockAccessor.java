package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = TFCLeavesBlock.class, remap = false)
public interface TFCLeavesBlockAccessor {

    @Invoker(value = "getDistanceProperty", remap = false)
    IntegerProperty kubejs_tfc$AccessDistProp();

    @Accessor(value = "maxDecayDistance", remap = false)
    int kubejs_tfc$MaxDist();

    // We're here for the dist prop anyway, may as well get this as well
    @Invoker(value = "updateDistance", remap = false)
    int kubejs_tfc$UpdateDistance(LevelAccessor level, BlockPos pos);
}
