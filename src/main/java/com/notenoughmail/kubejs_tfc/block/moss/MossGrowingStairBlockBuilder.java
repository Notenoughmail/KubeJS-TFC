package com.notenoughmail.kubejs_tfc.block.moss;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.MossGrowingCallback;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.block.custom.StairBlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossGrowingStairsBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class MossGrowingStairBlockBuilder extends StairBlockBuilder {

    public ResourceLocation mossyBlock;
    public MossGrowingCallback mossGrowth;

    public MossGrowingStairBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = Blocks.OAK_STAIRS.getRegistryName();
        mossGrowth = ((currentLevel, pos, state, needsWater) -> (!needsWater || FluidHelpers.isSame(currentLevel.getFluidState(pos), Fluids.WATER)));
    }

    public MossGrowingStairBlockBuilder mossyBlock(ResourceLocation block) {
        mossyBlock = block;
        return this;
    }

    public MossGrowingStairBlockBuilder mossyConversion(MossGrowingCallback callback) {
        mossGrowth = callback;
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingStairsBlock(Blocks.OAK_PLANKS::defaultBlockState, createProperties(), () -> KubeJSRegistries.blocks().get(mossyBlock)) {

            @Override
            public void convertToMossy(Level worldIn, BlockPos pos, BlockState state, boolean needsWater) {
                Block mossBlock = KubeJSRegistries.blocks().get(mossyBlock);
                if (!(mossBlock instanceof StairBlock)) {
                    mossBlock = Blocks.OAK_STAIRS;
                    KubeJSTFC.LOGGER.error("The provided 'mossy' block: \"{}\" is not a stair block or does not exist!", mossyBlock);
                }
                if (mossGrowth.convertToMossy(worldIn, pos, state, needsWater)) {
                    worldIn.setBlockAndUpdate(pos, Helpers.copyProperties(mossBlock.defaultBlockState(), state));
                }
            }
        };
    }
}
