package com.notenoughmail.kubejs_tfc.block.moss;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.MossGrowingCallback;
import dev.latvian.mods.kubejs.block.custom.SlabBlockBuilder;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.rock.MossGrowingSlabBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluids;

public class MossGrowingSlabBlockBuilder extends SlabBlockBuilder {

    public ResourceLocation mossyBlock;
    public MossGrowingCallback mossGrowthFull;
    public MossGrowingCallback mossGrowthHalf;

    public MossGrowingSlabBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = RegistryInfo.BLOCK.getId(Blocks.OAK_PLANKS);
        mossGrowthFull = ((container, needsWater) -> (!needsWater || FluidHelpers.isSame(container.minecraftLevel.getFluidState(container.getPos().above()), Fluids.WATER)));
        mossGrowthHalf = ((container, needsWater) -> (!needsWater || FluidHelpers.isSame(container.minecraftLevel.getFluidState(container.getPos()), Fluids.WATER)));
    }

    public MossGrowingSlabBlockBuilder mossySlab(ResourceLocation block) {
        mossyBlock = block;
        return this;
    }

    public MossGrowingSlabBlockBuilder mossyConversionFull(MossGrowingCallback callback) {
        mossGrowthFull = callback;
        return this;
    }

    public MossGrowingSlabBlockBuilder mossyConversionHalf(MossGrowingCallback callback) {
        mossGrowthHalf = callback;
        return this;
    }

    public MossGrowingSlabBlockBuilder mossyConversion(MossGrowingCallback callback) {
        mossGrowthFull = callback;
        mossGrowthHalf = callback;
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingSlabBlock(createProperties(), () -> null) {

            @Override
            public void convertToMossy(Level worldIn, BlockPos pos, BlockState state, boolean needsWater) {
                Block mossBlock = RegistryInfo.BLOCK.getValue(mossyBlock);
                if (!(mossBlock instanceof SlabBlock)) {
                    mossBlock = Blocks.OAK_SLAB;
                    KubeJSTFC.LOGGER.error("The provided 'mossy' block: \"{}\" is not a slab block or does not exist!", mossyBlock);
                }
                if (state.getValue(TYPE) == SlabType.DOUBLE) {
                    if (mossGrowthFull.convertToMossy(new BlockContainerJS(worldIn, pos), needsWater)) {
                        worldIn.setBlockAndUpdate(pos, Helpers.copyProperties(mossBlock.defaultBlockState(), state));
                    }
                } else {
                    if (mossGrowthHalf.convertToMossy(new BlockContainerJS(worldIn, pos), needsWater)) {
                        worldIn.setBlockAndUpdate(pos, Helpers.copyProperties(mossBlock.defaultBlockState(), state));
                    }
                }
            }
        };
    }
}
