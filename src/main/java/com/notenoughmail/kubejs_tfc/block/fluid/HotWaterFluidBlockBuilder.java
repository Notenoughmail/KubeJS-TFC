package com.notenoughmail.kubejs_tfc.block.fluid;

import com.notenoughmail.kubejs_tfc.fluid.HotWaterFluidBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.HotWaterBlockJS;
import dev.latvian.mods.kubejs.fluid.FluidBlockBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class HotWaterFluidBlockBuilder extends FluidBlockBuilder {

    private final HotWaterFluidBuilder builder;

    public HotWaterFluidBlockBuilder(HotWaterFluidBuilder b) {
        super(b);
        builder = b;
    }

    @Override
    public Block createObject() {
        return new HotWaterBlockJS(builder, Block.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable());
    }
}
