package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class ExtendedPropertiesMultipartShapedBlockBuilder extends MultipartShapedBlockBuilder implements ISupportExtendedProperties {

    public transient Consumer<ExtendedPropertiesJS> props;

    public ExtendedPropertiesMultipartShapedBlockBuilder(ResourceLocation i) {
        super(i);
        props = p -> {};
    }

    @Override
    public BlockBuilder extendedProperties(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return Util.make(extendedPropsJS(), props).delegate();
    }
}
