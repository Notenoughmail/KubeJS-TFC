package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class DefaultCropBlockBuilder extends AbstractCropBlockBuilder {

    public DefaultCropBlockBuilder(ResourceLocation i) {
        super(i);
        type = Type.DEFAULT;
    }

    @Override
    public Block createObject() {
        return CropUtils.defaultCrop(createExtendedProperties(), stages, dead, seeds, nutrient, climateRange);
    }
}
