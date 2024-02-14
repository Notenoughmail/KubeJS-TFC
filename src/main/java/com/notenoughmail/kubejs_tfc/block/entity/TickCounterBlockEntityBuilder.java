package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.TickCounterBlockEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TickCounterBlockEntityBuilder extends TFCBlockEntityBuilder<TickCounterBlockEntityJS> {

    public TickCounterBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<TickCounterBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of((pos, state) -> new TickCounterBlockEntityJS(this.get(), pos, state), getBlocks()).build(null);
    }
}
