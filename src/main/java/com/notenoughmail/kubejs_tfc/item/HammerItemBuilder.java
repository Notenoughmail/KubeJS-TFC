package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.HammerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class HammerItemBuilder extends HandheldItemBuilder {

    @Nullable
    public transient ResourceLocation metalTexture;

    public HammerItemBuilder(ResourceLocation i) {
        super(i, 3, -2.4f);
        metalTexture = null;
        tag(TFCTags.Items.HAMMERS.location());
    }

    @Info(value = "Sets the texture location used when the hammer is in a trip hammer")
    public HammerItemBuilder metalTexture(ResourceLocation texture) {
        metalTexture = texture;
        return this;
    }

    @Override
    public Item createObject() {
        return new HammerItem(toolTier, attackDamageBaseline, speedBaseline, createItemProperties(), metalTexture);
    }
}
