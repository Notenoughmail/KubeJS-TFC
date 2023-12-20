package com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity;

import com.notenoughmail.kubejs_tfc.block.entity.TickCounterBlockEntityBuilder;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TickCounterBlockEntityJS extends TickCounterBlockEntity {

    public TickCounterBlockEntityJS(TickCounterBlockEntityBuilder builder, BlockPos pos, BlockState state) {
        this(builder.get(), pos, state);
    }

    protected TickCounterBlockEntityJS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
