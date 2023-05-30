package com.notenoughmail.kubejs_tfc.block.moss;

import dev.latvian.mods.kubejs.block.custom.SlabBlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossSpreadingSlabBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MossSpreadingSlabBuilder extends SlabBlockBuilder {

    public MossSpreadingSlabBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Block createObject() {
        return new MossSpreadingSlabBlock(createProperties().randomTicks());
    }
}
