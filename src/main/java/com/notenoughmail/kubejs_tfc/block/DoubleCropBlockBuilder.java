package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

// TODO: Dead double crop, wild double crop
public class DoubleCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient int doubleStages;
    public transient boolean requiresStick;

    public DoubleCropBlockBuilder(ResourceLocation i) {
        super(i);
        stages = 4;
        doubleStages = 4;
        type = Type.CLIMBING;
        requiresStick = false;
    }

    @Override
    public DoubleCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 6) {
            stages = i;
        }
        return this;
    }

    public DoubleCropBlockBuilder doubleStages(int i) {
        if (i >= 1 && i <= 6) {
            doubleStages = i;
        }
        return this;
    }

    public DoubleCropBlockBuilder requiresStick(boolean requiresStick) {
        this.requiresStick = requiresStick;
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtils.doubleCrop(createExtendedProperties(), stages, doubleStages, dead, seeds, nutrient, climateRange, requiresStick);
    }
}
