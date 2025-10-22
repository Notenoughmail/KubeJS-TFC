package io.github.notenoughmail.kubejstfc.items;

import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import net.dries007.tfc.common.items.MoldItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MoldItemBuilder extends FluidCapacityItemBuilder {

    public MoldItemBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean mold() {
        return true;
    }

    @Override
    public Item createObject() {
        return new MoldItem(capacity, allowedFluids, createItemProperties());
    }
}
