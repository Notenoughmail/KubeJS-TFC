package com.notenoughmail.kubejs_tfc.block.moss;

import dev.latvian.mods.kubejs.block.custom.WallBlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossSpreadingWallBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MossSpreadingWallBlockBuilder extends WallBlockBuilder {

    public MossSpreadingWallBlockBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Block createObject() {
        return new MossSpreadingWallBlock(createProperties().randomTicks());
    }
}
