package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public class FluidContainerItemBuilder extends ItemBuilder {

    public transient boolean canPlaceLiquid;
    public transient boolean canPlaceSource;
    public transient int capacity;
    public transient TagKey<Fluid> whitelist;
    public transient String filledDisplayName;

    public static List<FluidContainerItemBuilder> thisList = new ArrayList<>();

    public FluidContainerItemBuilder(ResourceLocation i) {
        super(i);
        canPlaceLiquid = false;
        canPlaceSource = false;
        capacity = 100;
        whitelist = TFCTags.Fluids.USABLE_IN_JUG;
        filledDisplayName = "%s " + displayName;
        thisList.add(this);
    }

    public FluidContainerItemBuilder canPlaceLiquid(boolean b) {
        canPlaceLiquid = b;
        return this;
    }

    public FluidContainerItemBuilder canPlaceLiquidSource(boolean b) {
        canPlaceSource = b;
        return this;
    }

    public FluidContainerItemBuilder capacity(int i) {
        capacity = i;
        return this;
    }

    public FluidContainerItemBuilder fluidTagAccept(ResourceLocation tag) {
        whitelist = TagKey.create(Registries.FLUID, tag);
        return this;
    }

    public FluidContainerItemBuilder filledDisplayName(String s) {
        filledDisplayName = s;
        return this;
    }

    @Override
    public Item createObject() {
        return new FluidContainerItem(createItemProperties(), () -> capacity, whitelist, canPlaceLiquid, canPlaceSource) {

            // Override so it's not effected by the config
            @Override
            public boolean canPlaceSourceBlocks() {
                return canPlaceSource;
            }
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.itemModel(id, m -> ModelUtils.ITEMS.fluidContainerModelJson(m, id));
    }

    @Override
    public void generateLang(LangEventJS lang) {
        super.generateLang(lang);
        lang.add(translationKey + ".filled", filledDisplayName);
    }
}
