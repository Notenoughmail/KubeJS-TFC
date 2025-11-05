package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.items.TFCHoeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class TFCHoeItemBuilder extends HandheldItemBuilder {

    public TFCHoeItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        Assistant.singleTag(this, ItemTags.HOES);
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new TFCHoeItem(toolTier, createItemProperties());
    }
}
