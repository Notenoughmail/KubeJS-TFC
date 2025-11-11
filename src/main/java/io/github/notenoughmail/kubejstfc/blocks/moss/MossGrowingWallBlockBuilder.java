package io.github.notenoughmail.kubejstfc.blocks.moss;

import dev.latvian.mods.kubejs.block.custom.WallBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.blocks.rock.MossGrowingWallBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MossGrowingWallBlockBuilder extends WallBlockBuilder {

    public transient Supplier<Block> mossyBlock;

    public MossGrowingWallBlockBuilder(ResourceLocation i) {
        super(i);
        mossyBlock = () -> Blocks.COBBLESTONE_WALL;
    }

    public MossGrowingWallBlockBuilder mossyBlock(Holder<Block> block) {
        mossyBlock = Assistant.holderAsSupplier(block);
        return this;
    }

    @Override
    public Block createObject() {
        return new MossGrowingWallBlock(createProperties(), mossyBlock);
    }
}
