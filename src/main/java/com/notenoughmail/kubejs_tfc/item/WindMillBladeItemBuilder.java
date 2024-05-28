package com.notenoughmail.kubejs_tfc.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.WindmillBladeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class WindMillBladeItemBuilder extends ItemBuilder {

    public transient Color bladeColor;

    public WindMillBladeItemBuilder(ResourceLocation i) {
        super(i);
        bladeColor = ColorWrapper.BLACK;
        tag(TFCTags.Items.WINDMILL_BLADES.location());
    }

    @Info(value = "Sets the color of the windmill blade")
    public WindMillBladeItemBuilder bladeColor(Color color) {
        bladeColor = color;
        return this;
    }

    @Override
    public Item createObject() {
        return new WindmillBladeItemJS(createItemProperties(), colorFromColor(bladeColor.getRgbJS()));
    }

    private static float[] colorFromColor(int color) {
        final int i = (color & 16711680) >> 16;
        final int j = (color & 65280) >> 8;
        final int k = (color & 255);
        return new float[]{ i / 255.0F, j / 255.0F, k / 255.0F };
    }

    public static class WindmillBladeItemJS extends WindmillBladeItem {

        private final float[] color;

        public WindmillBladeItemJS(Properties properties, float[] color) {
            super(properties, DyeColor.WHITE);
            this.color = color;
        }

        @Override
        public float[] getTextureColors() {
            return color;
        }
    }
}
