package com.notenoughmail.kubejs_tfc.block.moss;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.MossGrowingCallback;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.block.custom.WallBlockBuilder;
import net.dries007.tfc.common.blocks.rock.MossGrowingWallBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class MossGrowingWallBlockBuilder extends WallBlockBuilder {

    public ResourceLocation mossyBlock;
    public MossGrowingCallback mossGrowth;

    public MossGrowingWallBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = Blocks.COBBLESTONE_WALL.getRegistryName();
        mossGrowth = ((currentLevel, pos, state, needsWater) -> (!needsWater || FluidHelpers.isSame(currentLevel.getFluidState(pos), Fluids.WATER)));
    }

    public MossGrowingWallBlockBuilder mossyBlock(ResourceLocation block) {
        mossyBlock = block;
        return this;
    }

    public MossGrowingWallBlockBuilder mossyConversion(MossGrowingCallback callback) {
        mossGrowth = callback;
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingWallBlock(createProperties(), () -> null) {

            @Override
            public void convertToMossy(Level worldIn, BlockPos pos, BlockState state, boolean needsWater) {
                Block mossBlock = KubeJSRegistries.blocks().get(mossyBlock);
                if (!(mossBlock instanceof WallBlock)) {
                    mossBlock = Blocks.COBBLESTONE_WALL;
                    KubeJSTFC.LOGGER.error("The provided 'mossy' block \"{}\" is not a wall block or does not exist!", mossyBlock);
                }
                if (mossGrowth.convertToMossy(worldIn, pos, state, needsWater)) {
                    worldIn.setBlockAndUpdate(pos, Helpers.copyProperties(mossBlock.defaultBlockState(), state));
                }
            }
        };
    }
}
