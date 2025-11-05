package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ChiselItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ChiselItemBuilder extends HandheldItemBuilder {

    public ChiselItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        Assistant.singleTag(this, TFCTags.Items.TOOLS_CHISEL);
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new ChiselItem(toolTier, createItemProperties());
    }
}
