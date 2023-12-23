package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.BerryBushBlockEntityJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BerryBushBlockEntityBuilder extends TFCBlockEntityBuilder<BerryBushBlockEntityJS> {

    public BerryBushBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<BerryBushBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of((pos, state) -> new BerryBushBlockEntityJS(this.get(), pos, state), getBlocks()).build(null);
    }
}
