package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.dries007.tfc.common.items.GlassworkingItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class GlassworkingItemBuilder extends ItemBuilder {

    public transient GlassOperation operation;

    public GlassworkingItemBuilder(ResourceLocation i) {
        super(i);
        operation = GlassOperation.SAW;
    }

    @Info(value = "Sets the glassworking operation type this item is capable of doing")
    public GlassworkingItemBuilder operation(GlassOperation operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public Item createObject() {
        return new GlassworkingItem(createItemProperties(), operation);
    }
}
