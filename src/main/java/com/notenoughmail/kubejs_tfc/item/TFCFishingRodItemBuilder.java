package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TFCFishingRodItemBuilder extends HandheldItemBuilder {

    @Nullable
    public transient Float fishingStrength;
    public static final List<TFCFishingRodItemBuilder> thisList = new ArrayList<>();
    public transient String customCastModel;

    public TFCFishingRodItemBuilder(ResourceLocation i) {
        super(i, 3f, -2.4f);
        fishingStrength = null;
        thisList.add(this);
        customCastModel = "";
    }

    @Info(value = "Sets the rod's fishing strength, defaults the a value based on the speed of the rod's tier")
    public TFCFishingRodItemBuilder fishingStrength(float f) {
        fishingStrength = f;
        return this;
    }

    @Info(value = "Sets the rod's model when cast")
    public TFCFishingRodItemBuilder castModel(String model) {
        customCastModel = model;
        return this;
    }

    @Info(value = "Allows this rod to hold small fishing bait by adding it to the correct tag")
    public TFCFishingRodItemBuilder smallBait() {
        return (TFCFishingRodItemBuilder) tag(TFCTags.Items.HOLDS_SMALL_FISHING_BAIT.location());
    }

    @Info(value = "Allows this rod to hold large fishing bait by adding it the correct tag")
    public TFCFishingRodItemBuilder largeBait() {
        return (TFCFishingRodItemBuilder) tag(TFCTags.Items.HOLDS_LARGE_FISHING_BAIT.location());
    }

    @Override
    public Item createObject() {
        return new TFCFishingRodItem(createItemProperties(), toolTier) {

            @Override
            public float getFishingStrength() {
                return fishingStrength == null ? super.getFishingStrength() : fishingStrength;
            }
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        ModelUtils.ITEMS.fishingRod(id, generator, customCastModel);
    }
}
