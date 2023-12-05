package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidContainerItemBuilder extends ItemBuilder {

    public transient boolean canPlaceLiquid;
    public transient boolean canPlaceSource;
    public transient int capacity;
    public transient TagKey<Fluid> whitelist;
    @Nullable
    public transient Component filledDisplayName;
    public transient String filledTranslationKey;

    public static List<FluidContainerItemBuilder> thisList = new ArrayList<>();

    public FluidContainerItemBuilder(ResourceLocation i) {
        super(i);
        canPlaceLiquid = false;
        canPlaceSource = false;
        capacity = 100;
        whitelist = TFCTags.Fluids.USABLE_IN_JUG;
        filledTranslationKey = "";
        filledDisplayName = null;
        thisList.add(this);
    }

    @Info(value = "Determines if the item can place fluids in world")
    public FluidContainerItemBuilder canPlaceLiquid(boolean b) {
        canPlaceLiquid = b;
        return this;
    }

    @Info(value = "Determines if the item can place source blocks when placing fluid blocks in world")
    public FluidContainerItemBuilder canPlaceLiquidSource(boolean b) {
        canPlaceSource = b;
        return this;
    }

    @Info(value = "Sets the capacity, in mB, of the fluid container")
    public FluidContainerItemBuilder capacity(int i) {
        capacity = i;
        return this;
    }

    @Info(value = "Sets the fluid tag that the item is allowed to hold")
    public FluidContainerItemBuilder fluidTagAccept(ResourceLocation tag) {
        whitelist = TagKey.create(Registries.FLUID, tag);
        return this;
    }

    @Info(value = "Sets the ")
    public FluidContainerItemBuilder filledDisplayName(Component c) {
        filledDisplayName = c;
        return this;
    }

    public FluidContainerItemBuilder filledTranslationKey(String s) {
        filledTranslationKey = s;
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

    protected String getFilledTranslationKey() {
        if (filledTranslationKey.isEmpty()) {
            return getTranslationKeyGroup() + "." + id.getNamespace() + "." + id.getPath() + ".filled";
        } else {
            return filledTranslationKey;
        }
    }

    @Override
    public void generateLang(LangEventJS lang) {
        super.generateLang(lang);
        if (filledDisplayName != null) {
            lang.add(id.getNamespace(), getFilledTranslationKey(), filledDisplayName.getString());
        } else {
            lang.add(id.getNamespace(), getFilledTranslationKey(), "%s " + UtilsJS.snakeCaseToTitleCase(id.toString()));
        }
    }
}
