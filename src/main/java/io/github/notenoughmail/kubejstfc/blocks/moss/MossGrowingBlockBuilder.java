package io.github.notenoughmail.kubejstfc.blocks.moss;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.rock.MossGrowingBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

@ReturnsSelf
@SuppressWarnings("unused")
public class MossGrowingBlockBuilder extends BlockBuilder {

    public transient Supplier<Block> mossyBlock;

    public MossGrowingBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = () -> Blocks.OAK_PLANKS;
    }

    @Info("The block this converts into when moss grows to it")
    public MossGrowingBlockBuilder mossyBlock(Holder<Block> block) {
        mossyBlock = Assistant.holderAsSupplier(block);
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingBlock(createProperties(), mossyBlock);
    }
}
