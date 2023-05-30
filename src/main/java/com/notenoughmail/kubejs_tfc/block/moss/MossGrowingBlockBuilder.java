package com.notenoughmail.kubejs_tfc.block.moss;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.MossGrowingCallback;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossGrowingBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class MossGrowingBlockBuilder extends BlockBuilder {

    public ResourceLocation mossyBlock;
    public MossGrowingCallback mossGrowth;

    public MossGrowingBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = Blocks.AIR.getRegistryName();
        mossGrowth = ((currentLevel, pos, state, needsWater) -> (!needsWater || FluidHelpers.isSame(currentLevel.getFluidState(pos.above()), Fluids.WATER)));
    }

    public MossGrowingBlockBuilder mossyBlock(ResourceLocation block) {
        mossyBlock = block;
        return this;
    }

    public MossGrowingBlockBuilder mossyConversion(MossGrowingCallback callback) {
        mossGrowth = callback;
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingBlock(createProperties(), () -> KubeJSRegistries.blocks().get(mossyBlock)) {

            @Override
            public void convertToMossy(Level worldIn, BlockPos pos, BlockState state, boolean needsWater) {
                Block mossBlock = KubeJSRegistries.blocks().get(mossyBlock);
                if (mossBlock == null) {
                    mossBlock = Blocks.AIR;
                    KubeJSTFC.LOGGER.error("The provided 'mossy' block: \"{}\" does not exist!", mossyBlock);
                }
                if (mossGrowth.convertToMossy(worldIn, pos, state, needsWater)) {
                    worldIn.setBlock(pos, mossBlock.defaultBlockState(), 3);
                }
            }
        };
    }
}