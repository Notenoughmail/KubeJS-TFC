package com.notenoughmail.kubejs_tfc.addons.firmalife.item;

import com.eerussianguy.firmalife.common.items.WateringCanItem;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WateringCanItemBuilder extends ItemBuilder {

    public WateringCanItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Item createObject() {
        return new WateringCanItem(createItemProperties());
    }
}
