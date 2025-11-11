package io.github.notenoughmail.kubejstfc.blocks.moss;

import dev.latvian.mods.kubejs.block.custom.SlabBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.rock.MossGrowingSlabBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MossGrowingSlabBlockBuilder extends SlabBlockBuilder {

    public transient Supplier<Block> mossyBlock;

    public MossGrowingSlabBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = () -> Blocks.OAK_SLAB;
    }

    public MossGrowingSlabBlockBuilder mossyBlock(Holder<Block> block) {
        mossyBlock = Assistant.holderAsSupplier(block);
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingSlabBlock(createProperties(), mossyBlock);
    }
}
