package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.MutableToolTier;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class TFCFishingRodItemBuilder extends ItemBuilder {

    public transient MutableToolTier toolTier;
    public transient boolean useUniqueCastTexture;

    public TFCFishingRodItemBuilder(ResourceLocation i) {
        super(i);
        this.toolTier = new MutableToolTier(Tiers.IRON);
        this.useUniqueCastTexture = false;
    }

    public TFCFishingRodItemBuilder tier(Tier tier) {
        this.toolTier = new MutableToolTier(tier);
        return this;
    }

    public TFCFishingRodItemBuilder useUniqueCastTexture() {
        this.useUniqueCastTexture = true;
        return this;
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.json(AssetJsonGenerator.asItemModelLocation(id), ModelUtils.ITEMS.fishingRodModelJson(id, useUniqueCastTexture));
    }

    @Override
    public Item createObject() {
        return new TFCFishingRodItem(createItemProperties(), toolTier);
    }
}
