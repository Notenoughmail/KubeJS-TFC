package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class SpreadingCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient Supplier<Supplier<? extends Block>> fruitBlock;

    public SpreadingCropBlockBuilder(ResourceLocation i) {
        super(i);
        fruitBlock = () -> () -> Blocks.HONEY_BLOCK; // Why the hell not
        type = Type.SPREADING;
    }

    public SpreadingCropBlockBuilder fruitBlock(ResourceLocation fruitBlock) {
        this.fruitBlock = () -> () -> RegistryInfo.BLOCK.getValue(fruitBlock);
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtils.spreadingCrop(createExtendedProperties(), stages, dead, seeds, nutrient, climateRange, fruitBlock);
    }
}
