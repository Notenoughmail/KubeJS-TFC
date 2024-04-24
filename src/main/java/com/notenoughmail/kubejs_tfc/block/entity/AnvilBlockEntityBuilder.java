package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.AnvilBlockEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AnvilBlockEntityBuilder extends TFCBlockEntityBuilder<AnvilBlockEntityJS> {

    public AnvilBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<AnvilBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of(AnvilBlockEntityJS::new).build(null);
    }
}
