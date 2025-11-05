package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.ScytheItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class ScytheItemBuilder extends HandheldItemBuilder {

    public ScytheItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        Assistant.singleTag(this, TFCTags.Items.TOOLS_SCYTHE);
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new ScytheItem(toolTier, createItemProperties());
    }
}
