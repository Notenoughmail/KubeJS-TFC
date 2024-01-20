package com.notenoughmail.kubejs_tfc.block.entity;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CropBlockEntityBuilder extends TFCBlockEntityBuilder<CropBlockEntity> {

    public CropBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<CropBlockEntity> createObject() {
        return BlockEntityType.Builder.of((pos, level) -> new CropBlockEntity(this.get(), pos, level), getBlocks()).build(null);
    }
}
