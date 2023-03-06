package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ScytheItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ScytheItemBuilder extends HandheldItemBuilder {

    public transient TagKey<Block> mineableBlocks;

    public ScytheItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        this.mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_SCYTHE;
    }

    public ScytheItemBuilder mineableBlocks(ResourceLocation i) {
        this.mineableBlocks = TagKey.create(Registry.BLOCK_REGISTRY, i);
        return this;
    }

    @Override
    public Item createObject() {
        return new ScytheItem(toolTier, attackDamageBaseline, speedBaseline, mineableBlocks, createItemProperties());
    }
}
