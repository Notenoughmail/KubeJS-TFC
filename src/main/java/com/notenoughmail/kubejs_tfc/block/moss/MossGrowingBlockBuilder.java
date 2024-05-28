package com.notenoughmail.kubejs_tfc.block.moss;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.MossGrowingCallback;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.rock.MossGrowingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public class MossGrowingBlockBuilder extends BlockBuilder {

    public ResourceLocation mossyBlock;
    public MossGrowingCallback mossGrowth;

    public MossGrowingBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = RegistryInfo.BLOCK.getId(Blocks.AIR);
        mossGrowth = MossGrowingCallback.ADJACENT;
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
        return new MossGrowingBlock(createProperties(), () -> null) {

            @Override
            public void convertToMossy(Level worldIn, BlockPos pos, BlockState state, boolean needsWater) {
                Block mossBlock = RegistryInfo.BLOCK.getValue(mossyBlock);
                if (mossBlock == null) {
                    mossBlock = Blocks.AIR;
                    KubeJSTFC.error("The provided 'mossy' block: \"{}\" does not exist!", mossyBlock);
                }
                if (mossGrowth.convertToMossy(new BlockContainerJS(worldIn, pos), needsWater)) {
                    worldIn.setBlock(pos, mossBlock.defaultBlockState(), 3);
                }
            }
        };
    }
}
