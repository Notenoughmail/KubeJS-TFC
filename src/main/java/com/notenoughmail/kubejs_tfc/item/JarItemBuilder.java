package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.JarItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class JarItemBuilder extends ItemBuilder {

    public transient ResourceLocation model;
    public transient boolean hasRemainder;

    public JarItemBuilder(ResourceLocation i) {
        super(i);
        model = newID("block/", "");
        hasRemainder = true;
        tag(TFCTags.Items.JARS.location());
    }

    @Info("Sets the jar to not have a crafting remainder")
    public JarItemBuilder withoutCraftingRemainder() {
        hasRemainder = false;
        return this;
    }

    @Info("Sets the model to be used when this jar item is placed in a jar shelf")
    public JarItemBuilder placedModel(ResourceLocation id) {
        model = id;
        return this;
    }

    @Override
    public Item createObject() {
        return new JarItem(createItemProperties(), model, hasRemainder);
    }
}
