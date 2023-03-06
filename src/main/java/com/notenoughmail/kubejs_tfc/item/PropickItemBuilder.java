package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.PropickItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class PropickItemBuilder extends HandheldItemBuilder {

    public PropickItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        return new PropickItem(toolTier, attackDamageBaseline, speedBaseline, createItemProperties());
    }
}
