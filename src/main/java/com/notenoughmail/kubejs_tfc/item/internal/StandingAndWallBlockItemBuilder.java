package com.notenoughmail.kubejs_tfc.item.internal;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

public class StandingAndWallBlockItemBuilder extends BlockItemBuilder {

    public transient final BlockBuilder wallBlock;

    public StandingAndWallBlockItemBuilder(ResourceLocation i, BlockBuilder block, BlockBuilder wallBlock) {
        super(i);
        this.blockBuilder = block;
        this.wallBlock = wallBlock;
    }

    @Override
    public Item createObject() {
        return new StandingAndWallBlockItem(blockBuilder.get(), wallBlock.get(), createItemProperties(), Direction.DOWN);
    }
}
