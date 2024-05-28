package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.items.JarItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class JarItemBuilder extends ItemBuilder {

    public transient ResourceLocation model;

    public JarItemBuilder(ResourceLocation i) {
        super(i);
        model = id;
    }

    @Info(value = "Sets the model to be used when this jar item is placed in a jar shelf")
    public JarItemBuilder placedModel(ResourceLocation id) {
        model = id;
        return this;
    }

    @Override
    public Item createObject() {
        return new JarItem(createItemProperties(), model, true);
    }
}
