package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.TFCDirtBlockBuilder;
import net.dries007.tfc.common.blocks.soil.TFCRootedDirtBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class TFCRootedDirtBlockBuilder extends BlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public TFCRootedDirtBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
    }

    @Override
    public Block createObject() {
        return new TFCRootedDirtBlock(createProperties(), parent, parent.mud.get());
    }
}
