package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.MoldItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class MoldItemBuilder extends ItemBuilder {

    public transient IntSupplier capacity;
    public transient TagKey<Fluid> acceptableFluids;

    public static final List<MoldItemBuilder> thisList = new ArrayList<>();

    public MoldItemBuilder(ResourceLocation i) {
        super(i);
        this.capacity = () -> 100;
        this.acceptableFluids = TFCTags.Fluids.USABLE_IN_INGOT_MOLD;
        thisList.add(this);
    }

    @Info(value = "Sets the capacity, in mB, of the mold")
    public MoldItemBuilder capacity(int capacity) {
        this.capacity = () -> capacity;
        return this;
    }
    @Info(value = "Sets the capacity, in mB, supplier of the mold")
    public MoldItemBuilder capacitySupplier(IntSupplier capacity) {
        this.capacity = capacity;
        return this;
    }

    @Info(value = "Sets the fluid tag that the mold item is allowed to hold")
    public MoldItemBuilder fluidTagAccept(ResourceLocation fluidTag) {
        this.acceptableFluids = TagKey.create(Registries.FLUID, fluidTag);
        return this;
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.itemModel(id, m -> ModelUtils.ITEMS.fluidContainerModelJson(m, id));
        if (modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(id), modelJson);
            return;
        }

        generator.itemModel(id, m -> {
            if (!parentModel.isEmpty()) {
                m.parent(parentModel);
            } else {
                ModelUtils.ITEMS.fluidContainerModelJson(m, id);
            }
        });
    }

    @Override
    public Item createObject() {
        return new MoldItem(capacity, this.acceptableFluids, createItemProperties());
    }
}
