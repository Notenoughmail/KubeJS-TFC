package io.github.notenoughmail.kubejstfc.items;

import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import net.dries007.tfc.common.items.JugItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class JugItemBuilder extends FluidCapacityItemBuilder.WithLang {

    public JugItemBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    public Item createObject() {
        return new JugItem(createItemProperties(), capacity, allowedFluids);
    }
}
