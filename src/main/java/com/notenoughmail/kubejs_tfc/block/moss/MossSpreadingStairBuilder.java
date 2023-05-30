package com.notenoughmail.kubejs_tfc.block.moss;

import dev.latvian.mods.kubejs.block.custom.StairBlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossSpreadingStairBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MossSpreadingStairBuilder extends StairBlockBuilder {

    public MossSpreadingStairBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Block createObject() {
        return new MossSpreadingStairBlock(Blocks.OAK_PLANKS::defaultBlockState, createProperties().randomTicks());
    }
}
