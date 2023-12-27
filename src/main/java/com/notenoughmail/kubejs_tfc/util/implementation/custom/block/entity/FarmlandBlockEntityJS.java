package com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FarmlandBlockEntityJS extends FarmlandBlockEntity {

    public FarmlandBlockEntityJS(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
