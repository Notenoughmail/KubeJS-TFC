package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.WindmillBladeItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class WindMillBladeItemBuilder extends ItemBuilder {

    public static final List<WindMillBladeItemBuilder> thisList = new ArrayList<>();
    private static final ResourceLocation defaultTex = Helpers.identifier("textures/entity/misc/windmill_blade.png");

    public transient Color bladeColor;
    public transient ResourceLocation bladeTexture;

    public WindMillBladeItemBuilder(ResourceLocation i) {
        super(i);
        bladeColor = ColorWrapper.BLACK;
        tag(TFCTags.Items.ALL_WINDMILL_BLADES.location());
        thisList.add(this);
    }

    @Info(value = "Sets the color of the windmill blade's sail")
    public WindMillBladeItemBuilder bladeColor(Color color) {
        bladeColor = color;
        return this;
    }

    @Info(value = "Sets the texture of the whole windmill blade, defaults to `tfc:textures/entity/misc/windmill_blade.png`")
    public WindMillBladeItemBuilder bladeTexture(ResourceLocation texture) {
        bladeTexture = texture;
        return this;
    }

    @Override
    public Item createObject() {
        return new WindmillBladeItem(createItemProperties());
    }

    @HideFromJS
    public float[] getColor() {
        final int rgb = bladeColor.getRgbJS();
        final int r = (rgb & 0xFF0000) >> 16;
        final int g = (rgb & 0xFF00) >> 8;
        final int b = (rgb & 0xFF);
        return new float[] { r / 255.0F, g / 255.0F, b / 255.0F };
    }

    @HideFromJS
    public ResourceLocation getBladeTexture() {
        if (bladeTexture == null) {
            return defaultTex;
        }
        return bladeTexture.withPath(p -> p.endsWith(".png") ? p : p + ".png").withPath(p -> p.startsWith("textures/") ? p : "textures/" + p);
    }
}
