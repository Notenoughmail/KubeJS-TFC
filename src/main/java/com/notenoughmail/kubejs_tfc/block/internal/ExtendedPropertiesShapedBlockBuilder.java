package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.ShapedBlockBuilder;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class ExtendedPropertiesShapedBlockBuilder extends ShapedBlockBuilder implements ISupportExtendedProperties {

    public transient Consumer<ExtendedPropertiesJS> props;

    public ExtendedPropertiesShapedBlockBuilder(ResourceLocation i) {
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
