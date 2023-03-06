package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.MaceItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MaceItemBuilder extends HandheldItemBuilder {

    public MaceItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        return new MaceItem(toolTier, (int) attackDamageBaseline, speedBaseline, createItemProperties());
    }
}
