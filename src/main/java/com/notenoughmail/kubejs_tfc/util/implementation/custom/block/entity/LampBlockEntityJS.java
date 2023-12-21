package com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity;

import net.dries007.tfc.common.blockentities.LampBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LampBlockEntityJS extends LampBlockEntity {

    private final BlockEntityType<?> type;

    public LampBlockEntityJS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = type;
    }

    @Override
    public BlockEntityType<?> getType() {
        return type;
    }
}
