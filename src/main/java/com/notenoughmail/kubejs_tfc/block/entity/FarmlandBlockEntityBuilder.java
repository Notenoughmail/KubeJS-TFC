package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.FarmlandBlockEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FarmlandBlockEntityBuilder extends TFCBlockEntityBuilder<FarmlandBlockEntityJS> {

    public FarmlandBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<FarmlandBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of((pos, level) -> new FarmlandBlockEntityJS(this.get(), pos, level), getBlocks()).build(null);
    }
}
