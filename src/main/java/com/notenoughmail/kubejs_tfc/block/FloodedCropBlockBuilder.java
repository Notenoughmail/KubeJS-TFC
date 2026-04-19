package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class FloodedCropBlockBuilder extends AbstractCropBlockBuilder {

    public FloodedCropBlockBuilder(ResourceLocation i) {
        super(i, Type.FLOODED);
    }

    @Override
    public Block createObject() {
        return CropUtils.floodedCrop(createExtendedProperties(), stages, dead.get(), seeds.get(), nutrient, climateRange, growthMod, expiryMod);
    }
}
