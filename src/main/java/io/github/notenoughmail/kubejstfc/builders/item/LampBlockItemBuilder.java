package io.github.notenoughmail.kubejstfc.builders.item;

import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import io.github.notenoughmail.kubejstfc.blocks.LampBlockBuilder;
import net.dries007.tfc.common.items.LampBlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class LampBlockItemBuilder extends BlockItemBuilder {

    public final transient LampBlockBuilder blockBuilder;

    public LampBlockItemBuilder(ResourceLocation i, LampBlockBuilder blockBuilder) {
        super(i);
        this.blockBuilder = blockBuilder;
    }

    @Override
    public Item createObject() {
        return new LampBlockItem(blockBuilder.get(), createItemProperties());
    }
}
