package io.github.notenoughmail.kubejstfc.builders.fluid;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.KubeHotWaterBlock;
import dev.latvian.mods.kubejs.fluid.FluidBlockBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class HotWaterFluidBlockBuilder extends FluidBlockBuilder {

    private final SpringWaterBuilder builder;

    public HotWaterFluidBlockBuilder(SpringWaterBuilder b) {
        super(b);
        builder = b;
    }

    @Override
    public Block createObject() {
        return new KubeHotWaterBlock(builder, Block.Properties.ofFullCopy(Blocks.WATER).noCollission().strength(100.0F).noLootTable());
    }
}
