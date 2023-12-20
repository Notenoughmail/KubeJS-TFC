package com.notenoughmail.kubejs_tfc.block.entity;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.TickCounterBlockEntityJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TickCounterBlockEntityBuilder extends BuilderBase<BlockEntityType<TickCounterBlockEntityJS>> {

    public transient BlockBuilder block;
    public transient List<Supplier<? extends Block>> allowedBlocks;

    public TickCounterBlockEntityBuilder(ResourceLocation i) {
        super(i);
        allowedBlocks = new ArrayList<>();
    }

    public void addBlock(BlockBuilder block) {
        allowedBlocks.add(block);
    }

    @Override
    public RegistryInfo<?> getRegistryType() {
        return RegistryInfo.BLOCK_ENTITY_TYPE;
    }

    @Override
    public BlockEntityType<TickCounterBlockEntityJS> createObject() {
        return BlockEntityType.Builder.of((pos, state) -> new TickCounterBlockEntityJS(this, pos, state), getBlocks()).build(null);
    }

    // Allows the blocks using this to not create a new tick counter for every block registered
    private Block[] getBlocks() {
        final Block[] blocks = new Block[allowedBlocks.size()];
        for (int i = 0 ; i < allowedBlocks.size() ; i++) {
            blocks[i] = allowedBlocks.get(i).get();
        }
        return blocks;
    }
}
