package com.notenoughmail.kubejs_tfc.block.moss;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossSpreadingBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MossSpreadingBlockBuilder extends BlockBuilder {

    public MossSpreadingBlockBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Block createObject() {
        return new MossSpreadingBlock(createProperties().randomTicks());
    }
}
