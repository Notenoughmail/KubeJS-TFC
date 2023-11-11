package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.dries007.tfc.common.blocks.rock.RawRockBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

// TODO: Model generation
public class RawRockBlockBuilder extends BlockBuilder {

    public transient boolean naturallySupported;
    @Nullable
    public transient Component rockTypeTooltip;

    public RawRockBlockBuilder(ResourceLocation i) {
        super(i);
        naturallySupported = false;
        rockTypeTooltip = null;
    }

    public RawRockBlockBuilder naturallySupported(boolean supported) {
        naturallySupported = supported;
        return this;
    }

    public RawRockBlockBuilder rockTypeTooltip(Component comp) {
        rockTypeTooltip = comp;
        return this;
    }

    @Override
    public RawRockBlock createObject() {
        return new RawRockBlock(createProperties(), naturallySupported, rockTypeTooltip);
    }
}
