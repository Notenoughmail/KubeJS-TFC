package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MoldItemBuilder extends ItemBuilder {

    public transient IntSupplier capacity;
    public transient TagKey<Fluid> acceptableFluids;

    public MoldItemBuilder(ResourceLocation i) {
        super(i);
        this.capacity = () -> 100;
        this.acceptableFluids = TFCTags.Fluids.USABLE_IN_INGOT_MOLD;
        FluidContainerItemBuilder.colorList.add(this);
    }

    @Override
    public ItemBuilder texture(String tex) {
        texture("base", tex);
        return texture("fluid", tex + "_overlay");
    }

    @Info("Sets the capacity, in mB, of the mold")
    public MoldItemBuilder capacity(int capacity) {
        this.capacity = () -> capacity;
        return this;
    }
    @Info("Sets the capacity, in mB, supplier of the mold")
    public MoldItemBuilder capacitySupplier(Supplier<Integer> capacity) {
        this.capacity = capacity::get;
        return this;
    }

    @Info("Sets the fluid tag that the mold item is allowed to hold")
    public MoldItemBuilder fluidTagAccept(ResourceLocation fluidTag) {
        this.acceptableFluids = TagKey.create(Registries.FLUID, fluidTag);
        return this;
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        ResourceUtils.fluidContainerModel(this, generator);
    }

    @Override
    public Item createObject() {
        return new MoldItem(capacity, this.acceptableFluids, createItemProperties());
    }
}
