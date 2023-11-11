package com.notenoughmail.kubejs_tfc.item;

import com.ljuangbminecraft.tfcchannelcasting.common.TFCCCTags;
import com.ljuangbminecraft.tfcchannelcasting.common.items.TFCCCItems;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class MoldItemBuilder extends ItemBuilder {

    public transient int capacity;
    public transient TagKey<Fluid> acceptableFluids;

    public MoldItemBuilder(ResourceLocation i) {
        super(i);
        this.capacity = 100;
        this.acceptableFluids = TFCTags.Fluids.USABLE_IN_INGOT_MOLD;
    }

    public MoldItemBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public MoldItemBuilder fluidTagAccept(ResourceLocation fluidTag) {
        this.acceptableFluids = TagKey.create(Registries.FLUID, fluidTag);
        return this;
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.itemModel(id, m -> ModelUtils.ITEMS.fluidContainerModelJson(m, id));
    }

    @Override
    public Item createObject() {
        return new MoldItem(() -> this.capacity, this.acceptableFluids, createItemProperties());
    }
}
