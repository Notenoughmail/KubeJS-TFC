package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.TickCounterBlockEntityJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;

public class TickCounterBlockEntityBuilder extends TFCBlockEntityBuilder<TickCounterBlockEntityJS> {

    public TickCounterBlockEntityBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public BlockEntityType<TickCounterBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of((pos, state) -> new TickCounterBlockEntityJS(this.get(), pos, state), getBlocks()).build(null);
    }
}
