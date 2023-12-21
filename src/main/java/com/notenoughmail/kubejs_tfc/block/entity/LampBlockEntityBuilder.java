package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.LampBlockEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LampBlockEntityBuilder extends TFCBlockEntityBuilder<LampBlockEntityJS> {

    public LampBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<LampBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of((pos, state) -> new LampBlockEntityJS(this.get(), pos, state), getBlocks()).build(null);
    }
}
