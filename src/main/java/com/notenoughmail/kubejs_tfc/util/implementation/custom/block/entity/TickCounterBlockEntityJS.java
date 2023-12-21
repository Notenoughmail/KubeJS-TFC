package com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity;

import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

// Required to widen the visibility of the constructor
public class TickCounterBlockEntityJS extends TickCounterBlockEntity {

    public TickCounterBlockEntityJS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
