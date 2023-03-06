package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.TFCHoeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class TFCHoeItemBuilder extends HandheldItemBuilder {

    public TFCHoeItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        return new TFCHoeItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties());
    }
}
