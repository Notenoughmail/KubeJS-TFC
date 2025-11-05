package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class ToolItemBuilder extends HandheldItemBuilder {

    public transient TagKey<Block> mineableBlocks;

    public ToolItemBuilder(ResourceLocation i) {
        super(i, 3F, -2.4F);
        mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_KNIFE;
    }

    @Info("Adds this item to the `tfc:knives` tag and sets its mineable blocks tag to `tfc:mineable_with_knife`")
    public ToolItemBuilder knife() {
        mineableBlocks = TFCTags.Blocks.MINEABLE_WITH_KNIFE;
        Assistant.singleTag(this, TFCTags.Items.TOOLS_KNIFE);
        return this;
    }

    @Info("sets the block tag that the tool can properly dig at full speed")
    public ToolItemBuilder mineableBlocksTag(ResourceLocation blockTag) {
        this.mineableBlocks = BlockTags.create(blockTag);
        return this;
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new ToolItem(toolTier, mineableBlocks, createItemProperties());
    }
}
