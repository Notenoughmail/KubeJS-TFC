package com.notenoughmail.kubejs_tfc.block.moss;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.MossGrowingCallback;
import dev.latvian.mods.kubejs.block.custom.WallBlockBuilder;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.rock.MossGrowingWallBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MossGrowingWallBlockBuilder extends WallBlockBuilder {

    public ResourceLocation mossyBlock;
    public MossGrowingCallback mossGrowth;

    public MossGrowingWallBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = RegistryInfo.BLOCK.getId(Blocks.COBBLESTONE_WALL);
        mossGrowth = MossGrowingCallback.DEFAULT;
    }

    public MossGrowingWallBlockBuilder mossyWall(ResourceLocation block) {
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
                Block mossBlock = RegistryInfo.BLOCK.getValue(mossyBlock);
                if (!(mossBlock instanceof WallBlock)) {
                    mossBlock = Blocks.COBBLESTONE_WALL;
                    KubeJSTFC.LOGGER.error("The provided 'mossy' block \"{}\" is not a wall block or does not exist!", mossyBlock);
                }
                if (mossGrowth.convertToMossy(new BlockContainerJS(worldIn, pos), needsWater)) {
                    worldIn.setBlockAndUpdate(pos, Helpers.copyProperties(mossBlock.defaultBlockState(), state));
                }
            }
        };
    }
}
