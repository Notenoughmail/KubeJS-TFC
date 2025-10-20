package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.items.TFCMaceItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MaceItemBuilder extends HandheldItemBuilder {

    public MaceItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
    }

    @Override
    public Item createObject() {
        Assistant.toolItemAttributes(this);
        return new TFCMaceItem(createItemProperties());
    }
}
