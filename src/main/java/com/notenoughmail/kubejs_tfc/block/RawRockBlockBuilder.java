package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.dries007.tfc.common.blocks.rock.RawRockBlock;
import net.minecraft.resources.ResourceLocation;

public class RawRockBlockBuilder extends BlockBuilder {

    public transient boolean naturallySupported;

    public RawRockBlockBuilder(ResourceLocation i) {
        super(i);
        naturallySupported = false;
    }

    public RawRockBlockBuilder naturallySupported(boolean supported) {
        naturallySupported = supported;
        return this;
    }

    @Override
    public RawRockBlock createObject() {
        return new RawRockBlock(createProperties(), naturallySupported);
    }
}
