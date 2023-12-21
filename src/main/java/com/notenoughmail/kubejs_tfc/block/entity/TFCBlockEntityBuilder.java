package com.notenoughmail.kubejs_tfc.block.entity;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// A base class for BE types which don't need any customization and can be re-used for multiple blocks without issue
public abstract class TFCBlockEntityBuilder<T extends BlockEntity> extends BuilderBase<BlockEntityType<T>> {

    public transient List<Supplier<? extends Block>> allowedBlocks;

    public TFCBlockEntityBuilder(ResourceLocation i) {
        super(i);
        allowedBlocks = new ArrayList<>();
    }

    @Override
    public RegistryInfo<?> getRegistryType() {
        return RegistryInfo.BLOCK_ENTITY_TYPE;
    }

    public void addBlock(BlockBuilder block) {
        allowedBlocks.add(block);
    }

    // Allows the blocks using this to not create a new type for every block registered
    protected Block[] getBlocks() {
        final Block[] blocks = new Block[allowedBlocks.size()];
        for (int i = 0 ; i < allowedBlocks.size() ; i++) {
            blocks[i] = allowedBlocks.get(i).get();
        }
        return blocks;
    }
}
