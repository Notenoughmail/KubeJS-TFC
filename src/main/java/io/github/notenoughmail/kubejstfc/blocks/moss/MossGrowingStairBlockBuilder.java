package io.github.notenoughmail.kubejstfc.blocks.moss;

import dev.latvian.mods.kubejs.block.custom.StairBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.rock.MossGrowingStairsBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MossGrowingStairBlockBuilder extends StairBlockBuilder {

    public transient Supplier<Block> mossyBlock;

    public MossGrowingStairBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = () -> Blocks.OAK_STAIRS;
    }

    public MossGrowingStairBlockBuilder mossyBlock(Holder<Block> block) {
        mossyBlock = Assistant.holderAsSupplier(block);
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingStairsBlock(Blocks.OAK_PLANKS::defaultBlockState, createProperties(), mossyBlock);
    }
}
